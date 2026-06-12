package dev.johnoreilly.common.repository

import androidx.sqlite.SQLiteConnection
import co.touchlab.kermit.Logger
import com.kqlite.statement.insert
import com.kqlite.statement.quickDelete
import com.kqlite.statement.quickSelect
import com.kqlite.table.Action
import dev.johnoreilly.common.db.TblPeople
import dev.johnoreilly.common.di.PeopleInSpaceDatabaseWrapper
import dev.johnoreilly.common.remote.Assignment
import dev.johnoreilly.common.remote.AstroviewerApi
import dev.johnoreilly.common.remote.IssPosition
import dev.johnoreilly.common.remote.OrbitPoint
import dev.johnoreilly.common.remote.PeopleInSpaceApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.annotation.Single


interface PeopleInSpaceRepositoryInterface {
    fun fetchPeopleAsFlow(): Flow<List<Assignment>>
    fun pollISSPosition(): Flow<IssPosition>
    suspend fun fetchISSFuturePosition(): List<OrbitPoint>
    suspend fun fetchAndStorePeople()
}

@Single
class PeopleInSpaceRepository(
    private val peopleInSpaceApi: PeopleInSpaceApi,
    private val peopleInSpaceDatabase: PeopleInSpaceDatabaseWrapper,
    private val astroviewerApi: AstroviewerApi,
) : PeopleInSpaceRepositoryInterface {

    val coroutineScope: CoroutineScope = MainScope()
    private var connection: SQLiteConnection? = null
    private val peopleFlow = MutableStateFlow<List<Assignment>>(emptyList())
    val logger = Logger.withTag("PeopleInSpaceRepository")

    init {
        coroutineScope.launch {
            fetchAndStorePeople()
        }
    }

    override fun fetchPeopleAsFlow(): Flow<List<Assignment>> = peopleFlow.asStateFlow()

    private fun loadPeople(): List<Assignment> {
        connection = connection ?: peopleInSpaceDatabase.instance.open()

        return TblPeople.quickSelect().use { cursor ->
            cursor
                .asSequence()
                .map(TblPeople::mapToAssignment)
                .toList()
        }
    }

    fun refreshPeople() {
        val people = loadPeople()
        logger.d { "Found ${people.size} people." }
        peopleFlow.value = people
    }

    override suspend fun fetchAndStorePeople() {
        logger.d { "fetchAndStorePeople" }
        try {
            val result = peopleInSpaceApi.fetchPeople()

            connection = connection ?: peopleInSpaceDatabase.instance.open()
            // this is very basic implementation for now that removes all existing rows
            // in db and then inserts results from api request
            peopleInSpaceDatabase.instance.withTransaction {

                // DELETE FROM People;
                TblPeople.quickDelete(where = null)

                // INSERT OR REPLACE INTO People VALUES(?,?,?,?,?);
                val prepared = TblPeople.insert(onConflict = Action.REPLACE)
                result.people.forEach { assignment ->
                    prepared.bind {
                        TblPeople.bindAssignment(bind = this, assignment = assignment)
                    }.execute()
                }
                prepared.close()
            }
            refreshPeople()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // TODO report error up to UI
            logger.w(e) { "Exception during fetchAndStorePeople: $e" }
        }
    }

    override suspend fun fetchISSFuturePosition(): List<OrbitPoint> {
        return astroviewerApi.fetchISSFuturePositions().orbitData
    }

    override fun pollISSPosition(): Flow<IssPosition> {
        return flow {
            while (true) {
                try {
                    val position = peopleInSpaceApi.fetchISSPosition().iss_position
                    if (currentCoroutineContext().isActive) {
                        emit(position)
                    }
                    logger.d { position.toString() }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // TODO report error up to UI
                    logger.w(e) { "Exception during pollISSPosition: $e" }
                }
                delay(POLL_INTERVAL)
            }
        }
    }

    companion object {
        private const val POLL_INTERVAL = 10000L
    }
}
