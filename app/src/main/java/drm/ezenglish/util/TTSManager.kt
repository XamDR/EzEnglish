package drm.ezenglish.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.*

class TTSManager(private val context: Context) {
    private lateinit var tts: TextToSpeech

    fun init() {
        try {
            tts = TextToSpeech(context) { TextToSpeech.OnInitListener { status -> onInit(status) } }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun speak(text: String, isInitial: Boolean) {
        if (isInitial) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        else {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
        }
    }

    fun shutDown() = tts.shutdown()

    private fun onInit(status: Int) {
        val spanish = Locale("es")

        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(spanish)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "Este Lenguaje no esta permitido.", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(context, "Fallo al inicializar.", Toast.LENGTH_SHORT).show()
        }
    }
}