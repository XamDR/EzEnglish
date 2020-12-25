package drm.ezenglish.viewmodels

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import drm.ezenglish.App
import drm.ezenglish.entities.Question
import drm.ezenglish.util.getPathFromUrl
import java.util.*

class ListeningViewModel(private val app: App, storage: FirebaseStorage, dbReference: DatabaseReference)
    : AndroidViewModel(app) {

    private val player = MediaPlayer()
    private lateinit var timer: Timer

    init {
        player.setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
        fetchAudioUrlromFirebase(storage, dbReference)
    }

    val isPlaying: LiveData<Boolean>
        get() = _isPlaying
    private val _isPlaying = MutableLiveData(false)

    val duration: LiveData<Int>
        get() = _duration
    private val _duration = MutableLiveData(0)

    val currentPosition = MutableLiveData(0)

    val questions = MutableLiveData(emptyList<Question>())

    fun toggleAudio() {
        if (_isPlaying.value!!) {
            pauseAudio()
        }
        else {
            playAudio()
        }
    }

    fun onProgressChanged() {
        currentPosition.value?.let { player.seekTo(it) }
    }

    fun cleanUp () = player.release()

    private fun fetchAudioUrlromFirebase(storage: FirebaseStorage, dbReference: DatabaseReference) {
        val audiosRef = storage.getReference("audios")
        audiosRef.listAll().addOnSuccessListener {
            it.items.random().downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                prepareMediaPlayer(url)
                fetchQuestionsFromFirebase(dbReference, url.getPathFromUrl())
            }
        }
    }

    private fun fetchQuestionsFromFirebase(dbReference: DatabaseReference, path: String) {
        val repositoryQuestions = mutableListOf<Question>()
        dbReference.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { c -> c.getValue<Question>()?.let { repositoryQuestions.add(it) } }
                    questions.value = repositoryQuestions
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun playAudio() {
        player.start()
        _isPlaying.value = player.isPlaying
        _duration.value = player.duration

        player.setOnCompletionListener {
            _isPlaying.value = it.isPlaying
            timer.cancel()
            timer.purge()
            Toast.makeText(app, "Audio finalizado", Toast.LENGTH_SHORT).show()
        }

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                currentPosition.postValue(player.currentPosition)
            }
        }, 0L, 1000L)

    }

    private fun pauseAudio() {
        if (player.isPlaying) {
            player.pause()
            _isPlaying.value = player.isPlaying
            timer.cancel()
            timer.purge()
        }
    }

    private fun prepareMediaPlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
    }
}