package com.okre.oreummark.ui.main.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.common.FALSE_NUMBER
import com.okre.oreummark.common.OREUM_USER_ID
import com.okre.oreummark.common.OreumApplication.Companion.getLoginSP
import com.okre.oreummark.common.OreumApplication.Companion.getNickname
import com.okre.oreummark.common.OreumApplication.Companion.getloginSPEditor
import com.okre.oreummark.common.OreumApplication.Companion.setUserId
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.databinding.ActivitySettingBinding
import com.okre.oreummark.ui.login.LoginActivity
import java.util.concurrent.TimeUnit

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // 닉네임 세팅
        val nickname = getNickname()
        binding.settingNickname.text = nickname

        // 로그아웃 버튼 클릭
        binding.settingLogout.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                logoutBtnClick()
            }

        // 회원탈퇴 버튼 클릭
        binding.settingWithdrawal.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, WithdrawalActivity::class.java))
            }

        // back 버튼
        binding.settingBack.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }
    }

    // 로그아웃 버튼 클릭
    private fun logoutBtnClick() {
        // 내부 데이터 리셋
        getloginSPEditor().putInt(OREUM_USER_ID, FALSE_NUMBER).commit()
        setUserId(getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER))

        // 로그인 페이지 이동
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}