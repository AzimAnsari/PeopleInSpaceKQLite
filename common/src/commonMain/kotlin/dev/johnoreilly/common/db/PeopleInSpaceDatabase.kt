package dev.johnoreilly.common.db

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kqlite.database.KQLiteDatabase
import com.kqlite.migration.KQLiteMigration
import com.kqlite.table.KQLiteTable

class PeopleInSpaceDatabase(
    absolutePath: String,
) : KQLiteDatabase(absolutePath, VERSION, PeopleInSpaceDbDriver(BundledSQLiteDriver())) {

    companion object {
        const val NAME: String = "peopleinspace.db"
        const val VERSION: Int = 3
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