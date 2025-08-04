package com.okre.oreummark.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.okre.oreummark.common.OreumApplication.Companion.getAppInstance
import com.okre.oreummark.common.OreumApplication.Companion.getUserId
import com.okre.oreummark.common.OreumApplication.Companion.joinRef
import com.okre.oreummark.common.OreumApplication.Companion.setNickname
import com.okre.oreummark.model.login.JoinItem
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun toastMessage(message:String) {
    Toast.makeText(getAppInstance(), message, Toast.LENGTH_SHORT).show()
}

fun logMessage(message: String) {
    Log.e(OREUM_TAG, message)
}

lateinit var mLoadingDialog: LoadingDialog

fun showLoadingDialog(context: Context) {
    mLoadingDialog = LoadingDialog(context)
    mLoadingDialog.show()
}

fun dissmissLoadingDialog() {
    if (mLoadingDialog.isShowing) {
        mLoadingDialog.dismiss()
    }
}

fun findNickname() {
    val userId = getUserId().toString()
    joinRef.child(OREUM_USER).child(userId).addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.getValue(JoinItem::class.java)?.let {
                    val nickname = it.nickname
                    setNickname(nickname)
                    return
                }
        }

        override fun onCancelled(error: DatabaseError) {
            logMessage(error.message)
        }
    })
}


@SuppressLint("ConstantLocale")
val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

@SuppressLint("ConstantLocale")
@RequiresApi(Build.VERSION_CODES.O)
val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.getDefault())

fun changeFormatTime(times: Long) : String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val newNow = LocalDateTime.ofInstant(Date(times).toInstant(), ZoneId.systemDefault())
        newNow.format(dateFormat).toString()
    } else {
        simpleDateFormat.format(times)
    }
}