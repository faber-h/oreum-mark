package com.okre.oreummark.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.okre.oreummark.model.OreumItem
import com.okre.oreummark.model.oreum.OreumWholeFavorite
import com.okre.oreummark.model.oreum.OreumWholeStamp

/**
 * 앱 런처 아이콘을 터치하면 처음 실행되는 코드
 * App Scope 모든 코틀린 클래스,파일에서 호출할 수 있는 코드
 */
class OreumApplication : Application() {

    companion object {
        private lateinit var appInstance: OreumApplication
        fun getAppInstance() = appInstance

        private var oreumItem = mutableListOf<OreumItem>()
        fun setOreumItem(items: MutableList<OreumItem>){
            oreumItem = items
        }
        fun getOreumItem() = oreumItem

        private var oreumFavorite = mutableListOf<OreumWholeFavorite>()
        fun setOreumFavortie (favorite : MutableList<OreumWholeFavorite>){
            oreumFavorite = favorite
        }
        fun getOreumFavorite() = oreumFavorite

        private var oreumStamp = mutableListOf<OreumWholeStamp>()
        fun setOreumStamp (stamp : MutableList<OreumWholeStamp>){
            oreumStamp = stamp
        }
        fun getOreumStamp() = oreumStamp

        private lateinit var loginSharedPreferences: SharedPreferences
        private lateinit var loginSPEditor: SharedPreferences.Editor

        fun getLoginSP() = loginSharedPreferences
        fun getloginSPEditor() = loginSPEditor

        lateinit var firebaseDatabase: FirebaseDatabase
        lateinit var  joinRef : DatabaseReference
        lateinit var  favoriteRef : DatabaseReference
        lateinit var  stampRef : DatabaseReference
        lateinit var  oreumRef : DatabaseReference
        lateinit var  reviewRef : DatabaseReference
        lateinit var  myReviewRef : DatabaseReference

        private var userId : Int = FALSE_NUMBER
        fun setUserId(id: Int){
            userId = id
        }
        fun getUserId() = userId

        private var userNickname : String = FALSE_STRING
        fun setNickname(nickname: String){
            userNickname = nickname
        }
        fun getNickname() = userNickname
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        FirebaseApp.initializeApp(this)
        firebaseDatabase = Firebase.database
        joinRef = firebaseDatabase.getReference(OREUM_JOIN)
        favoriteRef = firebaseDatabase.getReference(OREUM_FAVORITE)
        stampRef = firebaseDatabase.getReference(OREUM_STAMP)
        oreumRef = firebaseDatabase.getReference(OREUM)
        reviewRef = firebaseDatabase.getReference(OREUM_REVIEW)
        myReviewRef = firebaseDatabase.getReference(OREUM_MY_REVIEW)

        loginSharedPreferences = getSharedPreferences(LOGIN, MODE_PRIVATE)
        loginSPEditor = loginSharedPreferences.edit()

        setUserId(getLoginSP().getInt(OREUM_USER_ID, -FALSE_NUMBER))
        findNickname()

        // 화면 세로모드
        settingScreenPortrait()
    }

    private fun settingScreenPortrait() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            @SuppressLint("SourceLockedOrientationActivity")
            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                // 화면 세로모드
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            override fun onActivityStarted(p0: Activity) {}
            override fun onActivityResumed(p0: Activity) {}
            override fun onActivityPaused(p0: Activity) {}
            override fun onActivityStopped(p0: Activity) {}
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
            override fun onActivityDestroyed(p0: Activity) {}
        })
    }
}