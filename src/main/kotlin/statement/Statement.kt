package statement

import database.Row
import enums.StatementType

data class Statement(var statementType: StatementType = StatementType.STATEMENT_NULL, var row: Row? = null)
