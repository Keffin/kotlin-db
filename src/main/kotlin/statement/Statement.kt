package statement

import enums.StatementType

data class Statement(var statementType: StatementType = StatementType.STATEMENT_NULL)
