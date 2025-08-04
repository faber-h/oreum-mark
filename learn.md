# OreumMark
## kakao map API
1. 카카오 개발자 사이트에 어플리케이션 정보 등록

[카카오 개발자 사이트](https://developers.kakao.com/)

로그인 -> 상단 '내 애플리케이션' -> '애플리케이션 추가하기'
-> '앱 아이콘'(선택), '앱 이름', '사업자명'(개인일 경우 임의 작성) -> 저장

2. 카카오맵에 키 해시 등록
- 무분별한 트래픽을 방지하기 위해 해시가 등록된 앱에 대해서만 API 요청을 허용하기 때문에 키 해시 등록이 필요
- 내 애플리케이션 > 앱 설정 > 플랫폼 > Android 플랫폼 등록 > 패키지명, 마켓 URL, 키 해시 > 저장
- 개발자의 로컬 개발 환경의 키 해시 얻어오는 코드
```
try {
    val information = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
    val signatures = information.signingInfo.apkContentsSigners
    for (signature in signatures) {
        val md = MessageDigest.getInstance("SHA").apply {
            update(signature.toByteArray())
        }
        val HASH_CODE = Base64.encodeToString(md.digest(), Base64.NO_WRAP)

        Log.d("TAG", "HASH_CODE -> $HASH_CODE")
    }
} catch (e: Exception) {
    Log.d("TAG", "Exception -> $e")
}
```
- 출시 후 키 해시 얻는 방법 (**!!해당 키 해시를 등록하지 않을 경우 출시된 앱에서 카카오맵이 정상적으로 작동되지 않음!!**)
> 앱 선택 > 설정 > 앱 무결성 > 앱 서명 > SHA-1 인증서 지문 복사 > 터미널에서 SHA-1 인증서 지문을 Base64로 인코딩<br/>
> echo 복사한SHA-1 | xxd -r -p | openssl base64 

3. 네이티브 앱 키 등록
- Permission 추가
```
// AndroidManifest.xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<application
    android:usesCleartextTraffic="true">
```
- APP KEY 추가
  내 애플리케이션 > 앱 설정 > 앱 키 > 네이티브 앱 키 복사
```
// AndroidManifest.xml
<meta-data android:name="com.kakao.sdk.AppKey" android:value="XXXXXXXXXXXXXXXXXXXXXXXXXXXX"/>
```

4. 라이브러리 파일 추가
- 프로젝트 라이브러리 파일 추가

[kakao map guide](https://apis.map.kakao.com/android/guide/) > SDK 다운로드<br/>
libDaumMapAndroid.jar 파일 : /app/libs/에 복사<br/>
libs 폴더 : /app/source/main 에 복사 후 폴더명 jniLibs 으로 변경
- build.gradle(Module) 의존성 추가
```
implementation files('libs/libDaumMapAndroid.jar')
```

5. 카카오맵 동작 확인
```
<net.daum.mf.map.api.MapView
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
<br/><br/><br/>

## kakao map - custom marker
```
val mMarker = MapPOIItem()
with(mMarker) {
    itemName = "name"    // 마커 클릭했을 때 이름
    setTag(tag)   // 마커 태그
    mapPoint = MapPoint.mapPointWithGeoCoord(Latitude, Longitude) // 마커 위치
    markerType = MapPOIItem.MarkerType.CustomImage  // 마커 이미지(커스텀)
    customImageResourceId = R.drawable.이미지  // 마커 커스텀 이미지 연결
    selectedMarkerType = MapPOIItem.MarkerType.CustomImage  // 마커 클릭 이미지(커스텀)
    customSelectedImageResourceId = R.drawable.클릭이미지  // 마커 클릭 커스텀 이미지
    isCustomImageAutoscale = true   // 커스텀 마커 이미지 크기 자동 조정(false : 항상 동일한 크기로 표시)
    setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점
    isShowCalloutBalloonOnTouch = false     // 마커 터치했을 때 말풍선(Callout Balloon)을 보여줄지 여부
}
binding.mapView.addPOIItem(mMarker)
```
<br/><br/><br/>

## BottomNavigationView - font
```
// themes.xml
<style name="BottomNavigationViewTextStyle">
    <item name="android:fontFamily">@font/fontname</item>
</style>
```
```
<com.google.android.material.bottomnavigation.BottomNavigationView
    app:itemTextAppearanceActive="@style/BottomNavigationViewTextStyle"
    app:itemTextAppearanceInactive="@style/BottomNavigationViewTextStyle"/>
```
<br/><br/><br/>

## Glide - radius, centerCrop
```
Glide.transform(CenterCrop(), RoundedCorners(dp값))
```
<br/><br/><br/>

## 출시 - Google 심사용 사용자 계정 정보 제공
정책 -> 앱 콘텐츠 -> 앱 액세스 권한 -> 관리 -> 전체 또는 일부 기능이 제한됨 -> ＋ 새 안내 추가 -> 로그인 계정 정보를 작성 -> 적용
- 로그인 계정 정보를 작성 
    - 이름: Google 심사용 계정
    - 사용자 이름/전화번호: 심사용으로 만든 계정
    - 비밀번호: 심사용으로 만든 계정의 비밀번호
    - 다른 안내: 심사 진행을 위해 임시 계정 정보를 제공해 드립니다. 해당 계정을 통해 검토 진행을 부탁드립니다.
<br/><br/><br/>

## dark mode off
- 디바이스 다크 모드에 따라 반전되는 색을 원치 않을 경우 다크 모드 비활성화가 필요
- DayNight -> Light
```
// res > values > themes > night/themes.xml
<style name="Theme.name" parent="Theme.MaterialComponents.DayNight.NoActionBar">
// DayNight -> Light
<style name="Theme.name" parent="Theme.MaterialComponents.Light.NoActionBar">
```
<br/><br/><br/>
