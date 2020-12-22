package drm.ezenglish.util

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import drm.ezenglish.App
import drm.ezenglish.adapters.QuestionAdapter
import drm.ezenglish.entities.Question

fun formatMillisecondsToString(milliseconds: Int): String {
    val buffer = StringBuffer()

    val minutes = (milliseconds % (1000 * 60 * 60) / (1000 * 60))
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000)

    buffer.append(String.format("%01d", minutes))
          .append(":")
          .append(String.format("%02d", seconds))

    return buffer.toString()
}

fun String.formatTopic(): String {
    val builder = StringBuilder(this)
    builder.setCharAt(0, Character.toUpperCase(this[0]))
    val index = this.indexOfFirst { c -> c.isUpperCase() }
    builder.insert(index, " ")
    return builder.toString()
}

@SuppressLint("SetTextI18n")
object Converter {
    @JvmStatic fun setCurrentPosition(currentPosition: Int): String {
        return formatMillisecondsToString(currentPosition)
    }

    @JvmStatic fun setDuration(duration: Int): String {
        return " / ${formatMillisecondsToString(duration)}"
    }
}

@BindingAdapter("dividerOrientation")
fun setDivider(recyclerView: RecyclerView, orientation: Int) {
    recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, orientation))
}

@BindingAdapter("questions")
fun setAdapter(recyclerView: RecyclerView, questions: List<Question>?) {
    if (questions != null) {
        recyclerView.adapter = QuestionAdapter(recyclerView.context.applicationContext as App, questions)
    }
    else {
        recyclerView.adapter = null
    }
}