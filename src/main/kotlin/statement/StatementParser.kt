package statement

import enums.PrepareResult
import enums.StatementType

class StatementParser {
    fun prepareStatement(command: String, statement: Statement): Pair<PrepareResult, Statement> {
        if (command.startsWith("insert")){
            statement.statementType = StatementType.STATEMENT_INSERT
            val args = command.split(" ")
            if (args.size < 4) {
                return Pair(PrepareResult.PREPARE_SYNTAX_ERROR, statement)
            }
            return Pair(PrepareResult.PREPARE_SUCCESS, statement)
        }
        if (command.startsWith("select")) {
            statement.statementType = StatementType.STATEMENT_SELECT
            return Pair(PrepareResult.PREPARE_SUCCESS, statement)
        }
        return Pair(PrepareResult.PREPARE_UNRECOGNIZED_STATEMENT, statement)
    }

    fun executeStatement(statement: Statement) {
        when (statement.statementType) {
            StatementType.STATEMENT_INSERT -> println("Insert occurs")
            StatementType.STATEMENT_SELECT -> println("Select occurs.")
            else -> throw IllegalStateException("Incorrect statement state")
        }
    }
}