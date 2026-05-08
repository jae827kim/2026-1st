## 2. Use case analysis

### 2.1 Use case diagram

아래의 그림은 K-Living Solution 시스템의 Use Case Diagram을 나타낸 것이다. Conceptualization Document에서 정의한 Use Case List를 바탕으로 Actor와 Use Case 사이의 관계를 도출하였다.

본 시스템의 Actor는 일반 사용자(User)와 관리자(Administrator)로 구분하였다. User는 회원가입, 로그인, 프로필 라벨 확인, 질문 등록 및 보상 포인트 예치, 답변 채택, 포인트 정산 결과 확인 기능을 사용할 수 있다. Administrator는 로그인 후 시스템 통계 확인과 데이터 제어 기능을 사용할 수 있다.

Data Load, Data Save, Data Update는 사용자가 직접 실행하는 기능이라기보다는 회원가입, 로그인, 질문 등록, 답변 채택, 통계 확인, 데이터 제어 과정에서 내부적으로 수행되는 기능이므로 include 관계로 표현하였다. 또한 질문 등록 기능은 보상 포인트 예치와 게시글 생성을 포함하고, 답변 채택 기능은 포인트 정산과 거래 결과 출력을 포함하도록 구성하였다.

![유스케이스 다이어그램](use%20case%20diagram.png)

본 시스템은 실제 외부 DB 서버를 사용하지 않고, Java File I/O를 통해 User.txt와 Post.txt 파일에 데이터를 저장한다. 따라서 파일 DB는 시스템 외부에서 독립적인 목적을 가지고 상호작용하는 Actor가 아니라, 시스템 내부에서 참조하고 갱신하는 데이터 저장소로 보았다. 이에 따라 Use Case Diagram의 Actor는 User와 Administrator로 한정하였고, Data Load, Data Save, Data Update는 주요 Use Case에 포함되는 내부 기능으로 표현하였다.

| Use Case ID | Use Case Name | Korean Name | Actor |
| :--- | :--- | :--- | :--- |
| UC-01 | Registration Info | 회원 정보 등록 | User |
| UC-02 | Login | 로그인 | User, Administrator |
| UC-03 | User Profile with Labeling | 사용자 프로필 라벨링 | User |
| UC-04 | Post & Reward | 질문 등록 및 보상 예치 | User |
| UC-05 | Answer Selection | 답변 채택 | User |
| UC-06 | Transaction Result | 거래 결과 확인 | User |
| UC-07 | Data Load | 데이터 불러오기 | System Internal |
| UC-08 | Data Save | 데이터 저장 | System Internal |
| UC-09 | Data Update | 데이터 갱신 | System Internal |
| UC-10 | System Statistics | 시스템 통계 확인 | Administrator |
| UC-11 | Data Control | 데이터 제어 | Administrator |
| UC-12 | Answer Registration | 답변 등록 | User |

## 2.2 Use Case Description

### 2.2.1 Use Case #1 : Registration Info

| Use Case #1 : Registration Info |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 사용자가 K-Living Solution을 이용하기 위해 계정 정보와 자취 경력을 등록하는 기능 |
| Scope | K-Living Solution |
| Level | User Level |
| Author | 김재원 |
| Last Update | May. 8. 2026 |
| Status | Analysis |
| Primary Actor | User |
| Preconditions | 시스템이 실행되어 있어야 한다. User.txt 파일에 접근할 수 있어야 한다. |
| Trigger | 사용자가 회원가입 양식을 작성한 후 등록 버튼을 눌렀을 때 |
| Success Post Condition | 새로운 사용자의 계정 정보와 자취 경력 데이터가 User.txt에 저장된다. 이후 사용자는 로그인 기능을 사용할 수 있다. |
| Failed Post Condition | 사용자의 회원가입 정보가 저장되지 않으며, 사용자는 로그인 기능을 사용할 수 없다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 사용자가 K-Living Solution에 회원가입을 하려고 할 때 시작된다. |
| 2 | 사용자는 회원가입 화면에서 ID, Password, 닉네임, 자취 경력 등의 정보를 입력한다. |
| 3 | 사용자는 회원가입 버튼을 누른다. |
| 4 | 시스템은 입력된 정보가 올바른 형식인지 확인한다. |
| 5 | 시스템은 동일한 ID가 이미 존재하는지 User.txt에서 확인한다. |
| 6 | 중복된 ID가 없다면 시스템은 새로운 사용자 정보를 User.txt에 저장한다. |
| 7 | 이 Use case는 사용자 정보가 성공적으로 저장되면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 4 | 4a. 필수 입력 정보가 비어있는 경우<br>4a.1. 시스템은 비어있는 정보를 입력하라는 메시지를 띄운다.<br>4a.2. 회원가입 화면으로 돌아간다.<br>4b. 자취 경력 값이 올바르지 않은 경우<br>4b.1. 시스템은 자취 경력을 올바르게 입력하라는 메시지를 띄운다.<br>4b.2. 회원가입 화면으로 돌아간다. |
| 5 | 5a. 이미 존재하는 ID인 경우<br>5a.1. 시스템은 중복된 ID가 존재한다는 메시지를 띄운다.<br>5a.2. 회원가입 화면으로 돌아간다. |
| 6 | 6a. User.txt에 데이터를 저장하지 못한 경우<br>6a.1. 시스템은 회원가입 실패 메시지를 띄운다.<br>6a.2. 회원가입 화면으로 돌아간다. |
| RELATED INFORMATION |  |
| Performance | ≤ 3 Seconds |
| Frequency | 사용자당 1번 |
| Concurrency | None |
| Due Date |  |

### 2.2.2 Use Case #2 : Login

| Use Case #2 : Login |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 등록된 사용자인지 확인하고 서비스 이용 권한을 부여하는 기능 |
| Scope | K-Living Solution |
| Level | User Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | User, Administrator |
| Preconditions | 사용자는 회원가입이 되어 있어야 한다. User.txt 파일에 접근할 수 있어야 한다. |
| Trigger | 사용자가 ID와 Password를 입력하고 로그인 버튼을 눌렀을 때 |
| Success Post Condition | 인증된 사용자에게 세션이 부여되고, 사용자는 개인화된 기능을 사용할 수 있다. |
| Failed Post Condition | 사용자는 로그인에 실패하며, 질문 작성, 답변 등록, 답변 채택, 포인트 거래 기능을 사용할 수 없다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 사용자가 K-Living Solution에 로그인하려고 할 때 시작된다. |
| 2 | 사용자는 로그인 화면에서 ID와 Password를 입력한다. |
| 3 | 사용자는 로그인 버튼을 누른다. |
| 4 | 시스템은 User.txt에서 입력된 ID와 Password가 등록된 정보와 일치하는지 확인한다. |
| 5 | 등록된 사용자라면 시스템은 해당 사용자에게 세션을 부여한다. |
| 6 | 시스템은 로그인한 사용자의 프로필과 라벨 정보를 불러온다. |
| 7 | 이 Use case는 로그인이 성공하면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 2 | 2a. ID 또는 Password가 입력되지 않은 경우<br>2a.1. 시스템은 ID 또는 Password를 입력하라는 메시지를 띄운다.<br>2a.2. 로그인 화면으로 돌아간다. |
| 4 | 4a. 등록되지 않은 ID인 경우<br>4a.1. 시스템은 등록되지 않은 사용자라는 메시지를 띄운다.<br>4a.2. 로그인 화면으로 돌아간다.<br>4b. Password가 일치하지 않는 경우<br>4b.1. 시스템은 비밀번호가 일치하지 않는다는 메시지를 띄운다.<br>4b.2. 로그인 화면으로 돌아간다.<br>4c. User.txt를 읽지 못한 경우<br>4c.1. 시스템은 데이터 로드 실패 메시지를 띄운다.<br>4c.2. 로그인 화면으로 돌아간다. |
| RELATED INFORMATION |  |
| Performance | ≤ 3 Seconds |
| Frequency | 사용자당 하루 평균 1~2번 |
| Concurrency | None |
| Due Date |  |

### 2.2.3 Use Case #3 : User Profile with Labeling

| Use Case #3 : User Profile with Labeling |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 사용자의 자취 숙련도를 라벨로 표시하여 답변 신뢰도를 높이는 기능 |
| Scope | K-Living Solution |
| Level | User Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | User |
| Preconditions | 사용자는 로그인된 상태여야 한다. User.txt에 사용자 정보와 자취 경력 데이터가 저장되어 있어야 한다. |
| Trigger | 로그인 직후 또는 다른 사용자가 특정 사용자의 프로필을 조회했을 때 |
| Success Post Condition | 사용자의 자취 경력에 맞는 라벨이 프로필에 표시된다. |
| Failed Post Condition | 사용자의 라벨이 표시되지 않거나 기본 라벨로 표시된다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 사용자의 프로필 정보를 표시해야 할 때 시작된다. |
| 2 | 시스템은 User.txt에서 해당 사용자의 계정 정보와 자취 경력 데이터를 불러온다. |
| 3 | 시스템은 자취 경력 기준에 따라 사용자에게 적절한 라벨을 부여한다. |
| 4 | 시스템은 사용자의 닉네임, 현재 포인트, 누적 포인트, 라벨 정보를 화면에 표시한다. |
| 5 | 이 Use case는 사용자 프로필과 라벨이 정상적으로 표시되면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 2 | 2a. 해당 사용자의 정보를 찾을 수 없는 경우<br>2a.1. 시스템은 사용자 정보를 찾을 수 없다는 메시지를 띄운다.<br>2a.2. 이전 화면으로 돌아간다.<br>2b. User.txt를 읽지 못한 경우<br>2b.1. 시스템은 데이터 로드 실패 메시지를 띄운다.<br>2b.2. 기본 프로필 화면으로 돌아간다. |
| 3 | 3a. 자취 경력 데이터가 올바르지 않은 경우<br>3a.1. 시스템은 기본 라벨을 부여한다.<br>3a.2. 프로필 화면에 기본 라벨을 표시한다. |
| RELATED INFORMATION |  |
| Performance | ≤ 2 Seconds |
| Frequency | 사용자 프로필 조회 시마다 |
| Concurrency | None |
| Due Date |  |

### 2.2.4 Use Case #4 : Post & Reward

| Use Case #4 : Post & Reward |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 사용자가 질문을 등록하면서 답변 보상을 위한 포인트를 예치하는 기능 |
| Scope | K-Living Solution |
| Level | User Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | User |
| Preconditions | 사용자는 로그인된 상태여야 한다. 사용자는 질문 등록에 필요한 현재 포인트를 보유하고 있어야 한다. |
| Trigger | 사용자가 질문 내용과 보상 포인트를 입력하고 질문 등록 버튼을 눌렀을 때 |
| Success Post Condition | 사용자의 현재 포인트가 차감되고, 질문 게시글이 Post.txt에 저장된다. |
| Failed Post Condition | 포인트가 차감되지 않으며, 질문 게시글도 저장되지 않는다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 사용자가 질문을 등록하려고 할 때 시작된다. |
| 2 | 사용자는 질문 작성 화면에서 제목, 내용, 카테고리, 보상 포인트를 입력한다. |
| 3 | 사용자는 질문 등록 버튼을 누른다. |
| 4 | 시스템은 입력된 질문 정보와 보상 포인트가 올바른지 확인한다. |
| 5 | 시스템은 사용자의 현재 포인트가 보상 포인트보다 크거나 같은지 확인한다. |
| 6 | 시스템은 사용자의 현재 포인트에서 보상 포인트를 차감한다. |
| 7 | 시스템은 질문 게시글과 예치된 보상 포인트 정보를 Post.txt에 저장한다. |
| 8 | 시스템은 변경된 사용자 포인트 정보를 User.txt에 갱신한다. |
| 9 | 이 Use case는 질문 등록과 보상 포인트 예치가 성공하면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 2 | 2a. 제목 또는 내용이 비어있는 경우<br>2a.1. 시스템은 필수 정보를 입력하라는 메시지를 띄운다.<br>2a.2. 질문 작성 화면으로 돌아간다. |
| 4 | 4a. 보상 포인트가 0 이하인 경우<br>4a.1. 시스템은 보상 포인트를 올바르게 입력하라는 메시지를 띄운다.<br>4a.2. 질문 작성 화면으로 돌아간다. |
| 5 | 5a. 사용자의 현재 포인트가 부족한 경우<br>5a.1. 시스템은 포인트가 부족하다는 메시지를 띄운다.<br>5a.2. 질문 작성 화면으로 돌아간다. |
| 7 | 7a. Post.txt에 게시글 저장을 실패한 경우<br>7a.1. 시스템은 질문 등록 실패 메시지를 띄운다.<br>7a.2. 포인트 차감 이전 상태로 복구한다.<br>7a.3. 질문 작성 화면으로 돌아간다. |
| 8 | 8a. User.txt에 포인트 정보를 갱신하지 못한 경우<br>8a.1. 시스템은 데이터 갱신 실패 메시지를 띄운다.<br>8a.2. 질문 등록을 취소하고 이전 상태로 복구한다. |
| RELATED INFORMATION |  |
| Performance | ≤ 3 Seconds |
| Frequency | 알 수 없음 |
| Concurrency | None |
| Due Date |  |

### 2.2.5 Use Case #5 : Answer Selection

| Use Case #5 : Answer Selection |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 질문자가 채택한 답변에 대해 보상 포인트를 확정하고 답변자에게 이전하는 기능 |
| Scope | K-Living Solution |
| Level | User Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | User |
| Preconditions | 사용자는 로그인된 상태여야 한다. 사용자는 해당 질문의 작성자여야 한다. 질문 게시글에는 하나 이상의 답변이 등록되어 있어야 한다. |
| Trigger | 질문자가 등록된 답변 중 하나를 채택했을 때 |
| Success Post Condition | 예치된 보상 포인트가 답변자에게 지급되고, 게시글의 채택 상태가 갱신된다. |
| Failed Post Condition | 보상 포인트가 이전되지 않으며, 답변 채택 상태도 변경되지 않는다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 질문자가 답변을 채택하려고 할 때 시작된다. |
| 2 | 질문자는 자신의 질문 게시글 상세 화면에 접속한다. |
| 3 | 질문자는 등록된 답변 목록 중 가장 적절한 답변을 선택한다. |
| 4 | 질문자는 답변 채택 버튼을 누른다. |
| 5 | 시스템은 현재 사용자가 해당 질문의 작성자인지 확인한다. |
| 6 | 시스템은 해당 질문이 이미 채택 완료 상태인지 확인한다. |
| 7 | 시스템은 예치된 보상 포인트를 답변자의 현재 포인트와 누적 포인트에 반영한다. |
| 8 | 시스템은 게시글의 채택 상태와 채택된 답변 정보를 Post.txt에 갱신한다. |
| 9 | 시스템은 답변자의 포인트 정보를 User.txt에 갱신한다. |
| 10 | 시스템은 포인트 정산 결과를 사용자에게 보여준다. |
| 11 | 이 Use case는 답변 채택과 포인트 정산이 성공하면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 5 | 5a. 현재 사용자가 질문 작성자가 아닌 경우<br>5a.1. 시스템은 답변 채택 권한이 없다는 메시지를 띄운다.<br>5a.2. 게시글 상세 화면으로 돌아간다. |
| 6 | 6a. 이미 채택이 완료된 질문인 경우<br>6a.1. 시스템은 이미 답변이 채택된 게시글이라는 메시지를 띄운다.<br>6a.2. 게시글 상세 화면으로 돌아간다. |
| 7 | 7a. 답변자 정보를 찾을 수 없는 경우<br>7a.1. 시스템은 답변자 정보를 찾을 수 없다는 메시지를 띄운다.<br>7a.2. 포인트 정산을 중단한다. |
| 8 | 8a. Post.txt 갱신에 실패한 경우<br>8a.1. 시스템은 답변 채택 실패 메시지를 띄운다.<br>8a.2. 포인트 정산 이전 상태로 복구한다. |
| 9 | 9a. User.txt 갱신에 실패한 경우<br>9a.1. 시스템은 포인트 갱신 실패 메시지를 띄운다.<br>9a.2. 답변 채택 이전 상태로 복구한다. |
| RELATED INFORMATION |  |
| Performance | ≤ 3 Seconds |
| Frequency | 질문 게시글당 최대 1번 |
| Concurrency | None |
| Due Date |  |

### 2.2.6 Use Case #6 : Transaction Result

| Use Case #6 : Transaction Result |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 포인트 정산 성공 여부와 최종 상태를 사용자에게 알려주는 기능 |
| Scope | K-Living Solution |
| Level | User Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | User |
| Preconditions | 포인트 정산 로직이 실행된 상태여야 한다. |
| Trigger | 포인트 정산 로직이 서버에서 종료된 직후 |
| Success Post Condition | 사용자는 포인트 변동 내역과 처리 결과를 확인할 수 있다. |
| Failed Post Condition | 사용자는 포인트 정산 결과를 확인할 수 없다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 답변 채택 또는 포인트 거래가 종료된 후 시작된다. |
| 2 | 시스템은 포인트 정산이 성공했는지 확인한다. |
| 3 | 시스템은 질문자와 답변자의 포인트 변동 내역을 정리한다. |
| 4 | 시스템은 사용자에게 거래 성공 여부, 차감 또는 지급된 포인트, 최종 포인트를 출력한다. |
| 5 | 이 Use case는 사용자가 포인트 정산 결과를 확인하면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 2 | 2a. 포인트 정산에 실패한 경우<br>2a.1. 시스템은 정산 실패 메시지를 출력한다.<br>2a.2. 실패 원인을 사용자에게 보여준다. |
| 3 | 3a. 사용자 포인트 정보를 불러오지 못한 경우<br>3a.1. 시스템은 포인트 정보 조회 실패 메시지를 출력한다.<br>3a.2. 이전 화면으로 돌아간다. |
| RELATED INFORMATION |  |
| Performance | ≤ 2 Seconds |
| Frequency | 포인트 거래 발생 시마다 |
| Concurrency | None |
| Due Date |  |

### 2.2.7 Use Case #7 : Data Load

| Use Case #7 : Data Load |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | User.txt와 Post.txt의 데이터를 메모리로 불러와 시스템 로직에서 사용할 수 있도록 하는 기능 |
| Scope | K-Living Solution |
| Level | System Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | System |
| Preconditions | 프로그램이 실행되어 있어야 한다. User.txt 또는 Post.txt 파일에 접근할 수 있어야 한다. |
| Trigger | 프로그램 구동 시 또는 데이터 동기화가 필요할 때 |
| Success Post Condition | 텍스트 파일에 저장된 사용자 정보와 게시글 정보가 객체화되어 메모리에 저장된다. |
| Failed Post Condition | 파일 데이터를 불러오지 못하며, 시스템은 정상적인 로직 처리를 할 수 없다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 프로그램이 실행되거나 데이터 동기화가 필요할 때 시작된다. |
| 2 | 시스템은 User.txt 파일의 존재 여부를 확인한다. |
| 3 | 시스템은 Post.txt 파일의 존재 여부를 확인한다. |
| 4 | 시스템은 User.txt에서 사용자 정보를 읽어온다. |
| 5 | 시스템은 Post.txt에서 게시글과 답변 정보를 읽어온다. |
| 6 | 시스템은 읽어온 데이터를 사용자 객체와 게시글 객체로 변환한다. |
| 7 | 이 Use case는 파일 데이터가 메모리에 정상적으로 로드되면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 2 | 2a. User.txt 파일이 존재하지 않는 경우<br>2a.1. 시스템은 새로운 User.txt 파일을 생성한다.<br>2a.2. 사용자 데이터를 빈 리스트로 초기화한다. |
| 3 | 3a. Post.txt 파일이 존재하지 않는 경우<br>3a.1. 시스템은 새로운 Post.txt 파일을 생성한다.<br>3a.2. 게시글 데이터를 빈 리스트로 초기화한다. |
| 4 | 4a. User.txt 데이터를 읽는 데 실패한 경우<br>4a.1. 시스템은 사용자 데이터 로드 실패 메시지를 출력한다.<br>4a.2. 로드 작업을 중단한다. |
| 5 | 5a. Post.txt 데이터를 읽는 데 실패한 경우<br>5a.1. 시스템은 게시글 데이터 로드 실패 메시지를 출력한다.<br>5a.2. 로드 작업을 중단한다. |
| 6 | 6a. 파일 데이터의 형식이 올바르지 않은 경우<br>6a.1. 시스템은 잘못된 형식의 데이터를 제외한다.<br>6a.2. 정상적으로 읽을 수 있는 데이터만 객체로 변환한다. |
| RELATED INFORMATION |  |
| Performance | ≤ 3 Seconds |
| Frequency | 프로그램 실행 시 또는 데이터 동기화 시 |
| Concurrency | None |
| Due Date |  |

### 2.2.8 Use Case #8 : Data Save

| Use Case #8 : Data Save |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 새로 생성된 데이터가 유실되지 않도록 텍스트 파일에 저장하는 기능 |
| Scope | K-Living Solution |
| Level | System Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | System |
| Preconditions | 저장할 데이터가 메모리에 존재해야 한다. User.txt 또는 Post.txt 파일에 접근할 수 있어야 한다. |
| Trigger | 신규 사용자, 게시글, 답변 등의 데이터가 생성되거나 작업이 종료될 때 |
| Success Post Condition | 메모리상의 데이터가 User.txt 또는 Post.txt에 물리적으로 기록된다. |
| Failed Post Condition | 변경된 데이터가 파일에 저장되지 않아 프로그램 종료 후 데이터가 유실될 수 있다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 새로운 데이터가 생성되거나 저장이 필요할 때 시작된다. |
| 2 | 시스템은 저장할 데이터의 종류가 사용자 데이터인지 게시글 데이터인지 확인한다. |
| 3 | 시스템은 메모리에 저장된 데이터를 파일 저장 형식에 맞게 변환한다. |
| 4 | 시스템은 변환된 데이터를 User.txt 또는 Post.txt에 기록한다. |
| 5 | 시스템은 파일 저장이 성공했는지 확인한다. |
| 6 | 이 Use case는 데이터가 성공적으로 저장되면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 3 | 3a. 저장할 데이터 형식이 올바르지 않은 경우<br>3a.1. 시스템은 데이터 형식 오류 메시지를 출력한다.<br>3a.2. 저장 작업을 중단한다. |
| 4 | 4a. 파일에 접근할 수 없는 경우<br>4a.1. 시스템은 파일 접근 실패 메시지를 출력한다.<br>4a.2. 저장 작업을 중단한다. |
| 5 | 5a. 파일 저장이 실패한 경우<br>5a.1. 시스템은 데이터 저장 실패 메시지를 출력한다.<br>5a.2. 변경된 데이터를 메모리에 유지한다. |
| RELATED INFORMATION |  |
| Performance | ≤ 2 Seconds |
| Frequency | 데이터 생성 또는 프로그램 종료 시마다 |
| Concurrency | None |
| Due Date |  |

### 2.2.9 Use Case #9 : Data Update

| Use Case #9 : Data Update |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 포인트 잔액, 누적 포인트, 답변 채택 여부 등 변경된 데이터 상태를 파일 DB에 반영하는 기능 |
| Scope | K-Living Solution |
| Level | System Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | System |
| Preconditions | 기존 데이터가 메모리에 로드되어 있어야 한다. 갱신할 대상 데이터가 존재해야 한다. |
| Trigger | 포인트 잔액, 채택 여부, 사용자 상태 등 데이터값에 변화가 생겼을 때 |
| Success Post Condition | 변경된 데이터가 메모리와 파일 DB에 모두 반영되어 최신 상태를 유지한다. |
| Failed Post Condition | 변경된 데이터가 반영되지 않거나, 메모리 데이터와 파일 데이터가 불일치할 수 있다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 시스템 내부 데이터값에 변화가 생겼을 때 시작된다. |
| 2 | 시스템은 갱신할 데이터의 종류와 대상을 확인한다. |
| 3 | 시스템은 메모리상의 사용자 또는 게시글 객체 값을 변경한다. |
| 4 | 시스템은 변경된 데이터를 파일 저장 형식으로 변환한다. |
| 5 | 시스템은 User.txt 또는 Post.txt에 변경된 내용을 반영한다. |
| 6 | 시스템은 파일 갱신이 정상적으로 완료되었는지 확인한다. |
| 7 | 이 Use case는 변경된 데이터가 정상적으로 갱신되면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 2 | 2a. 갱신할 대상 데이터를 찾지 못한 경우<br>2a.1. 시스템은 대상 데이터가 존재하지 않는다는 메시지를 출력한다.<br>2a.2. 데이터 갱신을 중단한다. |
| 3 | 3a. 메모리상의 객체 값을 변경하지 못한 경우<br>3a.1. 시스템은 메모리 데이터 갱신 실패 메시지를 출력한다.<br>3a.2. 이전 상태를 유지한다. |
| 5 | 5a. 파일 갱신에 실패한 경우<br>5a.1. 시스템은 파일 갱신 실패 메시지를 출력한다.<br>5a.2. 메모리 데이터를 이전 상태로 복구한다. |
| RELATED INFORMATION |  |
| Performance | ≤ 2 Seconds |
| Frequency | 데이터 변경 발생 시마다 |
| Concurrency | None |
| Due Date |  |

### 2.2.10 Use Case #10 : System Statistics

| Use Case #10 : System Statistics |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 관리자가 시스템 운영 상황과 포인트 흐름을 모니터링하는 기능 |
| Scope | K-Living Solution |
| Level | Admin Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | Administrator |
| Preconditions | 관리자는 로그인된 상태여야 한다. User.txt와 Post.txt 데이터를 불러올 수 있어야 한다. |
| Trigger | 관리자가 운영자 페이지에서 시스템 통계 확인 버튼을 눌렀을 때 |
| Success Post Condition | 관리자는 사용자 활동 지표, 게시글 수, 답변 수, 포인트 유통량 등의 정보를 확인할 수 있다. |
| Failed Post Condition | 관리자는 시스템 통계 정보를 확인할 수 없다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 관리자가 시스템 통계를 확인하려고 할 때 시작된다. |
| 2 | 관리자는 운영자 페이지에서 시스템 통계 메뉴를 선택한다. |
| 3 | 시스템은 관리자로 로그인된 사용자인지 확인한다. |
| 4 | 시스템은 User.txt와 Post.txt에서 사용자, 게시글, 답변, 포인트 데이터를 불러온다. |
| 5 | 시스템은 전체 사용자 수, 게시글 수, 답변 수, 채택된 답변 수, 총 포인트 유통량을 계산한다. |
| 6 | 시스템은 계산된 통계 결과를 관리자 화면에 출력한다. |
| 7 | 이 Use case는 통계 정보가 정상적으로 출력되면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 3 | 3a. 관리자가 아닌 사용자가 접근한 경우<br>3a.1. 시스템은 접근 권한이 없다는 메시지를 띄운다.<br>3a.2. 메인 화면으로 돌아간다. |
| 4 | 4a. User.txt 또는 Post.txt 데이터를 불러오지 못한 경우<br>4a.1. 시스템은 데이터 로드 실패 메시지를 띄운다.<br>4a.2. 운영자 페이지로 돌아간다. |
| 5 | 5a. 통계를 계산할 데이터가 없는 경우<br>5a.1. 시스템은 통계 데이터가 없다는 메시지를 출력한다.<br>5a.2. 빈 통계 결과를 표시한다. |
| RELATED INFORMATION |  |
| Performance | ≤ 3 Seconds |
| Frequency | 관리자가 요청할 때마다 |
| Concurrency | None |
| Due Date |  |

### 2.2.11 Use Case #11 : Data Control

| Use Case #11 : Data Control |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 관리자가 부적절한 사용자나 콘텐츠를 제어하여 커뮤니티의 건전성을 유지하는 기능 |
| Scope | K-Living Solution |
| Level | Admin Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | Administrator |
| Preconditions | 관리자는 로그인된 상태여야 한다. 제어할 사용자 또는 게시글 데이터가 존재해야 한다. |
| Trigger | 관리자가 신고된 게시글 삭제 또는 규정 위반 사용자 차단을 수행하려고 할 때 |
| Success Post Condition | 신고된 게시글이 삭제되거나 규정 위반 사용자가 차단된다. 변경된 데이터는 파일 DB에 반영된다. |
| Failed Post Condition | 부적절한 사용자나 콘텐츠가 제어되지 않으며, 기존 데이터 상태가 유지된다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 관리자가 데이터 제어 기능을 사용하려고 할 때 시작된다. |
| 2 | 관리자는 운영자 페이지에서 데이터 제어 메뉴를 선택한다. |
| 3 | 시스템은 현재 사용자가 관리자인지 확인한다. |
| 4 | 관리자는 제어할 사용자 또는 게시글을 선택한다. |
| 5 | 관리자는 삭제, 차단, 복구 등의 관리 작업을 선택한다. |
| 6 | 시스템은 선택된 대상 데이터가 존재하는지 확인한다. |
| 7 | 시스템은 선택된 관리 작업을 메모리 데이터에 반영한다. |
| 8 | 시스템은 변경된 내용을 User.txt 또는 Post.txt에 갱신한다. |
| 9 | 이 Use case는 데이터 제어 결과가 정상적으로 저장되면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 3 | 3a. 관리자가 아닌 사용자가 접근한 경우<br>3a.1. 시스템은 접근 권한이 없다는 메시지를 띄운다.<br>3a.2. 메인 화면으로 돌아간다. |
| 6 | 6a. 선택한 사용자 또는 게시글이 존재하지 않는 경우<br>6a.1. 시스템은 대상 데이터를 찾을 수 없다는 메시지를 띄운다.<br>6a.2. 데이터 제어 화면으로 돌아간다. |
| 7 | 7a. 이미 삭제되었거나 차단된 대상인 경우<br>7a.1. 시스템은 이미 처리된 대상이라는 메시지를 띄운다.<br>7a.2. 데이터 제어 화면으로 돌아간다. |
| 8 | 8a. 변경 내용을 파일에 저장하지 못한 경우<br>8a.1. 시스템은 데이터 갱신 실패 메시지를 띄운다.<br>8a.2. 변경 이전 상태로 복구한다. |
| RELATED INFORMATION |  |
| Performance | ≤ 3 Seconds |
| Frequency | 관리자가 필요할 때마다 |
| Concurrency | None |
| Due Date |  |

### 2.2.12 Use Case #12 : Answer Registration

| Use Case #12 : Answer Registration |  |
| --- | --- |
| GENERAL CHARACTERISTICS |  |
| Summary | 사용자가 등록된 질문 게시글에 대해 자신의 자취 경험과 생활 노하우를 바탕으로 답변을 작성하는 기능 |
| Scope | K-Living Solution |
| Level | User Level |
| Author | 김재원 |
| Last Update | May. 8. 2026  |
| Status | Analysis |
| Primary Actor | User |
| Preconditions | 사용자는 로그인된 상태여야 한다. 답변을 작성할 질문 게시글이 존재해야 한다. |
| Trigger | 사용자가 질문 상세 화면에서 답변 내용을 작성한 뒤 등록 버튼을 눌렀을 때 |
| Success Post Condition | 등록된 답변이 해당 질문 게시글에 연결되어 Post.txt에 저장되고, 질문자가 채택할 수 있는 답변 목록에 추가된다. |
| Failed Post Condition | 답변이 저장되지 않으며, 질문자는 해당 답변을 채택할 수 없다. |
| MAIN SUCCESS SCENARIO |  |
| Step | Action |
| 1 | 이 Use case는 사용자가 특정 질문에 답변을 작성하려고 할 때 시작된다. |
| 2 | 사용자는 질문 목록에서 답변하고 싶은 게시글을 선택한다. |
| 3 | 시스템은 질문 게시글의 상세 내용을 보여준다. |
| 4 | 사용자는 답변 작성 영역에 자신의 자취 경험과 해결 방법을 입력한다. |
| 5 | 사용자는 답변 등록 버튼을 누른다. |
| 6 | 시스템은 답변 내용이 올바르게 입력되었는지 확인한다. |
| 7 | 시스템은 답변 데이터를 해당 질문 게시글과 연결한다. |
| 8 | 시스템은 답변 정보를 Post.txt에 저장한다. |
| 9 | 시스템은 질문 상세 화면의 답변 목록을 갱신한다. |
| 10 | 이 Use case는 답변이 성공적으로 등록되면 끝난다. |
| EXTENSION SCENARIOS |  |
| Step | Branching Action |
| 2 | 2a. 선택한 질문 게시글이 존재하지 않는 경우<br>2a.1. 시스템은 게시글을 찾을 수 없다는 메시지를 띄운다.<br>2a.2. 질문 목록 화면으로 돌아간다. |
| 4 | 4a. 답변 내용이 입력되지 않은 경우<br>4a.1. 시스템은 답변 내용을 입력하라는 메시지를 띄운다.<br>4a.2. 답변 작성 화면으로 돌아간다. |
| 6 | 6a. 답변 글자 수가 제한 범위를 초과한 경우<br>6a.1. 시스템은 글자 수 제한 메시지를 띄운다.<br>6a.2. 답변 작성 화면으로 돌아간다. |
| 7 | 7a. 이미 답변 채택이 완료된 질문인 경우<br>7a.1. 시스템은 이미 종료된 질문이라는 메시지를 띄운다.<br>7a.2. 질문 상세 화면으로 돌아간다. |
| 8 | 8a. Post.txt에 답변을 저장하지 못한 경우<br>8a.1. 시스템은 답변 등록 실패 메시지를 띄운다.<br>8a.2. 답변 작성 화면으로 돌아간다. |
| RELATED INFORMATION |  |
| Performance | ≤ 3 Seconds |
| Frequency | 알 수 없음 |
| Concurrency | None |
| Due Date |  |
