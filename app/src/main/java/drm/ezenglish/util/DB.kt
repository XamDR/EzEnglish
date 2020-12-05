package drm.ezenglish.util

const val TblQuestion: String = "Pregunta"
const val QuestionIdColumn: String = "id"
const val QuestionContentColumn: String = "contenido"
const val QuestionOptionAColumn: String = "opcionA"
const val QuestionOptionBColumn: String = "opcionB"
const val QuestionOptionCColumn: String = "opcionC"
const val QuestionOptionDColumn: String = "opcionD"
const val CreateTable: String = "CREATE TABLE $TblQuestion ($QuestionIdColumn TEXT, $QuestionContentColumn TEXT, " +
        "$QuestionOptionAColumn TEXT, $QuestionOptionBColumn TEXT, $QuestionOptionCColumn TEXT, $QuestionOptionDColumn TEXT)"
const val DropTable: String = "DROP TABLE IF EXISTS $TblQuestion"