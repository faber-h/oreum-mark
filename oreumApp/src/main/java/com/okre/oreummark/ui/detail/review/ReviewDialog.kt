package com.okre.oreummark.ui.detail.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.databinding.DialogReviewBinding
import java.util.concurrent.TimeUnit

class ReviewDialog : DialogFragment() {
    companion object {
        fun newInstance() = ReviewDialog()
    }

    private lateinit var binding: DialogReviewBinding
    private lateinit var nickname: String
    private lateinit var oreumName: String
    private var oreumIdx: Int = FALSE_NUMBER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        nickname = bundle!!.getString(OREUM_NICKNAME, FALSE_STRING)
        oreumName = bundle.getString(OREUM_NAME, FALSE_STRING)
        oreumIdx = bundle.getInt(OREUM_IDX, FALSE_NUMBER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogReviewNickname.text = getString(R.string.dialog_review_nickname, nickname)
        binding.dialogReviewEdt.hint = getString(R.string.dialog_review_hint, oreumName)
        binding.dialogReviewBtn.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                val userReview = binding.dialogReviewEdt.text.toString()
                val time = System.currentTimeMillis()
                saveReview(oreumIdx, nickname, userReview, time)
                dismiss()
            }
    }
}