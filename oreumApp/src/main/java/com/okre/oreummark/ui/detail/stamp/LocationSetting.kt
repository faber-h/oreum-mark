package com.okre.oreummark.ui.detail.stamp

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.okre.oreummark.R
import com.okre.oreummark.common.*
import com.okre.oreummark.databinding.ActivitySettingLocationBinding
import com.okre.oreummark.ui.detail.DetailActivity

class LocationSetting : AppCompatActivity() {

    private lateinit var binding: ActivitySettingLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingLocationBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        networkCheck()
    }

    private fun networkCheck() {
        val status = OreumNetworkManager(applicationContext).checkNetworkState()
        if (status) {
            checkLocationCurrentDevice()
        } else {
            val dialog = NetworkDialog()
            dialog.show(supportFragmentManager, NETWORK_DIALOG_TAG)
            dialog.myDialogInterface = object : NetworkDialog.DialogListener {
                override fun onDialogPositiveClick() {
                    val againCheckStatus = OreumNetworkManager(applicationContext).checkNetworkState()
                    if (againCheckStatus) {
                        dialog.dismiss()
                        checkLocationCurrentDevice()
                    } else {
                        toastMessage(getString(R.string.network_must))
                    }
                }
            }
        }
    }

    private fun checkLocationCurrentDevice() {
        val locationIntervalTime = 3000L
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationIntervalTime)
            .setWaitForAccurateLocation(true) // 정확한 위치를 기다림 true일 경우 지하, 이동 중에 늦어질 수 있음
            .setMinUpdateIntervalMillis(locationIntervalTime) // 위치 획득 후 update 되는 최소 주기
            .setMaxUpdateDelayMillis(locationIntervalTime) // 위치 획득 후 update delay 최대
            .build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()

        // 위치 정보 설정 상태 확인
        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(builder)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 위치 정보설정 On 상태
        task.addOnSuccessListener {
            // 현재 위치 정보
            currentLocation()
        }

        // 위치 정보설정 Off 상태
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    resolutionForResult.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    logMessage("${sendEx.message}")
                }
            }
        }
    }

    // 위치 정보 설정 상태 intentSenderRequest
    private val resolutionForResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            checkLocationCurrentDevice()
        } else {
            toastMessage(getString(R.string.location_must))
            checkLocationCurrentDevice()
        }
    }

    // 현재 위치 정보
    private fun currentLocation() {
        binding.settingLayout.visibility = View.VISIBLE
        val oreumName = intent.getStringExtra(OREUM_NAME)
        binding.settingText.text = resources.getString(R.string.setting_location_text, oreumName)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    logMessage("Cannot get location.")
                else {
                    val lat = location.latitude
                    val lon = location.longitude
                    // 오름과 현재 위치 비교
                    compareLocation(lat, lon)
                }
            }
    }

    // 오름과 현재 위치 비교
    private fun compareLocation(lat: Double, lon: Double) {
        val oreumLatitude = intent.getDoubleExtra(OREUM_LATITUDE, FALSE_DOUBLE)
        val oreumLongitude = intent.getDoubleExtra(OREUM_LONGITUDE, FALSE_DOUBLE)
        val oreumLocation = Location(OREUM_LOCATION)
        oreumLocation.latitude = oreumLatitude
        oreumLocation.longitude = oreumLongitude

        val currentLocation = Location(OREUM_CURRENT_LOCATION)
        currentLocation.latitude = lat
        currentLocation.longitude = lon

        val distance = oreumLocation.distanceTo(currentLocation)
        stampBoolean(distance)
    }

    // 위치 비교 결과 50m 내일 경우 true
    private fun stampBoolean(distance: Float) {
        val stampBoolean = distance <= 50.0
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(STAMP_BOOLEAN, stampBoolean)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}