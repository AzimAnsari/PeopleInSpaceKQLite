package dev.johnoreilly.common.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.util.TreeMap


class DbStatement(
    private val db: SQLiteDatabase,
    private val sql: String,
) : androidx.sqlite.SQLiteStatement {

    private var cursor: Cursor? = null
    private val args = TreeMap<Int, Any?>()

    override fun bindBlob(index: Int, value: ByteArray) {
        args[index] = value
    }

    override fun bindDouble(index: Int, value: Double) {
        args[index] = value
    }

    override fun bindLong(index: Int, value: Long) {
        args[index] = value
    }

    override fun bindText(index: Int, value: String) {
        args[index] = value
    }

    override fun bindNull(index: Int) {
        args[index] = null
    }

    override fun getBlob(index: Int): ByteArray {
        return cursor!!.getBlob(index)
    }

    override fun getDouble(index: Int): Double {
        return cursor!!.getDouble(index)
    }

    override fun getLong(index: Int): Long {
        return cursor!!.getLong(index)
    }

    override fun getText(index: Int): String {
        return cursor!!.getString(index)
    }

    override fun isNull(index: Int): Boolean {
        return cursor!!.isNull(index)
    }

    override fun getColumnCount(): Int {
        return cursor!!.columnCount
    }

    override fun getColumnName(index: Int): String {
        return cursor!!.getColumnName(index)
    }

    override fun getColumnType(index: Int): Int {
        return cursor!!.getType(index)
    }

    override fun step(): Boolean {
//        val type = DatabaseUtils.getSqlStatementType(sql)
        val c = cursor
        if (c == null) {
            val c2 = db.rawQuery(sql, args.values.map { it.toString() }.toTypedArray())
            cursor = c2
            return c2.moveToFirst()
        } else {
            return c.moveToNext()
        }

//        if (type == DatabaseUtils.STATEMENT_SELECT || sql.contains(" RETURNING ")) {
//            val c = db.rawQuery(sql, args.values.map { it.toString() }.toTypedArray())
//            cursor = c
//            return c.moveToPosition(-1)
//        } else {
//            db.execSQL(sql, args.values.map { it.toString() }.toTypedArray())
//            return false
//        }
    }

    override fun reset() {
        cursor?.close()
        cursor = null
    }

    override fun clearBindings() {
        args.clear()
    }

    override fun close() {
        cursor?.close()
        args.clear()
    }
}
