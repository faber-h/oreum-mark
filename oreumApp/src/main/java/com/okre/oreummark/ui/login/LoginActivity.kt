package com.okre.oreummark.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.common.OreumApplication.Companion.getLoginSP
import com.okre.oreummark.common.OreumApplication.Companion.getloginSPEditor
import com.okre.oreummark.common.OreumApplication.Companion.joinRef
import com.okre.oreummark.common.OreumApplication.Companion.setUserId
import com.okre.oreummark.databinding.ActivityLoginBinding
import com.okre.oreummark.model.login.JoinItem
import com.okre.oreummark.ui.main.MainActivity
import java.util.concurrent.TimeUnit

/**
 * 로그인 페이지
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var emailEdt = FALSE_STRING
    var passwordEdt = FALSE_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        with(binding) {
            // 회원가입 버튼 클릭
            loginBtnJoin.clicks()
                .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    startActivity(Intent(this@LoginActivity, JoinFormActivity::class.java))
                    finish()
                }

            // 로그인 버튼 클릭
            loginBtnLogin.clicks()
                .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    loginBtnClick()
                }

            // 뒤로가기 버튼 클릭
            loginBtnBack.clicks()
                .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    finish()
                }
        }
    }

    private fun loginBtnClick() {
        emailEdt = binding.loginEdtEmail.text.toString()
        passwordEdt = binding.loginEdtPassword.text.toString()

        if (emailEdt.isEmpty()) { // 이메일 입력칸 비어있을 경우
            binding.loginLayoutEmail.error = getString(R.string.check_login_email)
            binding.loginLayoutPassword.error = null
        } else if (passwordEdt.isEmpty()) { // 비밀번호 입력칸 비어있을 경우
            binding.loginLayoutPassword.error = getString(R.string.check_login_password)
            binding.loginLayoutEmail.error = null
        } else { // 이메일, 비밀번호칸 비어있지 않을 경우
            binding.loginLayoutEmail.error = null
            binding.loginLayoutPassword.error = null

            // 회원이 맞는지 확인
            joinRef.child(OREUM_USER).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.getValue(JoinItem::class.java)?.let {
                            if (it.email == emailEdt) {
                                if (it.password == passwordEdt) {
                                    getloginSPEditor().putInt(OREUM_USER_ID, it.userId).commit()
                                    setUserId(getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER))
                                    findNickname()
                                    binding.loginError.visibility = View.GONE
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    finish()
                                    return // for문 종료
                                }
                            } else {
                                binding.loginError.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    logMessage(error.message)
                }
            })
        }
    }
}