package com.adopet.app.utils

import android.content.res.Resources
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.applyDefaultSystemPadding(defaultPaddingDp: Int = 24) {
    val scale = Resources.getSystem().displayMetrics.density
    val basePaddingPx = (defaultPaddingDp * scale).toInt()

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            basePaddingPx + systemBars.left,
            basePaddingPx + systemBars.top,
            basePaddingPx + systemBars.right,
            basePaddingPx + systemBars.bottom
        )
        insets
    }

    ViewCompat.requestApplyInsets(this)
}