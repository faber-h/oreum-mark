package com.okre.oreummark.ui.detail.review

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.common.OreumApplication.Companion.myReviewRef
import com.okre.oreummark.databinding.RvItemDetailReviewBinding
import com.okre.oreummark.model.review.ReviewItem
import java.util.concurrent.TimeUnit

class DetailReviewRVAdapter(private val itemList: MutableList<ReviewItem>, private val owner: Activity)
    : RecyclerView.Adapter<DetailReviewRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvItemDetailReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemDetailReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        with(holder.binding) {
            val myUserId = OreumApplication.getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER).toString()
            var reviewFavoriteBoolean: Boolean
            myReviewRef.child(myUserId).child(item.oreumIdx.toString()).child(item.userId.toString()).get().addOnCompleteListener {
                reviewFavoriteBoolean = if (it.result.value != null) {
                    reviewLikeIcon.setImageResource(R.drawable.ic_favorite_check)
                    true
                } else {
                    reviewLikeIcon.setImageResource(R.drawable.ic_favorite)
                    false
                }
                reviewLikeLayout.clicks()
                    .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
                    .subscribe {
                        saveOtherReview(item.oreumIdx, item.userId, !reviewFavoriteBoolean, item.reviewLikeNum)
                    }
            }

            reviewUserName.text = item.userNickname
            reviewUserReview.text = item.userReview
            reviewUserTime.text = changeFormatTime(item.userTime)
            reviewLikeText.text = item.reviewLikeNum.toString()
        }
    }

    override fun getItemCount(): Int = itemList.size
}