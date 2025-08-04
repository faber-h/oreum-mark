package com.okre.oreummark.ui.main.my.favorite

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.okre.oreummark.common.OreumApplication.Companion.favoriteRef
import com.okre.oreummark.common.OreumApplication.Companion.getOreumItem
import com.okre.oreummark.common.OreumApplication.Companion.getUserId
import com.okre.oreummark.common.logMessage
import com.okre.oreummark.databinding.FragmentMyFavoriteBinding
import com.okre.oreummark.model.OreumItem
import com.okre.oreummark.model.favorite.MyFavoriteItem
import com.okre.oreummark.ui.main.list.ListRVAdapter

class MyFavoriteFragment : Fragment() {
    companion object {
        fun newInstance() = MyFavoriteFragment()
    }

    private lateinit var binding: FragmentMyFavoriteBinding
    private var favoriteOreumList = mutableListOf<OreumItem>()
    private var oreumList = mutableListOf<OreumItem>()
    private lateinit var rvAdapter : ListRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oreumList = getOreumItem()
        rvAdapter = ListRVAdapter(favoriteOreumList, activity as Activity)
        binding.favoriteRv.adapter = rvAdapter
    }

    override fun onResume() {
        super.onResume()
        val userId = getUserId().toString()
        favoriteRef.child(userId).addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteOreumList.clear()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(MyFavoriteItem::class.java)?.let {
                        favoriteOreumList.add(oreumList[it.oreumIdx])
                    }
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                logMessage(error.message)
            }
        })
    }
}