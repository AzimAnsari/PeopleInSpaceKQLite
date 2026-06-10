package dev.johnoreilly.common.db

import com.kqlite.database.KQLiteDatabase
import com.kqlite.migration.KQLiteMigration

class PeopleInSpaceDbMigration12 : KQLiteMigration(1, 2) {
    override fun migrate(database: KQLiteDatabase) {
        val schema = database.schema()

        schema
            .alterTable(TblPeople)
            .addColumn(TblPeople.personImageUrl)

        schema
            .alterTable(TblPeople)
            .addColumn(TblPeople.personBio)
    }
}