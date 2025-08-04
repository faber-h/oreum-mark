package com.okre.oreummark.model.favorite

data class MyFavoriteItem(
    val userId: Int = -1,
    val oreumIdx: Int = -1,
    val favoriteBoolean: Boolean = false
)
