package com.okre.oreummark.common

import android.view.View
import android.view.WindowInsets
import androidx.core.view.updatePadding

fun bottomPadding(view: View) {
    view.doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(
            bottom = padding.bottom + insets.systemWindowInsetBottom
        )
    }
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        insets
    }
    requestApplyInsetsWhenAttached()
}

data class InitialPadding(
    val left: Int, val top: Int,
    val right: Int, val bottom: Int
)

fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}