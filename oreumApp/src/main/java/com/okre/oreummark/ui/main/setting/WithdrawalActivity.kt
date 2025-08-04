package com.okre.oreummark.ui.main.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.common.FALSE_NUMBER
import com.okre.oreummark.common.OREUM_USER
import com.okre.oreummark.common.OREUM_USER_ID
import com.okre.oreummark.common.OreumApplication.Companion.getLoginSP
import com.okre.oreummark.common.OreumApplication.Companion.getloginSPEditor
import com.okre.oreummark.common.OreumApplication.Companion.joinRef
import com.okre.oreummark.common.OreumApplication.Companion.setUserId
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.databinding.ActivityWithdrawalBinding
import com.okre.oreummark.ui.login.LoginActivity
import java.util.concurrent.TimeUnit

class WithdrawalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWithdrawalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawalBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // 뒤로가기
        binding.withdrawalBack.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        // 소멸 동의 체크박스
        binding.withdrawalCheckBox.setOnCheckedChangeListener { _, checkBoolean ->
            binding.withdrawalBtn.isEnabled = checkBoolean
        }

        // 회원탈퇴 버튼
        binding.withdrawalBtn.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                withdrawalBtnClick()
            }
    }

    // 회원탈퇴 버튼 클릭
    private fun withdrawalBtnClick() {
        // firebase 리셋
        val userId = getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER)
        joinRef.child(OREUM_USER).child(userId.toString()).removeValue()

        // 내부 데이터 리셋
        getloginSPEditor().putInt(OREUM_USER_ID, FALSE_NUMBER).commit()
        setUserId(userId)

        // 로그인 페이지 이동
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}