package com.okre.oreummark.ui.detail.stamp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.okre.oreummark.common.*
import com.okre.oreummark.common.OreumApplication.Companion.getLoginSP
import com.okre.oreummark.common.OreumApplication.Companion.getOreumStamp
import com.okre.oreummark.common.OreumApplication.Companion.joinRef
import com.okre.oreummark.common.OreumApplication.Companion.oreumRef
import com.okre.oreummark.common.OreumApplication.Companion.setOreumStamp
import com.okre.oreummark.common.OreumApplication.Companion.stampRef
import com.okre.oreummark.model.oreum.OreumWholeStamp
import com.okre.oreummark.model.oreum.StampViewModel
import com.okre.oreummark.model.stamp.MyStampItem

fun saveStamp(stampBoolean: Boolean, oreumIdx: Int, oreumName: String, owner: ViewModelStoreOwner) {
    val userId = getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER).toString()
    var oreumWholeStamp: MutableList<OreumWholeStamp>
    var wholeStampNum: Int
    var myStampNum: Int
    val stampViewModel : StampViewModel = ViewModelProvider(owner).get(StampViewModel::class.java)

    joinRef.child(OREUM_USER).child(userId).child(OREUM_MY_STAMP_NUM).get().addOnCompleteListener {
        myStampNum = it.result.value.toString().toInt()
        myStampNum ++
        joinRef.child(OREUM_USER).child(userId).child(OREUM_MY_STAMP_NUM).setValue(myStampNum)
    }

    oreumRef.child(oreumIdx.toString()).child(OREUM_STAMP).get().addOnCompleteListener {
        wholeStampNum = it.result.value.toString().toInt()
        oreumWholeStamp = getOreumStamp()

        val myStampItem = MyStampItem(userId.toInt(), oreumIdx, oreumName, stampBoolean)
        if (stampBoolean) {
            stampRef.child(userId).child(oreumIdx.toString()).setValue(myStampItem) // my stamp 변경
            wholeStampNum++ // 전체 stamp 변경
        }
        // 전체 stamp 변경
        oreumRef.child(oreumIdx.toString()).child(OREUM_STAMP).setValue(wholeStampNum)
        oreumWholeStamp[oreumIdx].oreumStamp = wholeStampNum
        setOreumStamp(oreumWholeStamp)
        stampViewModel.changeStampListLiveDate(oreumWholeStamp)
    }
}