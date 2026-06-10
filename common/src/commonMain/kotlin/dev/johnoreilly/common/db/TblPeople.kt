package dev.johnoreilly.common.db

import com.kqlite.column.Bind
import com.kqlite.column.notNull
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteTable
import dev.johnoreilly.common.remote.Assignment

object TblPeople : KQLiteTable("People") {
    val name = textColumn("name").notNull().primaryKey()
    val craft = textColumn("craft").notNull()
    val personImageUrl = textColumn("personImageUrl")
    val personBio = textColumn("personBio")
    val nationality = textColumn("nationality").notNull().default("")

    fun mapToAssignment(cursor: KQLiteCursor): Assignment {
        return Assignment(
            name = cursor[name],
            craft = cursor[craft],
            personImageUrl = cursor[personImageUrl],
            personBio = cursor[personBio],
            nationality = cursor[nationality]
        )
    }

    fun bindAssignment(bind: Bind, assignment: Assignment) {
        with(bind) {
            name.bind(assignment.name)
            craft.bind(assignment.craft)
            personImageUrl.bind(assignment.personImageUrl)
            personBio.bind(assignment.personBio)
            nationality.bind(assignment.nationality)
        }
    }
}