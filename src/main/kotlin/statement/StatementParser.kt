package statement

import database.Row
import database.Table
import enums.ExecutionStatus
import enums.PrepareResult
import enums.StatementType
import java.nio.ByteBuffer

class StatementParser(private val byteBuffer: ByteBuffer, private val table: Table) {

    fun prepareStatement(command: String, statement: Statement): Pair<PrepareResult, Statement> {
        if (command.startsWith("insert")){
            statement.statementType = StatementType.STATEMENT_INSERT

            val args = command.split(" ")
            if (args.size < 4) {
                return Pair(PrepareResult.PREPARE_SYNTAX_ERROR, statement)
            }
            statement.row = Row(args[1].toInt(), args[2], args[3])
            return Pair(PrepareResult.PREPARE_SUCCESS, statement)
        }
        if (command.startsWith("select")) {
            statement.statementType = StatementType.STATEMENT_SELECT
            return Pair(PrepareResult.PREPARE_SUCCESS, statement)
        }
        return Pair(PrepareResult.PREPARE_UNRECOGNIZED_STATEMENT, statement)
    }

    fun executeStatement(statement: Statement): ExecutionStatus {
        return when (statement.statementType) {
            StatementType.STATEMENT_INSERT -> executeInsert(statement, table)
            StatementType.STATEMENT_SELECT -> executeSelect(table)
            else -> throw IllegalStateException("Incorrect statement state")
        }
    }

    private fun executeInsert(statement: Statement, table: Table): ExecutionStatus {
        if (table.numberOfRows >= table.tableMaxNumberOfPages){
            throw IllegalStateException("Table is full!")
        }

        val row: Row? = statement.row

        row?.serialize(byteBuffer)

        table.addToRow(row)
        table.numberOfRows++
        println("Executing insert...")

        return ExecutionStatus.EXECUTE_SUCCESS
    }


    private fun executeSelect(table: Table): ExecutionStatus {
        for (row in table.rows){
            val deserializedRow: Row = row.deserialize(byteBuffer)
            formatRow(deserializedRow)
        }
        println("Executing select...")
        return ExecutionStatus.EXECUTE_SUCCESS
    }

    private fun formatRow(row: Row) {
        println("Id: ${row.id}, Username: ${row.userName}, Email: ${row.email}")
    }
}