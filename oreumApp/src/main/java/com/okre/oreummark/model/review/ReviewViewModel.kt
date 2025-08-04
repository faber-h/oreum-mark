package com.okre.oreummark.model.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReviewViewModel : ViewModel() {
    private var _reviewListMutableLiveData = MutableLiveData<MutableList<ReviewItem>>()
    val reviewListLiveData : LiveData<MutableList<ReviewItem>>
        get() = _reviewListMutableLiveData

    fun changeReviewListLiveDate(review: MutableList<ReviewItem>) {
        _reviewListMutableLiveData.postValue(review)
    }
}