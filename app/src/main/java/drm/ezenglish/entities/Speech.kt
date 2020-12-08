package drm.ezenglish.entities

data class Speech(val text: String = "Hello my Name is Max", val userSaid: String = "",
                  val isListening: Boolean = false, val error: String? = null)