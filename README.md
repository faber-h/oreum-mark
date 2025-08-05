# 🌋 오름마크 (oreumMark)

# 📖 목차

- [💡 기획 의도](#-기획-의도)
- [🔍 주요 기능](#-주요-기능)
- [🛠️ 기술 스택](#️-기술-스택)
- [⚙️ 구현 세부사항](#️-구현-세부사항)
  - [위치 기반 자동 스탬프 시스템](#위치-기반-자동-스탬프-시스템)
  - [firebase-좋아요-시스템](#firebase-좋아요-시스템)
  - [api-기반-오름-목록-제공](#api-기반-오름-목록-제공)
  - [실시간-후기-시스템](#실시간-후기-시스템)
<br />

# 💡 기획 의도

제주에는 오름과 올레길이 대표적인 걷기·산책 코스로 잘 알려져 있습니다.<br />
이 중 제주올레길에는 체계적인 스탬프 투어 시스템이 마련되어 있어, 많은 이들이 이를 통해 여행의 즐거움을 더하고 있습니다.<br />

하지만 **제주 오름**(화산 언덕)에는 아직 그러한 시스템이 마련되어 있지 않습니다.<br />
이 점에서 착안하여, 오름을 중심으로 한 위치 기반 스탬프 투어 앱을 기획하게 되었습니다.<br />

<br />

# 🔍 주요 기능

| 오름 목록 | 좋아요 | 스탬프 | 후기 |
| ---- | ---- | ---- | ---- |
| <img src="https://github.com/user-attachments/assets/01c8ce90-b543-44c9-a68e-1a4bd27800ad" width="180"/> | <img src="https://github.com/user-attachments/assets/637b12ce-c744-484b-9df1-8c1e75bd6339" width="180"/> | <img src="https://github.com/user-attachments/assets/269c089a-3adc-4b5e-aaf4-e81e14b93a6b" width="180"/> | <img src="https://github.com/user-attachments/assets/52e0d23a-49d2-408c-a468-e40df61d2cde" width="180"/>
| **오름 목록**<br />- API 기반으로 제공되는 오름 목록을 불러와, 지도 형식과 목록 형식 두 가지로 볼 수 있습니다. | **좋아요 기능**<br />- 관심 있는 오름에 좋아요를 클릭하면 관심 목록에 저장됩니다. | **정복한 오름에 스탬프 찍기**<br />- 오름의 좌표 정보를 기반으로 사용자의 현재 위치가 반경 내에 들어오면 자동으로 스탬프 찍기 기능이 실행됩니다. | **오름 후기 작성**<br />- 각 오름에 대해 간단한 후기 작성 가능<br />- 후기에는 날짜, 작성자(닉네임), 좋아요 수, 댓글 등을 함께 출력합니다. |

<br />

# ⚒️ 기술 스택 

<div>
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=Firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/Google%20Maps-4285F4?style=for-the-badge&logo=googlemaps&logoColor=white" />
</div>

<br />

# ⚙️ 구현 세부사항

## 위치 기반 자동 스탬프 시스템

오름 정복의 성취감을 제공하기 위해 GPS 기반 자동 스탬프 시스템을 구현했습니다.<br />
사용자가 직접 스탬프를 찍는 번거로움 없이, 오름의 지정된 반경 내에 들어오면 자동으로 스탬프가 찍히는 방식으로 설계했습니다.<br />

### 현재 위치 요청

```
fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ...)
```
FusedLocationProviderClient를 통해 구글 위치 제공 API 기반의 현재 위치를 고정밀로 요청합니다.<br />
위치 권한이 허용된 경우에만 위치 요청이 수행됩니다.<br />

### 반경 체크 및 자동 스탬프
```
val distance = oreumLocation.distanceTo(currentLocation)
val stampBoolean = distance <= 50.0
```

오름의 좌표와 현재 위치를 비교하여 거리를 계산합니다.<br />
50m 반경 이내에에 사용자가 위치할 경우 자동 스탬프 기능이 작동하도록 처리했습니다.<br />

<br />

## Firebase 좋아요 시스템

사용자들이 관심 있는 오름을 효과적으로 관리할 수 있도록 좋아요 기능을 구현했습니다.<br />
단순한 로컬 저장이 아닌 Firebase 실시간 데이터베이스를 활용하여 여러 기기에서 동기화되고, 실시간으로 업데이트되는 좋아요 시스템을 구축했습니다.<br />
사용자별로 개별 좋아요 목록을 관리하면서도 전체 오름의 좋아요 수를 실시간으로 집계하여 다른 사용자들에게도 인기도를 보여줍니다.<br />

### MVVM 패턴과 LiveData를 통한 반응형 UI

좋아요 상태의 실시간 반영을 위해 MVVM 아키텍처 패턴을 적용했습니다.<br />
모델에서 LiveData로 좋아요 목록을 관리하고, UI에서는 이를 관찰하여 자동으로 화면을 업데이트하도록 구현했습니다.<br />
```
// 반응형 UI를 위한 LiveData 구조
private var _favoriteListMutableLiveData = MutableLiveData<MutableList<OreumWholeFavorite>>()
val favoriteListLiveData : LiveData<MutableList<OreumWholeFavorite>>
    get() = _favoriteListMutableLiveData
```
사용자가 좋아요 버튼을 클릭하면 즉시 UI가 반응하고, 백그라운드에서 Firebase에 데이터를 저장하는 방식으로 빠른 사용자 경험을 제공했습니다.<br />

<br />

## API 기반 오름 목록 제공

제주도 공공데이터 API를 연동하여 오름 정보를 활용했습니다.<br />
사용자가 정보를 효과적으로 탐색할 수 있도록 지도와 목록 두 가지 형태의 인터페이스를 제공했습니다.<br />
API 통신 과정에서 발생할 수 있는 네트워크 불안정성을 고려하여 자동 재시도 메커니즘을 구현했습니다.<br />

### Retrofit과 OkHttp 기반 안정적 API 통신
제주도 오름 정보 API와의 통신을 위해 모바일 환경의 불안정한 네트워크 상황을 고려하여 Retrofit과 OkHttp를 조합했습니다.<br />
연결 실패 시 최대 2회까지 자동으로 재시도하도록 구현했습니다.<br />

```
// 네트워크 재시도 로직
while (!response.isSuccessful && tryCount < maxLimit) {
    tryCount++
    response = chain.proceed(request)
}
```

또한 연결 타임아웃과 읽기 타임아웃을 설정하여 사용자가 과도하게 기다리지 않으면서도 네트워크 상황에 유연하게 대응할 수 있도록 했습니다.<br />

<br />

## 실시간 후기 시스템

오름 방문 경험을 공유하고 다른 사용자들과 소통할 수 있는 후기 시스템을 구현했습니다.<br />
Firebase 실시간 데이터베이스를 활용하여 새로운 후기가 작성되거나 좋아요가 변경될 때마다 모든 사용자에게 즉시 반영되도록 했습니다.<br />

### 후기 작성 시간 처리

작성 시간을 타임스탬프로 저장하고 한국 시간대 기준으로 "yyyy.MM.dd. HH:mm" 형식으로 변환하여 사용자에게 표시했습니다.<br />
Android 버전 호환성을 고려하여 API 26 이상에서는 `LocalDateTime`을, 그 이하에서는 `SimpleDateFormat`을 사용하도록 구현했습니다.<br />

```
// Android 버전 호환성을 고려한 시간 형식화
fun changeFormatTime(times: Long) : String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val newNow = LocalDateTime.ofInstant(Date(times).toInstant(), ZoneId.systemDefault())
        newNow.format(dateFormat).toString()
    } else {
        simpleDateFormat.format(times)
    }
}
```

