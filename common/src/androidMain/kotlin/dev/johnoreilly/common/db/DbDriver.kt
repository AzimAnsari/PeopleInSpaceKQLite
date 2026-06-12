package dev.johnoreilly.common.db

import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.SQLiteConnection
import com.kqlite.database.KQLiteDriver

class DbDriver(private val file: String) : KQLiteDriver {
    override fun open(path: String, flags: Int?): SQLiteConnection {
        val sqlite = flags?.let { SQLiteDatabase.openDatabase(file, null, it) }
            ?: SQLiteDatabase.openOrCreateDatabase(file, null)
        return DbConnection(sqlite)
    }
}