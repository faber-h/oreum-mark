package com.okre.oreummark.ui.main.my.stamp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.common.INTENT_PUT_IDX
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.databinding.RvItemStampBinding
import com.okre.oreummark.model.stamp.MyStampItem
import com.okre.oreummark.ui.detail.DetailActivity
import java.util.concurrent.TimeUnit

class StampRVAdapter(private var itemList: MutableList<MyStampItem>, private val owner: Activity)
    : RecyclerView.Adapter<StampRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvItemStampBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemStampBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        with(holder.binding) {
            stampRvItemName.text = item.oreumName
            stampRvItemName.clicks()
                .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
                .subscribe { intentDetail(item.oreumIdx) }
            stampRvItemImage.clicks()
                .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
                .subscribe { intentDetail(item.oreumIdx) }
        }
    }

    private fun intentDetail(idx: Int) {
        Intent(owner, DetailActivity::class.java).apply {
            putExtra(INTENT_PUT_IDX, idx)
            owner.startActivity(this)
        }
    }

    override fun getItemCount(): Int = itemList.size
}