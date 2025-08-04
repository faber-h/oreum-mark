package com.okre.oreummark.model.review

data class ReviewItem(
    val oreumIdx: Int = -1,
    val userId: Int = -1,
    val userNickname: String = "",
    val userReview: String = "",
    val userTime: Long = 0,
    val reviewLikeNum: Int = 0
)
