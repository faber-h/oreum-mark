package com.okre.oreummark.ui.detail.review

import com.okre.oreummark.common.FALSE_NUMBER
import com.okre.oreummark.common.OREUM_REVIEW_LIKE_NUM
import com.okre.oreummark.common.OREUM_USER_ID
import com.okre.oreummark.common.OreumApplication
import com.okre.oreummark.common.OreumApplication.Companion.myReviewRef
import com.okre.oreummark.common.OreumApplication.Companion.reviewRef

fun saveOtherReview(oreumIdx: Int, otherUserId: Int, reviewFavoriteBoolean: Boolean, reviewLikeNum: Int) {
    val myUserId = OreumApplication.getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER).toString()
    var reviewWholeLikeNum = reviewLikeNum
    if (reviewFavoriteBoolean) {
        myReviewRef.child(myUserId).child(oreumIdx.toString()).child(otherUserId.toString()).setValue(reviewFavoriteBoolean.toString())
        reviewWholeLikeNum++
    } else {
        myReviewRef.child(myUserId).child(oreumIdx.toString()).child(otherUserId.toString()).removeValue()
        reviewWholeLikeNum--
    }
    reviewRef.child(oreumIdx.toString()).child(otherUserId.toString()).child(OREUM_REVIEW_LIKE_NUM).setValue(reviewWholeLikeNum)
}