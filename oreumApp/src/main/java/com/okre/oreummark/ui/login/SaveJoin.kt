package com.okre.oreummark.ui.login

import com.okre.oreummark.common.*
import com.okre.oreummark.common.OreumApplication.Companion.getLoginSP
import com.okre.oreummark.common.OreumApplication.Companion.getloginSPEditor
import com.okre.oreummark.common.OreumApplication.Companion.joinRef
import com.okre.oreummark.common.OreumApplication.Companion.setUserId
import com.okre.oreummark.model.login.JoinItem

fun joinSaveBtnClick(name: String, email: String, password: String, nickname: String) {
    var key: Int
    /*joinRef.child(OREUM_KEY).setValue(key) // firebase 첫 설정 시 사용*/
    joinRef.child(OREUM_KEY).get().addOnCompleteListener {
        // firebase key 가져오기
        key = it.result.value.toString().toInt()
        key++
        joinRef.child(OREUM_KEY).setValue(key)
        // userId 고유 key값 설정하여 저장
        val joinItem = JoinItem(key, name, email, password, nickname, 0)
        joinRef.child(OREUM_USER).child(key.toString()).setValue(joinItem)
        // 회원가입 -> 자동로그인
        getloginSPEditor().putInt(OREUM_USER_ID, key).commit()
        setUserId(getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER))
        findNickname()
    }.addOnFailureListener {
        logMessage("$it")
    }
}

