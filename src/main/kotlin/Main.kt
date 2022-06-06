import enums.MetaCommandResult
import enums.PrepareResult
import enums.StatementType
import kotlin.system.exitProcess


fun printPrompt() {
    print("db > ")
}

fun readInput(): String {
    val x: String? = readLine()

    if (x == null){
        println("Error reading input")
        exitProcess(1)
    } else {
        return x
    }
}


fun doMetaCommand(command: String): MetaCommandResult {
    if (command == ".exit") {
        exitProcess(1)
    } else {
        return MetaCommandResult.META_COMMAND_UNRECOGNIZED_COMMAND
    }
}

data class Statement(var statementType: StatementType)

fun prepareStatement(command: String, statement: Statement): PrepareResult {
    if (command.startsWith("insert")){
        statement.statementType = StatementType.STATEMENT_INSERT
        return PrepareResult.PREPARE_SUCCESS
    }
    if (command.startsWith("select")) {
        statement.statementType = StatementType.STATEMENT_SELECT
        return PrepareResult.PREPARE_SUCCESS
    }
    return PrepareResult.PREPARE_UNRECOGNIZED_STATEMENT
}

fun main() {
    while (true) {
        printPrompt()

        val line: String = readInput()

        if (line.startsWith(".")){
            when (doMetaCommand(line)) {
                MetaCommandResult.META_COMMAND_SUCCESS -> continue
                MetaCommandResult.META_COMMAND_UNRECOGNIZED_COMMAND -> {
                    println("Unrecognized command: $line")
                    continue
                }
            }
        }
    }
}