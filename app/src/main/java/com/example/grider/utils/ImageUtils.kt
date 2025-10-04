package com.example.grider.utils
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build

fun getImageDimensions(context: Context, uri: Uri): Pair<Int, Int>? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val drawable = ImageDecoder.decodeDrawable(source)
            drawable.intrinsicWidth to drawable.intrinsicHeight
        } else {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }
            options.outWidth to options.outHeight
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}