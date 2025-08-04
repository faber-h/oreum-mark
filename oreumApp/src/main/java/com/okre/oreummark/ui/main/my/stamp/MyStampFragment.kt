package com.okre.oreummark.ui.main.my.stamp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.common.OreumApplication.Companion.getNickname
import com.okre.oreummark.common.OreumApplication.Companion.getUserId
import com.okre.oreummark.common.OreumApplication.Companion.stampRef
import com.okre.oreummark.databinding.FragmentMyStampBinding
import com.okre.oreummark.model.stamp.MyStampItem

class MyStampFragment : Fragment() {
    companion object {
        fun newInstance() = MyStampFragment()
    }

    private lateinit var binding: FragmentMyStampBinding
    private lateinit var rvAdapter : StampRVAdapter
    private var stampList = mutableListOf<MyStampItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyStampBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAdapter = StampRVAdapter(stampList, requireActivity())
        binding.myStampRv.adapter = rvAdapter

        val nickname = getNickname()
        binding.myStampText.text = getString(R.string.my_stamp_text, nickname)

        val userId = OreumApplication.getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER).toString()
        var myStampNum: Int
        OreumApplication.joinRef.child(OREUM_USER).child(userId).child(OREUM_MY_STAMP_NUM).get().addOnCompleteListener {
            myStampNum = it.result.value.toString().toInt()
            binding.myStampNum.text = getString(R.string.my_stamp_num, myStampNum)
        }
    }

    override fun onResume() {
        super.onResume()
        val userId = getUserId()
        stampRef.child(userId.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                stampList.clear()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(MyStampItem::class.java)?.let {
                        stampList.add(MyStampItem(userId, it.oreumIdx, it.oreumName, it.stampBoolean))
                    }
                }
                rvAdapter.notifyItemRangeChanged(0, stampList.size -1)
            }

            override fun onCancelled(error: DatabaseError) {
                logMessage(error.message)
            }
        })
    }

}