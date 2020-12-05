package drm.ezenglish.util

import android.annotation.SuppressLint

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