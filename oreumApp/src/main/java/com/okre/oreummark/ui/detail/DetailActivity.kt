package com.okre.oreummark.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.common.OreumApplication.Companion.favoriteRef
import com.okre.oreummark.common.OreumApplication.Companion.getNickname
import com.okre.oreummark.common.OreumApplication.Companion.getOreumItem
import com.okre.oreummark.common.OreumApplication.Companion.getUserId
import com.okre.oreummark.common.OreumApplication.Companion.reviewRef
import com.okre.oreummark.common.OreumApplication.Companion.stampRef
import com.okre.oreummark.databinding.ActivityDetailBinding
import com.okre.oreummark.model.OreumItem
import com.okre.oreummark.model.review.ReviewItem
import com.okre.oreummark.ui.detail.favorite.saveFavorite
import com.okre.oreummark.ui.detail.review.DetailReviewRVAdapter
import com.okre.oreummark.ui.detail.review.ReviewDialog
import com.okre.oreummark.ui.detail.stamp.LocationSetting
import com.okre.oreummark.ui.detail.stamp.StampBooleanDialog
import com.okre.oreummark.ui.detail.stamp.saveStamp
import java.util.concurrent.TimeUnit

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var oreumDetailList = mutableListOf<OreumItem>()
    private var idx: Int = FALSE_NUMBER
    private var userId: String = FALSE_STRING
    private var userNickname: String = FALSE_STRING
    private var stampBoolean = true
    private var reviewList = mutableListOf<ReviewItem>()
    private var oreumName: String = FALSE_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        bottomPadding(binding.layout)

        idx = intent.getIntExtra(INTENT_PUT_IDX, FALSE_NUMBER)
        userId = getUserId().toString()
        userNickname = getNickname()

        // 상태 바 투명으로 설정(전체화면)
        setStatusBarTransparent()
        binding.detailToolbar.setPadding(
            0,
            baseContext.statusBarHeight(),
            0,
            0
        )

        // 오름 상세정보 설정
        oreumDetailSetting()

        // 좋아요 버튼 설정
        favoriteSetting()

        // 스탬프 버튼 설정
        stampSetting()

        // 리뷰 설정
        reviewSetting()

        backBtn()
    }

    // 오름 상세정보 설정
    private fun oreumDetailSetting() {
        oreumDetailList = getOreumItem()
        with(binding) {
            Glide.with(baseContext)
                .load(oreumDetailList[idx].imgPath)
                .centerCrop()
                .into(detailImage)
            oreumName = oreumDetailList[idx].oreumName
            detailName.text = oreumName
            detailAddr.text = oreumDetailList[idx].oreumAddr
            detailExplain.text = oreumDetailList[idx].explain
        }
    }

    // 좋아요 버튼 설정
    private fun favoriteSetting() {
        var favoriteBoolean = true
        favoriteRef.child(userId).child(idx.toString()).get().addOnSuccessListener {
            favoriteBoolean = it.value != null
            binding.detailFavorite.isChecked = favoriteBoolean
        }

        // 좋아요 버튼 클릭
        binding.detailFavorite.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                favoriteBoolean = !favoriteBoolean
                binding.detailFavorite.isChecked = favoriteBoolean
                saveFavorite(favoriteBoolean, idx, this)
            }
    }

    // 스탬프 버튼 설정
    private fun stampSetting() {
        stampRef.child(userId).child(idx.toString()).get().addOnSuccessListener {
            stampBoolean = it.value != null
            if (stampBoolean) {
                binding.detailStamp.text = getString(R.string.review_write)
                binding.detailStampTrueImage.visibility = View.VISIBLE
            } else {
                binding.detailStamp.text = getString(R.string.detail_stamp)
            }
        }

        // 스탬프 버튼 클릭
        binding.detailStamp.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                if (binding.detailStamp.text == getString(R.string.detail_stamp)) {
                    with(Intent(this, LocationSetting::class.java)) {
                        putExtra(OREUM_LATITUDE, oreumDetailList[idx].oreumLatitude)
                        putExtra(OREUM_LONGITUDE, oreumDetailList[idx].oreumLongitude)
                        putExtra(OREUM_NAME, oreumName)
                        registerForResult.launch(this)
                    }
                } else {
                    val reviewDialog = ReviewDialog.newInstance()
                    val bundle = Bundle()
                    bundle.putString(OREUM_NICKNAME, userNickname)
                    bundle.putInt(OREUM_IDX, idx)
                    bundle.putString(OREUM_NAME, oreumName)
                    reviewDialog.arguments = bundle
                    reviewDialog.show(supportFragmentManager, OREUM_REVIEW)
                }
            }
    }

    // 스탬프 버튼 LocationSetting intent 결과
    private val registerForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                stampBoolean = result.data!!.getBooleanExtra(STAMP_BOOLEAN, false)
                //stampBoolean = Random(System.currentTimeMillis()).nextBoolean() // 테스트 용
                showOreumStamp(stampBoolean)
            }
        }

    // 스탬프 찍기 성공 시 축하 다이알로그 & 스탬프 정보 저장
    private fun showOreumStamp(stampBoolean: Boolean) {
        val stampDialog = StampBooleanDialog.newInstance()
        val bundle = Bundle()
        bundle.putString(OREUM_NAME, oreumName)
        stampDialog.arguments = bundle
        if (stampBoolean) {
            saveStamp(stampBoolean, idx, oreumName, this) // 스탬프 정보 저장
            stampDialog.show(supportFragmentManager, stampBoolean.toString())
            binding.detailStamp.text = getString(R.string.review_write)
            binding.detailStampTrueImage.visibility = View.VISIBLE
        } else {
            stampDialog.show(supportFragmentManager, stampBoolean.toString())
        }
    }

    private fun reviewSetting() {
        reviewRef.child(idx.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    binding.detailReviewLayout.visibility = View.GONE
                } else {
                    binding.detailReviewLayout.visibility = View.VISIBLE
                    reviewList.clear()
                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.getValue(ReviewItem::class.java)?.let {
                            reviewList.add(it)
                        }
                    }
                    binding.detailReviewRv.adapter = DetailReviewRVAdapter(reviewList, this@DetailActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                logMessage(error.message)
            }
        })

        reviewRef.child(idx.toString()).child(userId).get().addOnCompleteListener {
            val myReview = it.result.value
            if (myReview != null) {
                binding.detailStamp.text = getString(R.string.stamp_success, oreumName)
                binding.detailStamp.isEnabled = false
            }
        }
    }

    private fun backBtn() {
        binding.detailBtnBack.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 전체화면 취소
        setStatusBarOrigin()
    }
}