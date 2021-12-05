# Place Finder
PlayMap SDK 활용하여 장소를 검색하고 저장하는 기능을 하는 어플리케이션

## Summary
- Kotlin 사용
- MVVM 패턴과 DI 를 조합하여 전반적인 프레임워크 구조 설계
- 클린 아키텍쳐 적용 
- Coroutines, Room, Paging3 사용

## Libraries used
- AAC (Lifecycles, LiveData, ViewModel, Room, Paging)
- Koin
- Coroutines
- Gson
- Glide

## Data Structure
- 그룹 테이블
    - id: Long (PrimaryKey) - 생성시간
    - title: String
    - createAt: Long
    - modifiedAt: Long
    
- 장소 테이블
    - id: Int (PrimaryKey) - poiId
    - groupId: Long (ForeignKey)
    - groupTitle: String
    - title: String
    - addr: String
    - centerLat: Double
    - centerLon: Double
    - createAt: Long
