package dev.johnoreilly.common.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement

class DbConnection(private val sqLiteDatabase: SQLiteDatabase) : SQLiteConnection {
    override fun prepare(sql: String): SQLiteStatement {
        Log.d("Azim", sql)
        return DbStatement(sqLiteDatabase, sql)
    }

    override fun close() {
        Log.d("DbConnection", "close")
        sqLiteDatabase.close()
    }
}