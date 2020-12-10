package drm.ezenglish.viewmodels

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import drm.ezenglish.App
import drm.ezenglish.entities.Speech
import drm.ezenglish.util.TTSManager
import java.util.*

class SpeakingViewModel(private val app: App) : AndroidViewModel(app), RecognitionListener {

    val speech: LiveData<Speech>
        get() = _speech
    private val _speech = MutableLiveData<Speech>()

    val permissionToRecordAudio = MutableLiveData(checkAudioRecordingPermission(context = app))

    private val isListening = _speech.value?.isListening ?: false

    private val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    private val speechRecognizer = createSpeechRecognizer(app).apply {
        setRecognitionListener(this@SpeakingViewModel)
    }

    private val ttsManager = TTSManager(app).apply {
        init()
    }

    fun recordSpeech() {
        if (!isListening) {
            startListening()
        }
    }

    fun speakText() {
        _speech.value?.text?.let { ttsManager.speak(it, true) }
    }

    fun cleanUp() = ttsManager.shutDown()

    fun getTextFromFirebase(dbReference: DatabaseReference) {
        dbReference.child("speech").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val text = snapshot.children.toList().random().getValue(String::class.java)

                    if (text != null) {
                        _speech.value = Speech(text)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun startListening() {
        speechRecognizer.startListening(recognizerIntent)
        notifyListening(true)
    }

    private fun checkAudioRecordingPermission(context: App) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    private fun notifyListening(isRecording: Boolean) {
        _speech.value = _speech.value?.copy(isListening = isRecording)
    }

    private fun updateResults(speechBundle: Bundle?) {
        val userSaid = speechBundle?.getStringArrayList(RESULTS_RECOGNITION)
        _speech.value = _speech.value?.copy(userSaid = userSaid?.get(0) ?: "")
    }

    override fun onBeginningOfSpeech() {
        notifyListening(true)
        Toast.makeText(app, "Grabación iniciada", Toast.LENGTH_SHORT).show()
    }

    override fun onEndOfSpeech() {
        notifyListening(false)
        Toast.makeText(app, "Grabación finalizada", Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: Int) {
         val message = when (error) {
            ERROR_AUDIO -> "Error de audio"
            ERROR_CLIENT -> "Error del cliente"
            ERROR_INSUFFICIENT_PERMISSIONS -> "Error de permisos"
            ERROR_NETWORK -> "Error de red"
            ERROR_NETWORK_TIMEOUT -> "Error de tiempo agotado en la red"
            ERROR_NO_MATCH -> "Error de match"
            ERROR_RECOGNIZER_BUSY -> "Error de disponibilidad"
            ERROR_SERVER -> "Error de servidor"
            ERROR_SPEECH_TIMEOUT -> "Error de tiempo agotado"
            else -> "Error desconocido"
        }
        Toast.makeText(app, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResults(results: Bundle?) {
        updateResults(results)
    }

    override fun onPartialResults(partialResults: Bundle?) {
        updateResults(partialResults)
    }

    override fun onReadyForSpeech(params: Bundle?) { }

    override fun onRmsChanged(rmsdB: Float) { }

    override fun onBufferReceived(buffer: ByteArray?) { }

    override fun onEvent(eventType: Int, params: Bundle?) { }
}