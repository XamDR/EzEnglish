package drm.ezenglish.viewmodels

import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import drm.ezenglish.App
import drm.ezenglish.entities.Question

class ListeningQuestionViewModel(private val app: App) : AndroidViewModel(app) {

    val question = MutableLiveData<Question>()

    fun onSelectedAnswer(radioGroup: RadioGroup, id: Int) {
        val selectedAnswer = radioGroup.findViewById<RadioButton>(id).text as String
        val answer = radioGroup.findViewById<RadioButton>(id).tag as String

        if (selectedAnswer == answer) {
            Toast.makeText(app, "¡Respuesta correcta!", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(app, "¡Respuesta incorrecta!", Toast.LENGTH_SHORT).show()
        }
    }
}