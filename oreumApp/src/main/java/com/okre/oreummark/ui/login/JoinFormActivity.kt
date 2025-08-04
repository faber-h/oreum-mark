package com.okre.oreummark.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.okre.oreummark.R
import com.okre.oreummark.common.FALSE_STRING
import com.okre.oreummark.common.JOIN_SUCCESS_DIALOG_TAG
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.common.toastMessage
import com.okre.oreummark.databinding.ActivityJoinFormBinding
import com.okre.oreummark.model.login.JoinViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class JoinFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinFormBinding
    private lateinit var joinViewModel: JoinViewModel
    private var i = 0
    private var name = FALSE_STRING
    private var email = FALSE_STRING
    private var password = FALSE_STRING
    private var passwordCheck = FALSE_STRING
    private var nickname = FALSE_STRING
    private lateinit var termBottomSheetDialog : BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinFormBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        joinViewModel = ViewModelProvider(this).get(JoinViewModel::class.java)

        // 이름 입력
        nameEdt()

        // 이메일 입력
        emailEdt()

        // 비밀번호 입력
        passwordEdt()

        // 비밀번호 확인
        passwordCheckEdt()

        // 닉네임 입력
        nicknameEdt()

        binding.joinBtnNext.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                joinBtnNextClick(i)
            }

        binding.joinFormBack.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }
    }

    // 이름 입력
    private fun nameEdt() {
        binding.joinFormEdtName.textChanges()
            .debounce(700, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (it.isEmpty() || Pattern.matches("^[가-힣a-zA-Z]{2,}$", it)) {
                    joinViewModel.changeNameLiveDate(it.toString())
                    runOnUiThread {
                        binding.joinFormLayoutName.error = null
                        binding.joinBtnNext.isEnabled = it.isNotEmpty()
                    }
                } else {
                    runOnUiThread {
                        binding.joinFormLayoutName.error = getString(R.string.recheck_name)
                        binding.joinBtnNext.isEnabled = false
                    }
                }
            }

        joinViewModel.nameLiveData.observe(this) {
            name = it
        }
    }

    // 이메일 입력
    private fun emailEdt() {
        binding.joinFormEdtEmail.textChanges()
            .debounce(700, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (it.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    joinViewModel.changeEmailLiveDate(it.toString())
                    runOnUiThread {
                        binding.joinFormLayoutEmail.error = null
                        binding.joinBtnNext.isEnabled = it.isNotEmpty()
                    }
                } else {
                    runOnUiThread {
                        binding.joinFormLayoutEmail.error = getString(R.string.recheck_email)
                        binding.joinBtnNext.isEnabled = false
                    }
                }
            }

        joinViewModel.emailLiveData.observe(this) {
            email = it
        }
    }

    // 비밀번호 입력
    private fun passwordEdt() {
        binding.joinFormEdtPassword.textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (it.isEmpty() || Pattern.matches(
                        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@!%*#?&]).{7,16}.\$",
                        it
                    )
                ) {
                    joinViewModel.changePasswordLiveDate(it.toString())
                    runOnUiThread {
                        binding.joinFormLayoutPassword.error = null
                        binding.joinBtnNext.isEnabled = it.isNotEmpty()
                    }
                } else {
                    runOnUiThread {
                        binding.joinFormLayoutPassword.error = getString(R.string.recheck_password)
                        binding.joinBtnNext.isEnabled = false
                    }
                }
            }

        joinViewModel.passwordLiveData.observe(this) {
            password = it
        }
    }

    // 비밀번호 확인
    private fun passwordCheckEdt() {
        binding.joinFormEdtPasswordCheck.textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (it.isEmpty()) {
                    runOnUiThread {
                        binding.joinFormLayoutPasswordCheck.error = null
                        binding.joinBtnNext.isEnabled = it.isNotEmpty()
                    }
                    return@subscribe
                }
                if (password == it.toString()) {
                    joinViewModel.changePasswordCheckLiveDate(it.toString())
                    runOnUiThread {
                        binding.joinFormLayoutPasswordCheck.error = null
                        binding.joinBtnNext.isEnabled = true
                    }
                } else {
                    runOnUiThread {
                        binding.joinFormLayoutPasswordCheck.error = getString(R.string.recheck_password_check)
                        binding.joinBtnNext.isEnabled = false
                    }
                }
            }

        joinViewModel.passwordCheckLiveData.observe(this) {
            passwordCheck = it
        }
    }

    // 닉네임 입력
    private fun nicknameEdt() {
        binding.joinFormEdtNickname.textChanges()
            .debounce(700, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (it.isEmpty() || Pattern.matches("^[가-힣a-zA-Z0-9]{1,10}$", it)) {
                    joinViewModel.changeNicknameLiveDate(it.toString())
                    runOnUiThread {
                        binding.joinFormLayoutNickname.error = null
                        binding.joinBtnNext.isEnabled = it.isNotEmpty()
                    }
                } else {
                    runOnUiThread {
                        binding.joinFormLayoutNickname.error = getString(R.string.recheck_nickname)
                        binding.joinBtnNext.isEnabled = false
                    }
                }
            }

        joinViewModel.nicknameLiveData.observe(this) {
            nickname = it
        }
    }

    private fun joinBtnNextClick(clickNum: Int) {
        with(binding) {
            when (clickNum) {
                0 -> {
                    joinFormTitleTop.text = getString(R.string.join_form_title_email_top)
                    joinFormTitleBottom.text = getString(R.string.join_form_title_email_bottom)
                    joinFormLayoutEmail.visibility = View.VISIBLE
                    joinBtnNext.isEnabled = false
                    joinFormEdtEmail.requestFocus()
                    i++
                }
                1 -> { // 이메일 입력 다음
                    joinFormTitleTop.text = getString(R.string.join_form_title_password_top)
                    joinFormTitleBottom.text = getString(R.string.join_form_title_password_bottom)
                    joinFormLayoutPassword.visibility = View.VISIBLE
                    joinBtnNext.isEnabled = false
                    joinFormEdtPassword.requestFocus()
                    i++
                }
                2 -> { // 비밀번호 입력 다음
                    joinFormTitleBottom.text =
                        getString(R.string.join_form_title_password_check_bottom)
                    joinFormLayoutPasswordCheck.visibility = View.VISIBLE
                    joinBtnNext.isEnabled = false
                    joinFormEdtPasswordCheck.requestFocus()
                    i++
                }
                3 -> { // 비밀번호 확인 다음
                    joinFormTitleTop.text = getString(R.string.join_form_title_nickname_top)
                    joinFormTitleBottom.text = getString(R.string.join_form_title_nickname_bottom)
                    joinFormLayoutNickname.visibility = View.VISIBLE
                    joinBtnNext.text = getString(R.string.join_form_btn_next_check)
                    joinBtnNext.isEnabled = false
                    joinFormEdtNickname.requestFocus()
                    i++
                }
                4 -> { // 닉네임 입력 다음
                    if (joinFormLayoutNickname.error != null
                        && joinFormLayoutPasswordCheck.error != null
                        && joinFormLayoutPassword.error != null
                        && joinFormLayoutEmail.error != null
                        && joinFormLayoutName.error != null
                    ) {
                        toastMessage(getString(R.string.recheck))
                    } else {
                        // 약관 동의 bottom sheet
                        showTermDialog()
                    }
                }
                else -> {}
            }
        }
    }

    private fun showTermDialog() {
        val termBottomSheetView = layoutInflater.inflate(R.layout.dialog_join_term, null)
        termBottomSheetDialog = BottomSheetDialog(this)
        termBottomSheetDialog.setContentView(termBottomSheetView)
        val termBtn = termBottomSheetView.findViewById<Button>(R.id.term_btn)

        // 약관 정보 webview
        termBottomSheetView.findViewById<ImageView>(R.id.term_arrow).clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, JoinTermContent::class.java))
            }

        // 약관 동의 체크
        termBottomSheetView.findViewById<CheckBox>(R.id.term_checkBox).setOnCheckedChangeListener { _, checkBoolean ->
            termBtn.isEnabled = checkBoolean
        }

        // 약관 동의 후 확인 버튼 클릭
        termBtn.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                joinSaveBtnClick(name, email, password, nickname)
                val joinSuccessDialog = JoinSuccessDialog.newInstance()
                joinSuccessDialog.show(supportFragmentManager, JOIN_SUCCESS_DIALOG_TAG)
            }

        termBottomSheetDialog.show()
    }
}