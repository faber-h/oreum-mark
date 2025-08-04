package com.okre.oreummark.ui.main.list

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.databinding.RvItemListBinding
import com.okre.oreummark.model.OreumItem
import com.okre.oreummark.model.oreum.FavoriteViewModel
import com.okre.oreummark.model.oreum.OreumWholeFavorite
import com.okre.oreummark.model.oreum.OreumWholeStamp
import com.okre.oreummark.model.oreum.StampViewModel
import com.okre.oreummark.ui.detail.DetailActivity
import java.util.concurrent.TimeUnit

class ListRVAdapter(
    private var itemList: MutableList<OreumItem>,
    private val owner: Activity
) : RecyclerView.Adapter<ListRVAdapter.ViewHolder>() {

    private var oreumListFavorite = mutableListOf<OreumWholeFavorite>()
    private var oreumListStamp = mutableListOf<OreumWholeStamp>()
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var stampViewModel: StampViewModel
    private var idx: Int = FALSE_NUMBER

    inner class ViewHolder(val binding: RvItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        favoriteViewModel =
            ViewModelProvider(owner as ViewModelStoreOwner).get(FavoriteViewModel::class.java)
        stampViewModel =
            ViewModelProvider(owner as ViewModelStoreOwner).get(StampViewModel::class.java)
        val item = itemList[position]
        idx = item.idx
        with(holder.binding) {
            Glide.with(owner.applicationContext)
                .load(item.imgPath)
                .transform(CenterCrop(), RoundedCorners(30))
                .into(rvItemImage)

            rvItemOreumName.text = item.oreumName

            oreumListFavorite = OreumApplication.getOreumFavorite()
            favoriteText.text = oreumListFavorite[idx].oreumFavorite.toString()
            favoriteViewModel.favoriteListLiveData.observe(owner as LifecycleOwner) {
                oreumListFavorite = it
                favoriteText.text = oreumListFavorite[idx].oreumFavorite.toString()
            }
            oreumListStamp = OreumApplication.getOreumStamp()
            stampText.text = oreumListStamp[idx].oreumStamp.toString()
            stampViewModel.stampListLiveData.observe(owner as LifecycleOwner) {
                oreumListStamp = it
                stampText.text = oreumListStamp[idx].oreumStamp.toString()
            }

            // 좋아요, 스탬프 아이콘 나의 오름에 저장되어 있을 경우 색 있는 아이콘으로 변경
            val userId = OreumApplication.getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER).toString()
            OreumApplication.favoriteRef.child(userId).child(idx.toString()).get().addOnSuccessListener {
                val favoriteBoolean = it.value != null
                if (favoriteBoolean) {
                    holder.binding.favoriteIcon.setImageResource(R.drawable.ic_favorite_check)
                } else {
                    holder.binding.favoriteIcon.setImageResource(R.drawable.ic_favorite_white)
                }
            }

            OreumApplication.stampRef.child(userId).child(idx.toString()).get().addOnSuccessListener {
                val stampBoolean = it.value != null
                if (stampBoolean) {
                    holder.binding.stampIcon.setImageResource(R.drawable.ic_stamp_check)
                } else {
                    holder.binding.stampIcon.setImageResource(R.drawable.ic_stamp_white)
                }
            }

            // list 클릭
            root.clicks()
                .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        owner,
                        rvItemImage,
                        TRANSITION_TAG
                    )
                    Intent(owner, DetailActivity::class.java).apply {
                        putExtra(INTENT_PUT_IDX, item.idx)
                        owner.startActivity(this, options.toBundle())
                    }
                }
        }
    }

    override fun getItemCount(): Int = itemList.size
}