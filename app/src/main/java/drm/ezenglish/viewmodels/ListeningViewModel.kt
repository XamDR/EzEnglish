package drm.ezenglish.viewmodels

import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import drm.ezenglish.App
import drm.ezenglish.entities.ListeningQuestion
import java.util.*

class ListeningViewModel(private val app: App, resourceId: Int) : AndroidViewModel(app) {

    private val player: MediaPlayer = MediaPlayer.create(app, resourceId)
    private lateinit var timer: Timer
//    private var dbReference: DatabaseReference
//    private lateinit var firebaseDatabase: FirebaseDatabase
//
//    init {
//        dbReference = FirebaseDatabase.getInstance().reference
//        initFirebase()
//    }
//
//    private fun initFirebase() {
//        FirebaseApp.initializeApp(app)
//        firebaseDatabase = FirebaseDatabase.getInstance()
//        dbReference = firebaseDatabase.reference
//    }

    val isPlaying: LiveData<Boolean>
        get() = _isPlaying
    private val _isPlaying = MutableLiveData(false)

    val duration: LiveData<Int>
        get() = _duration
    private val _duration = MutableLiveData(player.duration)

    val currentPosition = MutableLiveData(0)

    val questions = MutableLiveData(emptyList<ListeningQuestion>())

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

    fun getQuestionsFromFirebase(dbReference: DatabaseReference, path: String, callback: (List<ListeningQuestion>) -> Unit) {
        val questions = mutableListOf<ListeningQuestion>()
        dbReference.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val question = child.getValue(ListeningQuestion::class.java)

                        if (question != null) {
                            questions.add(question)
                        }
                    }
                    callback(questions)
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