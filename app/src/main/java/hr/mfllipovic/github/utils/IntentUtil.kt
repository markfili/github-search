package hr.mfllipovic.github.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun launchInBrowser(context: Context, url: String) {
    val webPage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webPage)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}