package com.okre.oreummark.ui.main.map

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.R
import com.okre.oreummark.common.FALSE_NUMBER
import com.okre.oreummark.common.INTENT_PUT_IDX
import com.okre.oreummark.common.OREUM_USER_ID
import com.okre.oreummark.common.OreumApplication.Companion.favoriteRef
import com.okre.oreummark.common.OreumApplication.Companion.getLoginSP
import com.okre.oreummark.common.OreumApplication.Companion.getOreumFavorite
import com.okre.oreummark.common.OreumApplication.Companion.getOreumItem
import com.okre.oreummark.common.OreumApplication.Companion.getOreumStamp
import com.okre.oreummark.common.OreumApplication.Companion.stampRef
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.databinding.FragmentMapBinding
import com.okre.oreummark.model.OreumItem
import com.okre.oreummark.model.map.MapViewModel
import com.okre.oreummark.model.oreum.FavoriteViewModel
import com.okre.oreummark.model.oreum.OreumWholeFavorite
import com.okre.oreummark.model.oreum.OreumWholeStamp
import com.okre.oreummark.model.oreum.StampViewModel
import com.okre.oreummark.ui.detail.DetailActivity
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.util.concurrent.TimeUnit

class MapFragment : Fragment(), MapView.MapViewEventListener, MapView.POIItemEventListener {
    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var binding: FragmentMapBinding
    private lateinit var mMapView: MapView
    private lateinit var mMarker: MapPOIItem
    private var oreumMapList = mutableListOf<OreumItem>()
    private var oreumMapFavorite = mutableListOf<OreumWholeFavorite>()
    private var oreumMapStamp = mutableListOf<OreumWholeStamp>()
    private lateinit var mapViewModel: MapViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var stampViewModel: StampViewModel
    private var currentTag: Int = FALSE_NUMBER

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMapView = binding.mapView

        // 카카오맵 이벤트리스너 세팅
        mMapView.setMapViewEventListener(this)
        mMapView.setPOIItemEventListener(this)

        // 카카오맵 마커 생성
        oreumMapList = getOreumItem()
        for (i in oreumMapList.indices) {
            createMarker(
                mMapView,
                oreumMapList[i].oreumName,
                oreumMapList[i].oreumLongitude,
                oreumMapList[i].oreumLatitude,
                oreumMapList[i].idx
            ) // Long = X, Lat = Y
        }

        // 카카오맵 마커 클릭 시 다이알로그 준비
        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        stampViewModel = ViewModelProvider(this).get(StampViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        oreumMapFavorite = getOreumFavorite()
        favoriteViewModel.favoriteListLiveData.observe(this) {
            oreumMapFavorite = it
        }
        oreumMapStamp = getOreumStamp()
        stampViewModel.stampListLiveData.observe(this) {
            oreumMapStamp = it
        }

        checkMyFavoriteStampIcon()

        if (currentTag != FALSE_NUMBER) {
            binding.bottomSheet.favoriteText.text = oreumMapFavorite[currentTag].oreumFavorite.toString()
            binding.bottomSheet.stampText.text = oreumMapStamp[currentTag].oreumStamp.toString()
        }
    }

    // 카카오맵 뷰 처음
    override fun onMapViewInitialized(mapView: MapView) {
        // setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), zoomLevel, animated)
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(33.43, 126.54), 8, true)
    }

    override fun onMapViewCenterPointMoved(mapView: MapView, mapCenterPoint: MapPoint) {}

    override fun onMapViewZoomLevelChanged(mapView: MapView, zoomLevel: Int) {}

    override fun onMapViewSingleTapped(mapView: MapView, mapCenterPoint: MapPoint) {}

    override fun onMapViewDoubleTapped(mapView: MapView, mapCenterPoint: MapPoint) {}

    override fun onMapViewLongPressed(mapView: MapView, mapCenterPoint: MapPoint) {}

    override fun onMapViewDragStarted(mapView: MapView, mapCenterPoint: MapPoint) {}

    override fun onMapViewDragEnded(mapView: MapView, mapCenterPoint: MapPoint) {}

    // 카카오맵 이동 완료
    override fun onMapViewMoveFinished(mapView: MapView, mapCenterPoint: MapPoint) {
        /* 카카오맵 화면 중심좌표 구하기 위한 코드(제주 중심 찾기)
        val mapPointGeo = mapCenterPoint.mapPointGeoCoord
        val positionLatitude : Double = mapPointGeo.latitude
        val positionLongitude : Double = mapPointGeo.longitude
        logMessage("Latitude : $positionLatitude, Longitude : $positionLongitude")*/
    }

    // 카카오맵 마커 클릭
    override fun onPOIItemSelected(mapView: MapView, mapPOIItem: MapPOIItem) {

        // 마커 클릭 -> bottom sheet
        binding.bottomSheet.dialogMarkerLayout.visibility = View.VISIBLE

        currentTag = mapPOIItem.tag
        mapViewModel.changeOreumNameLiveDate(oreumMapList[currentTag].oreumName)
        mapViewModel.oreumNameLiveData.observe(this) {
            binding.bottomSheet.dialogMarkerOreumName.text = it
        }
        mapViewModel.changeOreumAddrLiveDate(oreumMapList[currentTag].oreumAddr)
        mapViewModel.oreumAddrLiveData.observe(this) {
            binding.bottomSheet.dialogMarkerOreumAddr.text = it
        }
        mapViewModel.changeOreumFavoriteLiveDate(oreumMapFavorite[currentTag].oreumFavorite)
        mapViewModel.oreumFavoriteLiveData.observe(this) {
            binding.bottomSheet.favoriteText.text = it.toString()
        }
        mapViewModel.changeOreumStampLiveDate(oreumMapStamp[currentTag].oreumStamp)
        mapViewModel.oreumStampLiveData.observe(this) {
            binding.bottomSheet.stampText.text = it.toString()
        }

        checkMyFavoriteStampIcon()

        binding.bottomSheet.dialogMarkerLayout.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(INTENT_PUT_IDX, currentTag)
                    startActivity(this)
                }
            }

        // 마커 클릭한 곳을 지도 중심으로 이동
        mapView.setMapCenterPointAndZoomLevel(
            MapPoint.mapPointWithGeoCoord(
                mapPOIItem.mapPoint.mapPointGeoCoord.latitude,
                mapPOIItem.mapPoint.mapPointGeoCoord.longitude
            ), 6, true
        )
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}

    // 카카오맵 마커 생성
    private fun createMarker(
        mapView: MapView,
        oreumName: String,
        oreumLongitude: Double,
        oreumLatitude: Double,
        tag: Int
    ) {
        mMarker = MapPOIItem()
        with(mMarker) {
            itemName = oreumName    // 마커 클릭했을 때 이름
            setTag(tag)
            mapPoint = MapPoint.mapPointWithGeoCoord(oreumLatitude, oreumLongitude) // 마커 위치
            markerType = MapPOIItem.MarkerType.CustomImage  // 마커 이미지(커스텀)
            customImageResourceId = R.drawable.oreum_light_pin  // 마커 커스텀 이미지 연결
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage  // 마커 클릭 이미지(커스텀)
            customSelectedImageResourceId = R.drawable.oreum_dark_pin  // 마커 클릭 커스텀 이미지
            isCustomImageAutoscale = true   // 커스텀 마커 이미지 크기 자동 조정
            setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점
            isShowCalloutBalloonOnTouch = false     // 마커 터치했을 때 말풍선(Callout Balloon)을 보여줄지 여부
        }
        mapView.addPOIItem(mMarker)
    }

    // 좋아요, 스탬프 아이콘 나의 오름에 저장되어 있을 경우 색 있는 아이콘으로 변경
    private fun checkMyFavoriteStampIcon() {
        val userId = getLoginSP().getInt(OREUM_USER_ID, FALSE_NUMBER).toString()
        favoriteRef.child(userId).child(currentTag.toString()).get().addOnSuccessListener {
            val favoriteBoolean = it.value != null
            if (favoriteBoolean) {
                binding.bottomSheet.favoriteIcon.setImageResource(R.drawable.ic_favorite_check)
            } else {
                binding.bottomSheet.favoriteIcon.setImageResource(R.drawable.ic_favorite)
            }
        }

        stampRef.child(userId).child(currentTag.toString()).get().addOnSuccessListener {
            val stampBoolean = it.value != null
            if (stampBoolean) {
                binding.bottomSheet.stampIcon.setImageResource(R.drawable.ic_stamp_check)
            } else {
                binding.bottomSheet.stampIcon.setImageResource(R.drawable.ic_stamp)
            }
        }
    }
}