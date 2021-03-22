package hr.mfllipovic.github.utils

import android.content.Context
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun toRelativeTimeSpan(context: Context, rawDateString: String): String {
    val fromFormatter =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val date = fromFormatter.parse(rawDateString)
    if (date != null) {
        return DateUtils.getRelativeTimeSpanString(
            context,
            date.time,
            true
        ).toString()
    }
    return ""
}