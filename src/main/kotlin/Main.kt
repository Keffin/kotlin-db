import database.Table
import enums.MetaCommandResult
import enums.PrepareResult
import metacommand.MetaCommand
import statement.Statement
import statement.StatementParser
import java.nio.ByteBuffer
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

fun main() {
    val byteBuffer: ByteBuffer = ByteBuffer.allocate(4096)
    val table: Table = Table(0, mutableListOf())
    val statementParser = StatementParser(byteBuffer, table)
    val metaCommand = MetaCommand()

    while (true) {
        printPrompt()

        val line: String = readInput()

        if (line.startsWith(".")){
            when (metaCommand.doMetaCommand(line)) {
                MetaCommandResult.META_COMMAND_SUCCESS -> continue
                MetaCommandResult.META_COMMAND_UNRECOGNIZED_COMMAND -> println("Unrecognized command: $line")
            }
        }
        else {
            val statement = Statement()
            val statementEvaluation: Pair<PrepareResult, Statement> = statementParser.prepareStatement(line, statement)

            if (statementEvaluation.first == PrepareResult.PREPARE_UNRECOGNIZED_STATEMENT) {
                throw IllegalArgumentException("Input was faulty")
            }

            statementParser.executeStatement(statementEvaluation.second)
            println("Executed.")
        }

    }
}