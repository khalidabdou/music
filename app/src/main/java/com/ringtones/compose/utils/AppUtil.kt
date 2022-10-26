package com.ringtones.compose.utils

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.icu.text.Collator
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ringtones.compose.R
import com.ringtones.compose.foundation.extension.deviceLocale


object AppUtil {

    val IS_RINGTONE = false

    val collator: Collator = Collator.getInstance(deviceLocale).apply {
        strength = Collator.PRIMARY
    }

    fun Any?.toast(context: Context, length: Int = Toast.LENGTH_SHORT) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, this.toString(), length).show()
        }
    }

    @SuppressLint("ComposableNaming")
    @Composable
    fun Any?.toast(length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(LocalContext.current, this.toString(), length).show()
    }

    fun openStore(context: Context) {
        val appPackageName: String =
            context.packageName // getPackageName() from Context or Activity object

        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    fun privacy(context: Context) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.privacy_url)))
        context.startActivity(browserIntent)
    }


}