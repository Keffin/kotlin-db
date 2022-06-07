package metacommand

import enums.MetaCommandResult
import kotlin.system.exitProcess

class MetaCommand {
    fun doMetaCommand(command: String): MetaCommandResult {
        if (command == ".exit") {
            exitProcess(1)
        } else {
            return MetaCommandResult.META_COMMAND_UNRECOGNIZED_COMMAND
        }
    }
}