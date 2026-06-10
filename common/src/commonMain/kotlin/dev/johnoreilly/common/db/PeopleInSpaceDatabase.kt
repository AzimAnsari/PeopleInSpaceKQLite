package dev.johnoreilly.common.db

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kqlite.database.KQLiteDatabase
import com.kqlite.database.KQLiteDriver
import com.kqlite.migration.KQLiteMigration
import com.kqlite.table.KQLiteTable

class PeopleInSpaceDatabase(
    file: String,
    private val kqliteDriver: KQLiteDriver = PeopleInSpaceDbDriver(BundledSQLiteDriver())
) : KQLiteDatabase(file, VERSION) {

    companion object {
        const val NAME: String = "peopleinspace.db"
        const val VERSION: Int = 3
    }

    override fun getKQLiteDriver(): KQLiteDriver {
        return kqliteDriver
    }

    override fun getKQLiteTables(): List<KQLiteTable> {
        return listOf(TblPeople)
    }

    override fun getKQLiteMigrations(): List<KQLiteMigration> {
        return listOf(
            PeopleInSpaceDbMigration12(),
            PeopleInSpaceDbMigration23()
        )
    }
}