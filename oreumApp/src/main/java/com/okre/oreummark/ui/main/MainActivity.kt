package com.okre.oreummark.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.common.OreumApplication.Companion.oreumRef
import com.okre.oreummark.common.OreumApplication.Companion.setOreumFavortie
import com.okre.oreummark.common.OreumApplication.Companion.setOreumItem
import com.okre.oreummark.common.OreumApplication.Companion.setOreumStamp
import com.okre.oreummark.databinding.ActivityMainBinding
import com.okre.oreummark.model.OreumItem
import com.okre.oreummark.model.OreumRetrofitInterface
import com.okre.oreummark.model.oreum.FavoriteViewModel
import com.okre.oreummark.model.oreum.OreumWholeFavorite
import com.okre.oreummark.model.oreum.OreumWholeStamp
import com.okre.oreummark.ui.main.list.ListFragment
import com.okre.oreummark.ui.main.map.MapFragment
import com.okre.oreummark.ui.main.my.MyFragment
import com.okre.oreummark.ui.main.search.SearchActivity
import com.okre.oreummark.ui.main.setting.SettingActivity
import kotlinx.coroutines.*
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var oreumMainList = mutableListOf<OreumItem>()
    private var oreumMainFavorite = mutableListOf<OreumWholeFavorite>()
    private var oreumMainStamp = mutableListOf<OreumWholeStamp>()
    private var job1: Job? = null
    private lateinit var favoriteViewModel : FavoriteViewModel

    /*// 카카오맵 등록시 필요한 hash_code 얻어오는 코드
    @RequiresApi(Build.VERSION_CODES.P)*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        // 로딩다이알로그 시작
        showLoadingDialog(this)
        // 오름 api 가져오기
        coroutineCallRetrofit()

        // 메인화면 bottom nav
        bottomNavigationViewClick()

        // 돋보기 클릭
        topSearchClick()

        // 설정 클릭
        topSettingClick()

        /* // 카카오맵 등록시 필요한 hash_code 얻어오는 코드(출시 전 임시 코드)
        // 출시할 때 키 해시 변경 필요
        try {
            val information = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = information.signingInfo.apkContentsSigners
            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA").apply {
                    update(signature.toByteArray())
                }
                val HASH_CODE = Base64.encodeToString(md.digest(), Base64.NO_WRAP)

                logMessage("HASH_CODE -> $HASH_CODE")
            }
        } catch (e: Exception) {
            logMessage("Exception -> $e")
        }*/
    }

    private fun coroutineCallRetrofit() {
        job1 = CoroutineScope(Dispatchers.IO).launch {
            val response = OreumRetrofitInterface.getInstance().getOreumList()
            // application oreumItem 담기
            for ((i, list) in response.resultSummary.withIndex()) {
                oreumRef.child(i.toString()).child(OREUM_FAVORITE).get().addOnCompleteListener {
                    val favoriteResult = it.result.value.toString().toInt()
                    oreumMainFavorite.add(OreumWholeFavorite(i, favoriteResult))
                }

                oreumRef.child(i.toString()).child(OREUM_STAMP).get().addOnCompleteListener {
                    val stampResult = it.result.value.toString().toInt()
                    oreumMainStamp.add(OreumWholeStamp(i, stampResult))
                }

                oreumMainList.add(OreumItem(i, list.explain, list.imgPath, list.oreumAddr, list.oreumKname, list.x.toDouble(), list.y.toDouble()))
                /*// firebase 첫 설정 시 사용
                oreumRef.child(i.toString()).child("favorite").setValue(0)
                oreumRef.child(i.toString()).child("stamp").setValue(0)*/
            }
            setOreumItem(oreumMainList)
            setOreumFavortie(oreumMainFavorite)
            favoriteViewModel.changeFavoriteListLiveDate(oreumMainFavorite)
            setOreumStamp(oreumMainStamp)

            withContext(Dispatchers.Main) {
                // 로딩다이알로그 종료
                dissmissLoadingDialog()
                // 첫 화면 프래그먼트 띄우기
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MapFragment.newInstance())
                    .commitNow()
            }
            job1?.cancel()
        }
    }

    // 메인화면 bottom nav
    private fun bottomNavigationViewClick() {
        with(binding) {
            bottomNavigation.run {
                setOnItemSelectedListener {
                    when(it.itemId) {
                        R.id.menu_map -> {
                            changeFragment(MapFragment.newInstance())
                            topTitle.text = getString(R.string.map_title)
                            topSearch.visibility = View.GONE
                        }
                        R.id.menu_list -> {
                            changeFragment(ListFragment.newInstance())
                            topTitle.text = getString(R.string.list_title)
                            topSearch.visibility = View.VISIBLE
                        }
                        R.id.menu_my -> {
                            changeFragment(MyFragment.newInstance())
                            topTitle.text = getString(R.string.my_title)
                            topSearch.visibility = View.GONE
                        }
                    }
                    true
                }

                setOnItemReselectedListener {
                    when(it.itemId) {
                        R.id.menu_map -> {}
                        R.id.menu_list -> {}
                        R.id.menu_my -> {}
                    }
                }
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment).commit()
    }

    private fun topSearchClick() {
        binding.topSearch.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, SearchActivity::class.java))
            }
    }

    private fun topSettingClick() {
        binding.topSetting.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, SettingActivity::class.java))
            }
    }
}