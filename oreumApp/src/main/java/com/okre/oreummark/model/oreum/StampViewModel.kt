package com.okre.oreummark.model.oreum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StampViewModel : ViewModel() {
    private var _stampListMutableLiveData = MutableLiveData<MutableList<OreumWholeStamp>>()
    val stampListLiveData : LiveData<MutableList<OreumWholeStamp>>
        get() = _stampListMutableLiveData

    fun changeStampListLiveDate(stamp: MutableList<OreumWholeStamp>) {
        _stampListMutableLiveData.postValue(stamp)
    }
}