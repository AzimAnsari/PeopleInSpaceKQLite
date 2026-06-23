package dev.johnoreilly.common.db

import com.kqlite.column.Bind
import com.kqlite.column.notNull
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteAdapter
import com.kqlite.table.KQLiteTable
import dev.johnoreilly.common.remote.Assignment

object TblPeople : KQLiteTable("People"), KQLiteAdapter<Assignment> {
    val name = textColumn("name").notNull().primaryKey()
    val craft = textColumn("craft").notNull()
    val personImageUrl = textColumn("personImageUrl")
    val personBio = textColumn("personBio")
    val nationality = textColumn("nationality").notNull().default("")


    override fun mapper(cursor: KQLiteCursor): Assignment {
        return Assignment(
            name = cursor[name],
            craft = cursor[craft],
            personImageUrl = cursor[personImageUrl],
            personBio = cursor[personBio],
            nationality = cursor[nationality]
        )
    }

    override fun binder(
        bind: Bind,
        item: Assignment
    ) {
        bind.apply {
            name.bind(item.name)
            craft.bind(item.craft)
            personImageUrl.bind(item.personImageUrl)
            personBio.bind(item.personBio)
            nationality.bind(item.nationality)
        }
    }
}