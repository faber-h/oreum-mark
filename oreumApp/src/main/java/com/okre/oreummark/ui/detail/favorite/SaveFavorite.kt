package com.okre.oreummark.ui.detail.favorite

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.okre.oreummark.common.FALSE_NUMBER
import com.okre.oreummark.common.OREUM_FAVORITE
import com.okre.oreummark.common.OREUM_USER_ID
import com.okre.oreummark.common.OreumApplication.Companion.favoriteRef
import com.okre.oreummark.common.OreumApplication.Companion.getLoginSP
import com.okre.oreummark.common.OreumApplication.Companion.getOreumFavorite
import com.okre.oreummark.common.OreumApplication.Companion.oreumRef
import com.okre.oreummark.common.OreumApplication.Companion.setOreumFavortie
import com.okre.oreummark.model.favorite.MyFavoriteItem
import com.okre.oreummark.model.oreum.FavoriteViewModel
import com.okre.oreummark.model.oreum.OreumWholeFavorite

fun saveFavorite(favoriteBoolean: Boolean, oreumIdx: Int, owner: ViewModelStoreOwner) {
    val userId = getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER).toString()
    var oreumFavorite: MutableList<OreumWholeFavorite>
    var favoriteNum: Int
    val favoriteViewModel : FavoriteViewModel = ViewModelProvider(owner).get(FavoriteViewModel::class.java)

    oreumRef.child(oreumIdx.toString()).child(OREUM_FAVORITE).get().addOnCompleteListener {
        favoriteNum = it.result.value.toString().toInt()
        oreumFavorite = getOreumFavorite()

        val favoriteItem = MyFavoriteItem(userId.toInt(), oreumIdx, favoriteBoolean)
        if (favoriteBoolean) {
            favoriteRef.child(userId).child(oreumIdx.toString()).setValue(favoriteItem) // my favorite 변경
            favoriteNum++ // 전체 favorite 변경
        } else {
            favoriteRef.child(userId).child(oreumIdx.toString()).removeValue() // my favorite 변경
            favoriteNum-- // 전체 favorite 변경
        }
        oreumRef.child(oreumIdx.toString()).child(OREUM_FAVORITE).setValue(favoriteNum) // firebase 해당 오름 favorite 숫자 변경
        oreumFavorite[oreumIdx].oreumFavorite = favoriteNum // Application 오름 list 변경
        setOreumFavortie(oreumFavorite) // Application 오름 list 변경 -2
        favoriteViewModel.changeFavoriteListLiveDate(oreumFavorite) // favorite viewModel 변경
    }
}