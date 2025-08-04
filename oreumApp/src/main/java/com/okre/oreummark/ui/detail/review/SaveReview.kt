package com.okre.oreummark.ui.detail.review

import com.okre.oreummark.common.OreumApplication.Companion.getUserId
import com.okre.oreummark.common.OreumApplication.Companion.reviewRef
import com.okre.oreummark.model.review.ReviewItem

fun saveReview(oreumIdx: Int, userNickname: String, userReview: String, userTime: Long) {
    val userId = getUserId()
    val reviewItem = ReviewItem(oreumIdx, userId, userNickname, userReview, userTime, 0)
    reviewRef.child(oreumIdx.toString()).child(userId.toString()).setValue(reviewItem)
}