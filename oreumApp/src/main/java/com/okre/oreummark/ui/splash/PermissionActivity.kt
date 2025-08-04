package com.okre.oreummark.ui.splash

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.common.PermissionCheck
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.databinding.ActivityPermissionBinding
import java.util.concurrent.TimeUnit

/**
 * 위치 정보 권한확인 activity
 */
class PermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionBinding
    private lateinit var permissionCheck: PermissionCheck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        permissionCheck = PermissionCheck()

        binding.permissionBtn.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                if (!permissionCheck.checkSelfCurrentPermission(applicationContext)) { // 허용X
                    permissionCheck.requestAppPermissions(this)
                }
            }

        addOnBackPressedDispatcher {
            finishAffinity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionCheck.appPermissionResult(requestCode, grantResults)) {
            PermissionCheck.getInstance().isPermission = true
            finish()
        } else {
            finish()
        }
    }

    private fun AppCompatActivity.addOnBackPressedDispatcher(backPressed: () -> Unit) {
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressed.invoke() // 설정한 코드 실행
                }
            }
        )
    }
}