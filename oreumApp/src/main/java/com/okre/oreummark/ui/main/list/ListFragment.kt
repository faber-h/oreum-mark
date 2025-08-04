package com.okre.oreummark.ui.main.list

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.okre.oreummark.common.OreumApplication.Companion.getOreumItem
import com.okre.oreummark.databinding.FragmentListBinding
import com.okre.oreummark.model.OreumItem

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var binding: FragmentListBinding
    private var oreumList = mutableListOf<OreumItem>()
    private lateinit var rvAdapter : ListRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        rvAdapter = ListRVAdapter(oreumList, activity as Activity)
        binding.rv.adapter = rvAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oreumList = getOreumItem()

        /* sort
        val spinnerItems = resources.getStringArray(R.array.list_sort_array)
        with(binding) {
            spinnerSort.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems)
            spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View, position: Int, p3: Long) {
                    when (position) {
                        0 -> {
                            //oreumList = oreumList.sortBy { it.oreumName } as MutableList<OreumItem>
                            //setOreumItem(oreumList)
                            rvAdapter.notifyItemRangeChanged(0, oreumList.size -1)
                        }
                        1 -> {
                            //oreumList = oreumList.sortByDescending { it.oreumName } as MutableList<OreumItem>
                            //setOreumItem(oreumList)
                            rvAdapter.notifyItemRangeChanged(0, oreumList.size -1)
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //oreumList = oreumList.sortBy { it.oreumName } as MutableList<OreumItem>
                    //setOreumItem(oreumList)
                }
            }
        }*/
    }
}