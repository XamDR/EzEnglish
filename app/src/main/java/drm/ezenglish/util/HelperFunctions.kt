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

fun formatMillisecondsToString(milliseconds: Int): String {
    val buffer = StringBuffer()

    val minutes = (milliseconds % (1000 * 60 * 60) / (1000 * 60))
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000)

    buffer.append(String.format("%01d", minutes))
          .append(":")
          .append(String.format("%02d", seconds))

    return buffer.toString()
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

@BindingAdapter("selectedAnswer")
fun setSelectedAnswer(view: View, answer: MutableLiveData<String>?) {
    view.context.lifecycleOwner().let { parentActivity ->
        parentActivity?.let { lifeCycleOwner ->
            answer?.observe(lifeCycleOwner, { value ->
                view.findViewWithTag<RadioButton>(value.toString())
                    ?.also {
                        if (!it.isChecked) {
                            it.isChecked = true
                        }
                    }
                }
            )
        }
        (view as RadioGroup).setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedAnswer = radioGroup.findViewById<RadioButton>(checkedId).tag as String

            if(answer != null && answer.value != selectedAnswer) {
                answer.value = selectedAnswer
            }
        }
    }
}

fun Context.lifecycleOwner(): LifecycleOwner? {
    var currentContext = this
    var maxDepth = 20

    while (maxDepth-- > 0 && currentContext !is LifecycleOwner) {
        currentContext = (currentContext as ContextWrapper).baseContext
    }
    return if (currentContext is LifecycleOwner) currentContext else null
}
