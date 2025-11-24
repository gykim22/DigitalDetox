# Digital Detox
## Pilltip | 오늘의 집중이 내일의 나를 피워낼 수 있도록
<img width="128" height="128" alt="icon-512" src="https://github.com/user-attachments/assets/bbca20c2-683d-4de4-a86a-3a9b9b08edee" />

내가 오롯이 공부한 시간과, 그렇지 않은 시간을 정확히 구분하고, 스스로 돌아보고자 기획한 애플리케이션 서비스입니다.

우리 삶에 깊숙이 들어온 모바일 디바이스의 유혹으로부터 벗어나 하고자 하는, 해야만 하는 일에 집중할 수 있도록 도와줍니다.

타이머와 노트, 두 가지로 공부한 시간과 내용을 간편하게 기록하고, 하루하루 쌓여가는 기록을 보며 성장할 수 있으면 좋겠습니다.

## 서비스 소개
스마트폰을 사용하지 않고 집중하는 시간과 그렇지 않은 시간을 측정하고, 해당 시간과 공부 기록을 노트로 저장할 수 있습니다.
| 로고 | 타이머 화면 | 타이머 동작 | 노트 작성 | 노트 작성 취소 | 노트 목록 |
|----------|----------|----------|----------|----------|----------|
|<img width="128" height="128" alt="icon-512" src="https://github.com/user-attachments/assets/bbca20c2-683d-4de4-a86a-3a9b9b08edee" />| <img src="https://github.com/user-attachments/assets/17c4d4c4-9096-4a2e-abb8-00907d3c0abd" width="150"/> | <img src="https://github.com/user-attachments/assets/315b29f0-89e8-45bc-b645-ae952609c309" width="150"/> | <img src="https://github.com/user-attachments/assets/0d4d9ee7-627b-4a46-8c17-89b347fdbde2" width="150"/> | <img src="https://github.com/user-attachments/assets/39f0052b-e009-412a-915b-4bc772f0dc36" width="150"/> | <img src="https://github.com/user-attachments/assets/fa9c27c1-2067-4c05-afd7-542458bf0da8" width="150"/> |

## 공부 기록
[노션 바로가기](https://www.notion.so/gykim22/Digital-Detox-2a8f39afe338803b9e5fdf89bbf5d7cc?source=copy_link)

## 기능
- **듀얼 타이머 시스템 (Primary/Sub Timer)**
  - 공부시간 타이머 (Primary Timer): 사용자가 앱을 포그라운드에서 머물며 공부하는 시간을 측정합니다.
  - 휴식시간 타이머 (Sub Timer): 사용자가 앱을 백그라운드(화면 꺼짐 / 타 앱 사용)에 두었을 때의 시간을 측정합니다.
- **자동 타이머 전환** 
  - 앱의 라이프사이클 이벤트를 감지하여, 사용자가 앱을 벗어나면 공부 시간 기록을 자동으로 멈추고 휴식 시간 기록을 시작합니다. 
  - 앱으로 돌아오면 공부 시간 기록이 자동으로 재개됩니다.
- **학습 기록 노트**
  - 타이머 종료 시, 전체 시간, 공부 시간, 휴식 시간이 자동으로 기록된 노트 작성 기능을 제공하여, 간단한 공부결과나 해당 일자의 기록을 저장, 관리할 수 있습니다.
- **노트 관리**
  - 작성한 노트를 제목, 작성 일자, 전체 시간 등 다양한 기준으로 정렬하고, 수정 및 삭제할 수 있습니다.

## 기술 및 로직
|구분 | 기술 / 라이브러리 |설명|
|-----|------------------|-----|
|**개발 언어**|Kotlin|Android 주력 개발 언어|
|**UI Framework**|Jetpack Compose|선언형 UI 프레임워크 사용|
|**아키텍처**|Clean Architecture (MVVM)|모듈화 및 테스트 용이성 확보|
|**의존성 주입**|Hilt|Android DI 라이브러리 사용|
|**데이터베이스**|Room|SQLite 객체 매핑 라이브러리|
|**비동기 처리**|Kotlin Coroutines, Flow|비동기 데이터 스트림 및 반응형 스트림|
  
### 핵심 로직
- **듀얼 타이머 시스템 (Primary/Sub Timer)**
  - Hilt Qualifier를 활용하여 동일 TimerRepositoryImpl 구현체 두 개를 각각 Primary(공부 시간 측정용)와 Sub(휴식 시간 측정용)로 명확하게 구분하여 주입합니다.
  - TimerRepositoryImpl: CoroutineScope 내에서  정확한 시간을 측정하고, MutableStateFlow를 통해 ViewModel에 실시간으로 업데이트합니다.
  - TimerViewModel: collectPrimaryTimerState()와 collectSubTimerState()를 통해 두 타이머의 시간을 실시간으로 수집합니다.
- **라이프사이클 기반 타이머 제어**
  - LocalLifecycleOwner와 DisposableEffect를 사용하여 Android Lifecycle Event(ON_PAUSE, ON_RESUME)를 감지합니다.
  - 공부 시간 타이머 상태(RUNNING/PAUSED)와 앱의 포어/백그라운드 전환 따라 타이머의 작동을 상호 전환합니다.
- **노트 기능**
  - 공부 시간과 휴식 시간을 자동으로 포함하는 간단한 노트 기능을 제공합니다.
  - title, contents, timestamp, total_time, study_time (Primary), break_time (Sub)
  - 노트 관리/정렬
    - NoteListScreen은 제목, 작성 일자, 총 시간, 공부 시간, 휴식 시간을 기준으로 오름차순/내림차순 정렬이 가능합니다. 이 기능은 GetAllNotesUseCase 내부에서 Flow 데이터를 가공(map)하여 처리되므로, 데이터베이스 로직이 도메인 계층에 노출되지 않습니다.
  - 노트 작성/편집
    - 노트 작성 시 add_edit_note?noteId={noteId}&total={total}&study={study}&rest={rest} 경로를 통해 타이머 결과를 전달받습니다.
  - 작성 중 뒤로가기 버튼을 누르면 AlertDialog를 띄워 작성 중인 데이터 손실을 방지하는 UX 로직이 적용되어 있습니다.

## **아키텍처**
Digital Detox는 앱의 유지보수성과 테스트 용이성을 높이기 위해 클린 아키텍처(Clean Architecture) 원칙을 따릅니다.
### 아키텍쳐 설명
|레이어 | 설명|
|-----|-----------------------|
|Presentation Layer| TimerScreen, NoteListScreen 등 Composable UI와 ViewModel로 구성되며, UI 상태를 관리하고 사용자 이벤트를 처리합니다.|
|Domain Layer| 비즈니스 로직을 담고 있는 Use Cases와 Repository 인터페이스, Model로 구성됩니다.|
|Data Layer| TimerRepositoryImpl, NoteRepositoryImpl, Room Database 등 외부 데이터 소스 접근을 담당합니다.|
<img width="810" height="1203" alt="image" src="https://github.com/user-attachments/assets/5dbe641b-ac34-465b-95a5-70fadb7eb758" />

### Android Entry & 초기화 흐름
<img width="2513" height="214" alt="image" src="https://github.com/user-attachments/assets/987ef0e0-a082-4835-93ea-311157606f64" />

### Navigation 아키텍쳐
<img width="1172" height="907" alt="image" src="https://github.com/user-attachments/assets/d0fec006-8dbd-4fbd-af06-6b0e0f8b3578" />

### 데이터 흐름도
- 사용자 이벤트는 이벤트 객체를 통해 위로 올라가, 상태는 반응형 스트림을 통 아래로 흐르는 구조로 작성했습니다. 
- SOLID 원칙을 준수하고자, 상위 모듈이 하위 모듈에 직접 의존하지 않고 추상화된 인터페이스에 의존하도록 구성했습니다.
- 앱의 특정 상태를 ViewModel에서만 관리하도록 구성했습니다.
- 상태가 변경되면 UI가 자동으로 재구성되도록 반응형 로직을 작성했습니다.
<img width="1021" height="1271" alt="image" src="https://github.com/user-attachments/assets/c19f680a-81b9-4551-b4b8-c2c59d0896c3" />

## 개발자 소개
<div align="center">
  <table>
    <tr>
     <td align="center" width="25%">
        <img src="https://avatars.githubusercontent.com/u/77396909?v=4" width="100" height="100"><br>
        <a href="https://github.com/gykim22">김기윤</a><br>
        FE
      </td>
    </tr>
  </table>
</div>

본 레포지토리 내 코드 또는 작업물의 무단 복제 및 사전 동의 없는 활용을 금합니다.
