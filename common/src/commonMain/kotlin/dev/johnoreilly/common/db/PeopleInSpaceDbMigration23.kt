package dev.johnoreilly.common.db

import com.kqlite.database.KQLiteDatabase
import com.kqlite.migration.KQLiteMigration

class PeopleInSpaceDbMigration23 : KQLiteMigration(2, 3) {
    override fun migrate(database: KQLiteDatabase) {
        val schema = database.schema()

        schema
            .alterTable(TblPeople)
            .addColumn(TblPeople.nationality)

    }
}