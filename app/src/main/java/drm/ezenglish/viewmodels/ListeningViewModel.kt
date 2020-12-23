package drm.ezenglish.viewmodels

import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import drm.ezenglish.App
import drm.ezenglish.entities.Question
import java.util.*

class ListeningViewModel(private val app: App, resourceId: Int) : AndroidViewModel(app) {

    private val player: MediaPlayer = MediaPlayer.create(app, resourceId)
    private lateinit var timer: Timer

    val isPlaying: LiveData<Boolean>
        get() = _isPlaying
    private val _isPlaying = MutableLiveData(false)

    val duration: LiveData<Int>
        get() = _duration
    private val _duration = MutableLiveData(player.duration)

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

    fun getQuestionsFromFirebase(dbReference: DatabaseReference, path: String) {
        val repositoryQuestions = mutableListOf<Question>()
        dbReference.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { c -> c.getValue(Question::class.java)?.let { repositoryQuestions.add(it) } }
                    questions.value = repositoryQuestions
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun playAudio() {
        player.start()
        _isPlaying.value = player.isPlaying

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
}