package drm.ezenglish.viewmodels

import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import drm.ezenglish.App
import drm.ezenglish.entities.Quiz
import drm.ezenglish.util.template

class WritingViewModel(private val app: App) : AndroidViewModel(app) {

    private val quiz = MutableLiveData<Quiz>()
    val topics = MutableLiveData(emptyList<String?>())

    fun getTopicsFromFirebase(dbReference: DatabaseReference, callback: (List<String?>) -> Unit) {

        dbReference.child("writing").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val results = snapshot.children.map { c -> c.key }
                    callback(results)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    @Suppress("UNCHECKED_CAST")
    fun getSentenceFromFirebase(dbReference: DatabaseReference, path: String = "pastPerfect", callback: (Quiz) -> Unit) {
        dbReference.child("writing").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val topic = snapshot.child(path)
                    val randomChild = topic.children.toList().random()
                    val sentence = randomChild.child("content").getValue<String>()
                    val ranges = randomChild.children.filter { c -> c.key != "content" }
                        .map { c -> c.getValue<ArrayList<@JvmSuppressWildcards Int>>() }
                        .map { a -> Pair(a?.get(0), a?.get(1)) }

                    if (sentence != null) {
                        quiz.value = Quiz(sentence, ranges as List<Pair<Int, Int>>)
                    }
                    callback(quiz.value!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun render(quiz: Quiz): String {
        val builder = StringBuilder(quiz.content)

        for (i in quiz.ranges.size - 1 downTo 0) {
            builder.deleteRange(quiz.ranges[i].first, quiz.ranges[i].first + quiz.ranges[i].second)
            val length = quiz.ranges[i].second
            val replacement = """<input id=""q$i""
                type="text" class= "editable"
                maxlength="$length" 
                style ="width: ${ length * 1.162 } ch;"/>
            """.trimIndent()
            builder.insert(quiz.ranges[i].first, replacement)
        }
        return String.format(template, builder)
    }

    fun showAnswers() = Toast.makeText(app, "Falta implementar!!!", Toast.LENGTH_SHORT).show()
}