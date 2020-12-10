package drm.ezenglish.entities

data class Question(val content: String = "", val optionA: String = "", val optionB: String = "",
                    val optionC: String = "", val optionD: String = "", val answer: String = "")