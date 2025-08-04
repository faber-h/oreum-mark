package com.okre.oreummark.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.core.view.WindowCompat

// 앱 전체 화면
fun Activity.setStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= 30) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    } else {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
}

// 상태창 높이 계산
@SuppressLint("InternalInsetResource", "DiscouragedApi")
fun Context.statusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

// 네비게이션 높이 계산
@SuppressLint("InternalInsetResource", "DiscouragedApi")
fun Context.navigationHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")

    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

// 전체화면 취소
fun Activity.setStatusBarOrigin() {
    if (Build.VERSION.SDK_INT >= 30) {    // API 30 에 적용
        WindowCompat.setDecorFitsSystemWindows(window, true)
    } else {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}