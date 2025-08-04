package com.okre.oreummark.model.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JoinViewModel : ViewModel() {
    // 이름
    private var _nameMutableLiveData = MutableLiveData("")
    val nameLiveData : LiveData<String>
        get() = _nameMutableLiveData

    fun changeNameLiveDate(name: String) {
        _nameMutableLiveData.postValue(name)
    }

    // 이메일
    private var _emailMutableLiveData = MutableLiveData("")
    val emailLiveData : LiveData<String>
        get() = _emailMutableLiveData

    fun changeEmailLiveDate(email: String) {
        _emailMutableLiveData.postValue(email)
    }

    // 비밀번호
    private var _passwordMutableLiveData = MutableLiveData("")
    val passwordLiveData : LiveData<String>
        get() = _passwordMutableLiveData

    fun changePasswordLiveDate(password: String) {
        _passwordMutableLiveData.postValue(password)
    }

    // 비밀번호 확인
    private var _passwordCheckMutableLiveData = MutableLiveData("")
    val passwordCheckLiveData : LiveData<String>
        get() = _passwordCheckMutableLiveData

    fun changePasswordCheckLiveDate(passwordCheck: String) {
        _passwordCheckMutableLiveData.postValue(passwordCheck)
    }

    // 닉네임 입력
    private var _nicknameMutableLiveData = MutableLiveData("")
    val nicknameLiveData : LiveData<String>
        get() = _nicknameMutableLiveData

    fun changeNicknameLiveDate(nickname: String) {
        _nicknameMutableLiveData.postValue(nickname)
    }
}