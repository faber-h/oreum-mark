package com.okre.oreummark.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.okre.oreummark.common.OreumApplication.Companion.getAppInstance

/**
 * 위치 정보 권한확인
 */
class PermissionCheck {
    companion object {
        private lateinit var permissionManager: PermissionCheck
        private lateinit var locationSp: SharedPreferences
        private lateinit var locationSpEditor: SharedPreferences.Editor

        fun getInstance() : PermissionCheck {
            if (this::permissionManager.isInitialized) {
                return permissionManager
            } else {
                locationSp = getAppInstance().getSharedPreferences(LOCATION, Application.MODE_PRIVATE)
                locationSpEditor = locationSp.edit()
                permissionManager = PermissionCheck()
            }
            return permissionManager
        }
    }

    var isPermission : Boolean
        get() = locationSp.getBoolean(LOCATION, false)
        set(permissionCheck) {
            with(locationSpEditor){
                putBoolean(LOCATION, permissionCheck).apply()
            }
        }

    // 권한 목록
    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val permissionCheckList = mutableListOf<String>()

    fun checkSelfCurrentPermission(context: Context) : Boolean {
        for (permission in permissions) {
            val result = ContextCompat.checkSelfPermission(context, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionCheckList.add(permission) // 확인할 권한들 permissionCheckList에 담기
            }
        }
        if (permissionCheckList.isNotEmpty()) {
            return false
        }
        return true
    }

    fun requestAppPermissions(owner: Activity) {
        logMessage(permissionCheckList.toString())
        ActivityCompat.requestPermissions(owner, permissionCheckList.toTypedArray(), PERMISSION_CODE)
    }

    // 위치 정보 권한확인 activity로 boolean return
    fun appPermissionResult(requestCode: Int, grantResults: IntArray): Boolean {
        if (requestCode == PERMISSION_CODE && grantResults.isNotEmpty()) {
            for (result in grantResults) {
                if (result == -1) {
                    return false
                }
            }
        }
        return true
    }
}