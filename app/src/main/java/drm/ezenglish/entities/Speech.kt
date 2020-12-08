package drm.ezenglish.entities

data class Speech(val text: String = "", val userSaid: String = "",
                  val isListening: Boolean = false, val error: String? = null)