package com.okre.oreummark.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.common.OreumApplication.Companion.getOreumItem
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.databinding.ActivitySearchBinding
import com.okre.oreummark.model.OreumItem
import com.okre.oreummark.ui.main.list.ListRVAdapter
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var oreumList = mutableListOf<OreumItem>()
    private var oreumSearchList = mutableListOf<OreumItem>()
    private lateinit var rvAdapter : ListRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        oreumList = getOreumItem()

        // 검색 버튼
        searchBtn()

        // 뒤로가기 버튼
        backBtn()
    }

    private fun searchBtn() {
        binding.searchBtnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { // 검색 버튼 누를 때 호출
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean { // 검색창에서 글자가 변경이 일어날 때마다 호출
                search(newText)
                return true
            }
        })
    }

    private fun search(searchText: String?) {
        oreumSearchList = oreumList.filter {
            if (searchText != null) {
                it.oreumName.contains(searchText, false)
            } else {
                return
            }
        } as MutableList<OreumItem>
        if (!oreumSearchList.isEmpty()) {
            binding.searchRv.visibility = View.VISIBLE
            binding.searchEmpty.visibility = View.GONE
            rvAdapter = ListRVAdapter(oreumSearchList, this@SearchActivity)
            binding.searchRv.adapter = rvAdapter
        } else {
            binding.searchRv.visibility = View.GONE
            binding.searchEmpty.visibility = View.VISIBLE
        }
    }

    private fun backBtn() {
        binding.searchBtnBack.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }
    }
}