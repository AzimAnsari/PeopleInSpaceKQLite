package dev.johnoreilly.common.db

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteDriver
import com.kqlite.database.KQLiteDriver


class PeopleInSpaceDbDriver(private val sqliteDriver: SQLiteDriver) : KQLiteDriver {
    override fun open(file: String, flags: Int?): SQLiteConnection {
        return sqliteDriver.open(file)
    }
}