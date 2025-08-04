package com.okre.oreummark.model.login

data class JoinItem (
    val userId: Int = -1,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val nickname: String = "",
    val myStampNum: Int = 0
)