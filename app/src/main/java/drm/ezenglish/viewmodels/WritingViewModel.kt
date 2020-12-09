package drm.ezenglish.viewmodels

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import drm.ezenglish.entities.Quiz
import drm.ezenglish.entities.SelectionRange
import drm.ezenglish.util.template

class WritingViewModel {

    private val quiz = MutableLiveData<Quiz>()

    fun getSentenceFromFirebase(dbReference: DatabaseReference, callback: (Quiz) -> Unit) {
        dbReference.child("writing").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val randomChild = snapshot.children.toList().random()
                    val sentence = randomChild.child("content").getValue(String::class.java)
                    val genericTypeIndicator = object : GenericTypeIndicator<ArrayList<Int>>() {}
                    val ranges = randomChild.children.filter { c -> c.key != "content" }
                        .map { c -> c.getValue(genericTypeIndicator) as ArrayList<Int> }.map { a -> SelectionRange(a[0], a[1]) }

                    if (sentence != null) {
                        quiz.value = Quiz(sentence, ranges)
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
            builder.removeRange(quiz.ranges[i].start, quiz.ranges[i].start + quiz.ranges[i].length)
            val length = quiz.ranges[i].length
            val replacement = """<input id=""q${i}""
                type =""text"" class = ""editable""
                maxlength=""$length"" 
                style =""width: ${ length * 1.162 } ch;""/>
            """.trimIndent()
            builder.insert(quiz.ranges[i].start, replacement)
        }
        return String.format(template, builder)
    }
}