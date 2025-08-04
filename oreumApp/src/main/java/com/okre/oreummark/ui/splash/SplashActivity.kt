package com.okre.oreummark.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.common.OreumApplication.Companion.getLoginSP
import com.okre.oreummark.databinding.ActivitySplashBinding
import com.okre.oreummark.ui.login.LoginActivity
import com.okre.oreummark.ui.main.MainActivity

/**
 * 스플래시 진입 시
 * 1.위치 정보 권한확인
 * 2.네트워크 체크
 * 3.로그인 체크
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var loginToken: Int = FALSE_NUMBER
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        loginToken = getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER)
    }

    override fun onResume() {
        super.onResume()
        // 1. 위치 정보 허용 확인
        if (!PermissionCheck.getInstance().isPermission) { // 허용X
            locationPermissionCheck()
        } else { // 허용O
            // 2. 네트워크 체크
            networkCheck()
        }
    }

    // 앱 접근 권한 activity 이동
    private fun locationPermissionCheck() {
        startActivity(Intent(this, PermissionActivity::class.java))
    }

    // 2. 네트워크 체크
    private fun networkCheck() {
        val status = OreumNetworkManager(applicationContext).checkNetworkState()
        if (status) {
            // 3. 로그인 확인
            loginCheck()
        } else {
            val dialog = NetworkDialog()
            dialog.show(supportFragmentManager, NETWORK_DIALOG_TAG)
            dialog.myDialogInterface = object : NetworkDialog.DialogListener {
                override fun onDialogPositiveClick() {
                    val againCheckStatus = OreumNetworkManager(applicationContext).checkNetworkState()
                    if (againCheckStatus) {
                        dialog.dismiss()
                        // 3. 로그인 확인
                        loginCheck()
                    } else {
                        toastMessage(getString(R.string.network_must))
                    }
                }
            }
        }
    }

    // 3. 로그인 확인
    private fun loginCheck() {
        if (loginToken == FALSE_NUMBER) { // 회원가입 또는 로그인
            handler.postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, SPLASH_DELAY_DURATION)
        } else { // 자동로그인
            handler.postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, SPLASH_DELAY_DURATION)
        }
    }
}