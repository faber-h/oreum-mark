package com.okre.oreummark.ui.main.my

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.okre.oreummark.ui.main.my.favorite.MyFavoriteFragment
import com.okre.oreummark.ui.main.my.stamp.MyStampFragment

class MyViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    private val NUM_TABS = 2
    override fun getItemCount() = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MyFavoriteFragment.newInstance()
            1 -> return MyStampFragment.newInstance()
        }
        return MyFavoriteFragment.newInstance()
    }
}