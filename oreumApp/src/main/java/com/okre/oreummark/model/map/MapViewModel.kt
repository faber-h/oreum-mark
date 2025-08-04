package com.okre.oreummark.model.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    // 이름
    private var _oreumNameMutableLiveData = MutableLiveData("")
    val oreumNameLiveData : LiveData<String>
        get() = _oreumNameMutableLiveData

    fun changeOreumNameLiveDate(name: String) {
        _oreumNameMutableLiveData.postValue(name)
    }

    // 주소
    private var _oreumAddrMutableLiveData = MutableLiveData("")
    val oreumAddrLiveData : LiveData<String>
        get() = _oreumAddrMutableLiveData

    fun changeOreumAddrLiveDate(addr: String) {
        _oreumAddrMutableLiveData.postValue(addr)
    }

    // 좋아요
    private var _oreumFavoriteMutableLiveData = MutableLiveData(0)
    val oreumFavoriteLiveData : LiveData<Int>
        get() = _oreumFavoriteMutableLiveData

    fun changeOreumFavoriteLiveDate(favorite: Int) {
        _oreumFavoriteMutableLiveData.postValue(favorite)
    }

    // 스탬프
    private var _oreumStampMutableLiveData = MutableLiveData(0)
    val oreumStampLiveData : LiveData<Int>
        get() = _oreumStampMutableLiveData

    fun changeOreumStampLiveDate(stamp: Int) {
        _oreumStampMutableLiveData.postValue(stamp)
    }
}