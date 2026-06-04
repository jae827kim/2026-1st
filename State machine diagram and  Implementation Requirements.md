## 4. State Machine Diagram

<img width="1837" height="1403" alt="image" src="https://github.com/user-attachments/assets/443e0268-a299-4ec1-888f-ece6d5029717" />
State Machine Diagram은 K-Living Solution 전체 시스템을 하나의 객체로 보고, 프로그램 실행부터 종료까지 시스템이 가질 수 있는 상태(State)와 상태 전이(Transition)를 나타낸다. 본 시스템은 크게 로그인 전 공통 흐름, 일반 사용자 흐름(User Flow), 관리자 흐름(Administrator Flow)의 3가지 영역으로 구분된다.

---

### (1) 시스템 초기화 및 로그인 전 공통 흐름 (Common Flow)

* 시스템 시작 및 데이터 로드
  * Launch System (시작점) -> 프로그램 실행 시 Data Loading 상태로 전이되어 User.txt, Post.txt 파일을 불러온다.
  * 로드 성공: Login Screen 상태로 이동한다.
  * 파일 없음: File Initialize 상태로 전이되어 빈 User.txt, Post.txt 파일을 초기 생성한 뒤 Login Screen으로 이동한다.
  * 로드 실패: Error State로 전이되며 시스템이 최종 종료된다.

* 회원가입 및 로그인
  * Login Screen 상태에서 '회원가입' 선택 시 Registration Screen 상태로 이동한다.
    * 가입 성공: Login Screen으로 복귀한다.
    * 가입 실패 (입력 오류 / ID 중복 / 저장 실패): Registration Screen 상태를 유지한다.
  * Login Screen에서 올바른 계정 정보를 입력하면 회원 유형에 따라 다음 메인 상태로 전이된다.
    * 일반 사용자 로그인 성공: User Main 상태로 이동한다.
    * 관리자 로그인 성공: Admin Main 상태로 이동한다.
    * ID/PW 오류 또는 차단 사용자: Login Screen 상태를 유지한다.

------

### (2) 일반 사용자 흐름 (User Flow)

일반 사용자가 로그인하면 Wait User Action 대기 상태를 거쳐 각 기능으로 분기된다.

* 질문 등록 및 포인트 검증
  * Post Writing: 질문 내용과 보상 포인트를 입력한다. (등록 버튼 클릭)
  * Point Checking: 사용자의 보유 포인트가 충분한지 검증한다.
    * 포인트 충분 / 저장 성공: Post Saving 상태를 거쳐 Question List로 이동한다.
    * 포인트 부족 / 입력 오류: 저장 실패 및 포인트 복구 후 다시 Post Writing 상태로 되돌아온다.

* 질문 목록 및 상세 보기
  * Question List: 등록된 질문 목록을 확인하며, 특정 게시글 선택 시 Question Detail 상태로 진입한다.

* 답변 작성 및 저장
  * Question Detail에서 '답변 등록' 선택 시 Answer Writing 상태로 이동한다.
  * 작성 완료 후 Answer Saving 상태에서 저장을 수행하며, 성공 시 다시 Question Detail로 복귀하여 답변 목록이 갱신된다. (오류나 저장 실패 시 작성 상태를 유지한다.)

* 답변 채택 및 포인트 정산 아카이브
  * 질문자가 답변을 채택하면 Answer Selection 상태로 전이되어 권한 및 채택 여부를 확인한다.
  * 확인 성공 시 Transaction Processing에서 포인트 정산을 수행한다.
  * 정산 성공 시 Transaction Result 화면에 결과를 출력하고, Archive Saving 상태에서 채택된 질문과 답변을 아카이브에 최종 저장한 뒤 Question Detail로 돌아간다. (정산 실패 시 Question Detail로 복귀한다.)

* 프로필 조회 및 로그아웃
  * Profile View: 내 프로필 정보를 확인한다. ('뒤로가기' 시 대기 상태로 복귀한다.)
  * Logout Confirm: 로그아웃 확인 후 완료 시 Login Screen으로 전이된다.

------

### (3) 관리자 흐름 (Administrator Flow)

관리자가 로그인하면 Wait Admin Action 대기 상태를 거쳐 시스템 관리 기능으로 분기된다.

* 시스템 통계 확인
  * Statistics View: 시스템 통계 화면을 조회한다.
  * Statistics Loading: 새로고침 요청 시 전체 사용자 수, 게시글 수, 답변 수, 포인트 유통량 등을 다시 계산하여 Statistics View에 갱신한다.

* 사용자 관리 (차단 / 차단 해제)
  * User Control View: 회원 관리 화면으로 진입한다.
  * User Blocking: 특정 사용자 차단 처리 후 User.txt를 갱신하고 복귀한다.
  * User Unblocking: 차단된 사용자 해제 처리 후 User.txt를 갱신하고 복귀한다.

* 게시글 관리 (삭제 / 복구)
  * Post Control View: 게시글 관리 화면으로 진입한다.
  * Post Deleting: 특정 게시글을 삭제 상태로 변경 후 Post.txt를 갱신하고 복귀한다.
  * Post Restoring: 삭제된 게시글을 정상 복구 후 Post.txt를 갱신하고 복귀한다.

* 관리자 로그아웃
  * Admin Logout Confirm: 관리자 로그아웃 확인 후 완료 시 Login Screen으로 전이된다.
 
## 5. Implementation Requirements

### 5.1 H/W Platform Requirements

| Requirement | Recommended Specification |
| :--- | :--- |
| CPU | Intel i3 이상 또는 동급 CPU |
| RAM | 4GB 이상 |
| HDD / SSD | 10GB 이상 |
| Network | 필수 아님 |
| Input Device | 키보드 및 마우스 |

### 5.2 S/W Platform Requirements

| Requirement | Recommended Specification |
| :--- | :--- |
| Operating System | Microsoft Windows 7 이상 |
| Implementation Language | Java 1.8 이상 |

### 5.3 Non-functional Requirements
