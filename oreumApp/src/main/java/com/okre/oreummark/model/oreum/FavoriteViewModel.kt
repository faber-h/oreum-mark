package com.okre.oreummark.model.oreum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteViewModel : ViewModel() {
    private var _favoriteListMutableLiveData = MutableLiveData<MutableList<OreumWholeFavorite>>()
    val favoriteListLiveData : LiveData<MutableList<OreumWholeFavorite>>
        get() = _favoriteListMutableLiveData

    fun changeFavoriteListLiveDate(favorite: MutableList<OreumWholeFavorite>) {
        _favoriteListMutableLiveData.postValue(favorite)
    }
}