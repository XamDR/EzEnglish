package drm.ezenglish.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import java.lang.StringBuilder
import java.util.*

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