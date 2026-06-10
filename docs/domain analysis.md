# 3. Domain analysis

아래의 그림은 K-Living Solution 시스템의 Domain Analysis에서 사용되는 주요 클래스들의 관계를 간단하게 나타낸 것이다.

![클래스 다이어그램](class.png)

본 Domain Analysis는 시스템의 실제 구현 구조보다는 K-Living Solution을 구성하는 핵심 개념 클래스와 이들 사이의 관계를 나타내는 데 중점을 두었다. 본 시스템은 실제 DB 서버를 사용하지 않고 Java File I/O를 통해 `User.txt`, `Post.txt` 파일에 데이터를 저장하므로, 파일 입출력과 데이터 동기화를 담당하는 클래스를 별도로 도출하였다.

K-Living Solution은 사용자가 질문을 등록하고, 다른 사용자가 답변을 작성하며, 질문자가 답변을 채택하면 보상 포인트가 답변자에게 전달되는 구조를 가진다. 따라서 사용자 정보, 질문 게시글, 답변, 포인트 거래, 파일 데이터 관리, 관리자 기능을 중심으로 Domain Class를 구성하였다.

## 3.1 Domain Class List

| Class Name | Description |
| --- | --- |
| User | 계정 정보, 자취 경력, 포인트 데이터를 관리하는 사용자 클래스이다. 질문 작성, 답변 작성, 답변 채택 등의 기능을 수행하는 주체이다. |
| Administrator | User를 상속받은 관리자 클래스이다. 일반 사용자 기능에 더해 시스템 통계 확인과 부적절한 데이터 제어 기능을 수행한다. |
| Post | 사용자가 등록한 질문 게시글 클래스이다. 질문의 제목, 내용, 카테고리, 보상 포인트, 작성자, 채택 상태를 관리한다. |
| Answer | 질문 게시글에 등록되는 답변 클래스이다. 답변 내용, 답변 작성자, 대상 질문, 채택 여부를 관리한다. |
| PointTransaction | 질문 등록 시 포인트 예치, 답변 채택 시 포인트 이전, 사용자 포인트 갱신 로직을 처리하는 클래스이다. |
| SystemManager | K-Living Solution의 전체 흐름을 제어하는 중심 클래스이다. 회원가입, 로그인, 질문 등록, 답변 등록, 답변 채택, 관리자 기능을 연결한다. |
| FileManager | `User.txt`, `Post.txt` 파일을 읽고 쓰는 클래스이다. Data Load, Data Save, Data Update 기능을 수행한다. |
| LabelManager | 사용자의 자취 경력과 누적 포인트를 기준으로 사용자 라벨을 부여하는 클래스이다. |
| Archive | 채택된 질문과 답변을 저장하여 이후 다른 사용자가 참고할 수 있도록 관리하는 클래스이다. |

## 3.2 Class Description

### 3.2.1 User

User는 K-Living Solution을 사용하는 일반 사용자 클래스이다. 사용자는 회원가입을 통해 계정 정보를 등록하고, 로그인 후 질문 등록, 답변 등록, 답변 채택 등의 기능을 사용할 수 있다. 또한 사용자의 자취 경력과 포인트 정보는 라벨링과 등급 판단에 사용된다.

| Attribute | Type | Description |
| --- | --- | --- |
| userId | String | 사용자의 고유 ID |
| password | String | 사용자 로그인에 필요한 비밀번호 |
| nickname | String | 시스템에서 표시되는 사용자 이름 |
| experienceYear | int | 사용자의 자취 경력 |
| currentPoint | int | 질문 등록 시 사용할 수 있는 현재 포인트 |
| totalPoint | int | 사용자가 누적해서 획득한 전체 포인트 |
| userLabel | String | 자취 경력이나 누적 포인트에 따라 부여되는 사용자 라벨 |
| isBlocked | boolean | 관리자가 차단한 사용자인지 나타내는 상태값 |

### 3.2.2 Administrator

Administrator는 User를 상속받은 관리자 클래스이다. 일반 사용자와 동일하게 로그인할 수 있으며, 추가적으로 시스템 통계 확인과 데이터 제어 기능을 수행한다. 관리자는 부적절한 게시글을 삭제하거나 규정 위반 사용자를 차단하여 커뮤니티의 건전성을 유지한다.

| Attribute | Type | Description |
| --- | --- | --- |
| adminId | String | 관리자 식별 ID |
| authorityLevel | int | 관리자 권한 수준 |
| managedUserCount | int | 관리자가 확인할 수 있는 전체 사용자 수 |
| managedPostCount | int | 관리자가 확인할 수 있는 전체 게시글 수 |

### 3.2.3 Post

Post는 사용자가 등록한 질문 게시글 클래스이다. 질문자는 질문을 작성할 때 보상 포인트를 함께 설정하며, 해당 포인트는 답변 채택 전까지 예치된 상태로 유지된다. 질문자가 답변을 채택하면 게시글의 채택 상태가 변경되고 보상 포인트가 답변자에게 전달된다.

| Attribute | Type | Description |
| --- | --- | --- |
| postId | String | 질문 게시글의 고유 ID |
| title | String | 질문 게시글 제목 |
| content | String | 질문 게시글 내용 |
| category | String | 질문의 주제 또는 분류 |
| rewardPoint | int | 답변 채택 시 지급될 보상 포인트 |
| writerId | String | 질문 작성자의 ID |
| isAdopted | boolean | 답변 채택이 완료되었는지 나타내는 상태값 |
| selectedAnswerId | String | 채택된 답변의 ID |
| isDeleted | boolean | 관리자가 삭제한 게시글인지 나타내는 상태값 |

### 3.2.4 Answer

Answer는 질문 게시글에 등록되는 답변 클래스이다. 사용자는 질문 상세 화면에서 답변을 작성할 수 있으며, 질문자는 여러 답변 중 하나를 채택할 수 있다. 채택된 답변은 보상 포인트 지급 대상이 되며, 아카이브에 저장될 수 있다.

| Attribute | Type | Description |
| --- | --- | --- |
| answerId | String | 답변의 고유 ID |
| targetPostId | String | 답변이 연결된 질문 게시글 ID |
| writerId | String | 답변 작성자의 ID |
| content | String | 답변 내용 |
| isSelected | boolean | 해당 답변이 채택되었는지 나타내는 상태값 |
| isDeleted | boolean | 관리자가 삭제한 답변인지 나타내는 상태값 |

### 3.2.5 PointTransaction

PointTransaction은 포인트 거래를 관리하는 클래스이다. 질문 등록 시 질문자의 현재 포인트에서 보상 포인트를 차감하고, 답변 채택 시 예치된 포인트를 답변자에게 지급한다. 포인트 이전이 완료되면 거래 결과를 생성하여 사용자에게 보여준다.

| Attribute | Type | Description |
| --- | --- | --- |
| transactionId | String | 포인트 거래의 고유 ID |
| postId | String | 포인트 거래가 발생한 질문 게시글 ID |
| questionerId | String | 질문 작성자의 ID |
| answererId | String | 채택된 답변 작성자의 ID |
| pointAmount | int | 거래되는 포인트 양 |
| transactionStatus | String | 거래 성공, 실패, 대기 등의 상태 |
| resultMessage | String | 사용자에게 출력할 거래 결과 메시지 |

### 3.2.6 SystemManager

SystemManager는 K-Living Solution의 전체 흐름을 제어하는 중심 클래스이다. 회원가입, 로그인, 질문 등록, 답변 등록, 답변 채택, 통계 확인, 데이터 제어 기능이 SystemManager를 통해 호출된다. 또한 FileManager, PointTransaction, LabelManager와 연결되어 시스템 내부 기능을 통합적으로 관리한다.

| Attribute | Type | Description |
| --- | --- | --- |
| loginUser | User | 현재 로그인된 사용자 |
| userList | List\<User> | 시스템에 등록된 사용자 목록 |
| postList | List\<Post> | 시스템에 등록된 질문 게시글 목록 |
| answerList | List\<Answer> | 시스템에 등록된 답변 목록 |
| fileManager | FileManager | 파일 입출력을 담당하는 객체 |
| labelManager | LabelManager | 사용자 라벨링을 담당하는 객체 |

### 3.2.7 FileManager

FileManager는 실제 DB 대신 사용되는 `User.txt`, `Post.txt` 파일을 관리하는 클래스이다. 프로그램 실행 시 파일 데이터를 메모리로 불러오고, 데이터가 변경되면 파일에 저장하거나 갱신한다. 따라서 Data Load, Data Save, Data Update Use Case와 직접적으로 연결된다.

| Attribute | Type | Description |
| --- | --- | --- |
| userFilePath | String | 사용자 정보가 저장되는 `User.txt` 경로 |
| postFilePath | String | 게시글 및 답변 정보가 저장되는 `Post.txt` 경로 |
| userData | String | 파일에서 읽어온 사용자 데이터 |
| postData | String | 파일에서 읽어온 게시글 데이터 |

### 3.2.8 LabelManager

LabelManager는 사용자의 자취 경력과 누적 포인트를 기준으로 라벨을 부여하는 클래스이다. 사용자의 라벨은 프로필과 답변 화면에 표시되어 답변자의 신뢰도를 판단하는 기준으로 사용된다.

| Attribute | Type | Description |
| --- | --- | --- |
| experienceYear | int | 라벨 판단에 사용되는 자취 경력 |
| totalPoint | int | 라벨 판단에 사용되는 누적 포인트 |
| labelRule | String | 라벨 부여 기준 |
| resultLabel | String | 최종적으로 사용자에게 부여된 라벨 |

### 3.2.9 Archive

Archive는 채택된 질문과 답변을 저장하는 클래스이다. 질문자가 답변을 채택하면 해당 질문과 답변은 아카이브에 저장되어 이후 다른 초보 자취생들이 참고할 수 있는 데이터가 된다. 이를 통해 K-Living Solution은 단순한 질의응답 시스템이 아니라 지식이 축적되는 시스템으로 동작한다.

| Attribute | Type | Description |
| --- | --- | --- |
| archiveId | String | 아카이브 데이터의 고유 ID |
| postId | String | 저장된 질문 게시글 ID |
| answerId | String | 채택된 답변 ID |
| category | String | 아카이브 분류 |
| keyword | String | 검색 또는 분류에 활용할 키워드 |

## 3.3 Class Relationship

| Relationship | Description |
| --- | --- |
| Administrator inherits User | 관리자는 일반 사용자 기능을 포함하면서 추가적인 관리 기능을 가진다. |
| User creates Post | 사용자는 질문 게시글을 작성할 수 있다. |
| User creates Answer | 사용자는 다른 사용자의 질문에 답변을 작성할 수 있다. |
| Post has Answer | 하나의 질문 게시글은 여러 개의 답변을 가질 수 있다. |
| Post uses PointTransaction | 질문 등록과 답변 채택 과정에서 보상 포인트 거래가 발생한다. |
| PointTransaction updates User | 포인트 거래 결과에 따라 사용자의 현재 포인트와 누적 포인트가 갱신된다. |
| SystemManager controls User, Post, Answer | 시스템의 주요 기능은 SystemManager를 통해 실행된다. |
| SystemManager uses FileManager | 시스템은 FileManager를 통해 텍스트 파일 데이터를 로드, 저장, 갱신한다. |
| LabelManager assigns label to User | LabelManager는 사용자의 자취 경력과 누적 포인트를 기준으로 라벨을 부여한다. |
| Archive stores selected Post and Answer | 채택된 질문과 답변은 Archive에 저장되어 재사용 가능한 지식 데이터가 된다. |
