package com.okre.oreummark.ui.main.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.okre.oreummark.R
import com.okre.oreummark.databinding.FragmentMyBinding

class MyFragment : Fragment() {
    companion object {
        fun newInstance() = MyFragment()
    }

    private lateinit var binding: FragmentMyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabTitleArray = arrayOf(getString(R.string.my_list_favorite), getString(R.string.my_list_stamp))
        binding.myViewPager.adapter = MyViewPagerAdapter(parentFragmentManager, lifecycle)
        TabLayoutMediator(binding.myTabLayout, binding.myViewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }
}