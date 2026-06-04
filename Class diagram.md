# 1. Introduction

## 1.1 Summary
혼자 사는 사람들이 늘어나면서 자취 생활 중 겪는 문제도 다양해지고 있다. 처음 자취를 시작한 사람들은 월세, 공과금, 청소, 식재료 보관, 생활용품 구매처럼 사소하지만 막상 해결하기 어려운 문제들을 자주 겪는다. 반면 오래 자취한 사람들은 이런 문제를 해결한 경험이 있지만, 이를 체계적으로 공유할 수 있는 구조는 부족하다.
K-Living Solution은 이러한 문제를 해결하기 위한 자취생 지식 공유 시스템이다. 사용자는 질문을 등록할 때 보상 포인트를 설정하고, 다른 사용자는 자신의 경험을 바탕으로 답변을 작성한다. 질문자가 답변을 채택하면 보상 포인트가 답변자에게 지급되며, 채택된 답변은 이후 다른 사용자가 참고할 수 있는 정보로 남는다.

## 1.2 Important Points of Design
1. 포인트 보상을 통한 답변 품질 향상

2. 자취 경력과 누적 포인트를 활용한 사용자 라벨링

3. User.txt, Post.txt 기반의 파일 데이터 관리

4. 질문 등록, 답변 작성, 답변 채택 과정의 명확한 흐름 설계

5. 관리자 기능을 통한 사용자 및 게시글 관리

6. 채택된 답변의 아카이브 저장과 재사용 구조 마련

# 2. Class diagram

<img width="2043" height="1301" alt="image" src="https://github.com/user-attachments/assets/f3cdbd7c-abfa-413d-914b-c4b97ba4f829" />

## 1) User

User 클래스는 K-Living Solution을 사용하는 일반 사용자를 나타내는 클래스이다. 사용자는 회원가입을 통해 계정 정보를 등록하고, 로그인 후 질문 등록, 답변 등록, 답변 채택, 프로필 조회 등의 기능을 사용할 수 있다. 또한 현재 포인트와 누적 포인트는 질문 등록과 답변 채택 과정에서 변경되며, 사용자 라벨과 등급 판단에 활용된다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| userId | String | 사용자의 고유 ID |
| password | String | 로그인에 필요한 비밀번호 |
| nickname | String | 시스템에서 표시되는 사용자 이름 |
| experienceYear | int | 사용자의 자취 경력 |
| currentPoint | int | 질문 등록 시 사용할 수 있는 현재 포인트 |
| totalPoint | int | 사용자가 누적해서 획득한 전체 포인트 |
| userLabel | String | 자취 경력과 누적 포인트에 따라 부여되는 사용자 라벨 |
| isBlocked | boolean | 관리자가 차단한 사용자인지 나타내는 상태값 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| User(userId: String, password: String, nickname: String, experienceYear: int) | 사용자 객체를 생성하는 생성자 |
| getUserId(): String | 사용자의 ID를 반환 |
| getPassword(): String | 사용자의 비밀번호를 반환 |
| getNickname(): String | 사용자의 닉네임을 반환 |
| getExperienceYear(): int | 사용자의 자취 경력을 반환 |
| getCurrentPoint(): int | 사용자의 현재 포인트를 반환 |
| getTotalPoint(): int | 사용자의 누적 포인트를 반환 |
| getUserLabel(): String | 사용자에게 부여된 라벨을 반환 |
| isBlocked(): boolean | 사용자가 차단 상태인지 확인 |
| setCurrentPoint(point: int): void | 사용자의 현재 포인트를 설정 |
| setTotalPoint(point: int): void | 사용자의 누적 포인트를 설정 |
| setUserLabel(label: String): void | 사용자의 라벨을 설정 |
| setBlocked(blocked: boolean): void | 사용자의 차단 여부를 설정 |
| canUsePoint(point: int): boolean | 사용자가 특정 포인트를 사용할 수 있는지 확인 |
| decreasePoint(point: int): void | 사용자의 현재 포인트를 차감 |
| increasePoint(point: int): void | 사용자의 현재 포인트를 증가 |

## 2) Administrator

Administrator 클래스는 시스템 관리자를 나타내는 클래스이다. 관리자는 일반 사용자 기능을 포함하면서 추가적으로 시스템 통계 확인, 사용자 차단, 게시글 삭제, 게시글 복구 등의 관리 기능을 수행한다. 따라서 Administrator는 User 클래스를 상속받도록 설계하였다.


### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| adminId | String | 관리자 식별 ID |
| authorityLevel | int | 관리자의 권한 수준 |
| managedUserCount | int | 관리자가 확인할 수 있는 전체 사용자 수 |
| managedPostCount | int | 관리자가 확인할 수 있는 전체 게시글 수 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| Administrator(userId: String, password: String) | 관리자 객체를 생성하는 생성자 |
| viewStatistics(): void | 시스템 통계 정보를 확인 |
| blockUser(userId: String): void | 특정 사용자를 차단 |
| unblockUser(userId: String): void | 차단된 사용자를 해제 |
| deletePost(postId: String): void | 특정 게시글을 삭제 상태로 변경 |
| restorePost(postId: String): void | 삭제된 게시글을 복구 |

## 3) Post

Post 클래스는 사용자가 등록한 질문 게시글을 나타내는 클래스이다. 질문자는 게시글 작성 시 제목, 내용, 카테고리, 보상 포인트를 입력한다. 보상 포인트는 답변 채택 전까지 예치된 상태로 유지되며, 답변이 채택되면 해당 포인트가 답변자에게 지급된다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| postId | String | 질문 게시글의 고유 ID |
| title | String | 질문 게시글 제목 |
| content | String | 질문 게시글 내용 |
| category | String | 질문의 주제 또는 분류 |
| rewardPoint | int | 답변 채택 시 지급될 보상 포인트 |
| writerId | String | 질문 작성자의 ID |
| isAdopted | boolean | 답변 채택이 완료되었는지 나타내는 상태값 |
| selectedAnswerId | String | 채택된 답변의 ID |
| isDeleted | boolean | 관리자가 삭제한 게시글인지 나타내는 상태값 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| Post(postId: String, title: String, content: String, category: String, rewardPoint: int, writerId: String) | 질문 게시글 객체를 생성하는 생성자 |
| getPostId(): String | 게시글 ID를 반환 |
| getTitle(): String | 게시글 제목을 반환 |
| getContent(): String | 게시글 내용을 반환 |
| getCategory(): String | 게시글 카테고리를 반환 |
| getRewardPoint(): int | 보상 포인트를 반환 |
| getWriterId(): String | 질문 작성자의 ID를 반환 |
| isAdopted(): boolean | 게시글의 답변 채택 여부를 확인 |
| isDeleted(): boolean | 게시글의 삭제 여부를 확인 |
| addAnswer(answer: Answer): void | 게시글에 답변을 추가 |
| selectAnswer(answerId: String): void | 특정 답변을 채택 답변으로 설정 |
| markDeleted(): void | 게시글을 삭제 상태로 변경 |
| restore(): void | 삭제된 게시글을 복구 |

## 4) Answer

Answer 클래스는 질문 게시글에 등록되는 답변을 나타내는 클래스이다. 사용자는 질문 상세 화면에서 답변을 작성할 수 있으며, 질문자는 여러 답변 중 하나를 채택할 수 있다. 채택된 답변은 보상 포인트 지급 대상이 되고, 이후 Archive에 저장될 수 있다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| answerId | String | 답변의 고유 ID |
| targetPostId | String | 답변이 연결된 질문 게시글 ID |
| writerId | String | 답변 작성자의 ID |
| content | String | 답변 내용 |
| isSelected | boolean | 해당 답변이 채택되었는지 나타내는 상태값 |
| isDeleted | boolean | 관리자가 삭제한 답변인지 나타내는 상태값 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| Answer(answerId: String, targetPostId: String, writerId: String, content: String) | 답변 객체를 생성하는 생성자 |
| getAnswerId(): String | 답변 ID를 반환 |
| getTargetPostId(): String | 답변이 연결된 게시글 ID를 반환 |
| getWriterId(): String | 답변 작성자의 ID를 반환 |
| getContent(): String | 답변 내용을 반환 |
| isSelected(): boolean | 답변의 채택 여부를 확인 |
| isDeleted(): boolean | 답변의 삭제 여부를 확인 |
| select(): void | 답변을 채택 상태로 변경 |
| markDeleted(): void | 답변을 삭제 상태로 변경 |
| restore(): void | 삭제된 답변을 복구 |

## 5) PointTransaction

PointTransaction 클래스는 질문 등록과 답변 채택 과정에서 발생하는 포인트 거래를 관리하는 클래스이다. 질문 등록 시 질문자의 현재 포인트에서 보상 포인트를 차감하고, 답변 채택 시 예치된 포인트를 답변자의 현재 포인트와 누적 포인트에 반영한다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| transactionId | String | 포인트 거래의 고유 ID |
| postId | String | 포인트 거래가 발생한 질문 게시글 ID |
| questionerId | String | 질문 작성자의 ID |
| answererId | String | 채택된 답변 작성자의 ID |
| pointAmount | int | 거래되는 포인트 양 |
| transactionStatus | String | 거래 성공, 실패, 대기 등의 상태 |
| resultMessage | String | 사용자에게 출력할 거래 결과 메시지 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| PointTransaction(transactionId: String, postId: String, questionerId: String, pointAmount: int) | 포인트 거래 객체를 생성하는 생성자 |
| depositPoint(questioner: User, amount: int): boolean | 질문 등록 시 질문자의 포인트를 예치 |
| transferPoint(answerer: User, amount: int): boolean | 답변 채택 시 답변자에게 포인트를 지급 |
| rollback(questioner: User, answerer: User): void | 파일 저장 실패 등 오류 발생 시 포인트 거래 이전 상태로 복구 |
| makeResultMessage(): String | 포인트 정산 결과 메시지를 생성 |
| getTransactionStatus(): String | 거래 상태를 반환 |
| getResultMessage(): String | 거래 결과 메시지를 반환 |

## 6) SystemManager

SystemManager 클래스는 K-Living Solution의 전체 흐름을 제어하는 중심 클래스이다. 회원가입, 로그인, 질문 등록, 답변 등록, 답변 채택, 포인트 정산, 관리자 통계, 데이터 제어 기능은 모두 SystemManager를 통해 실행된다. 또한 FileManager, LabelManager, PointTransaction 등 다른 클래스와 연결되어 시스템 내부 기능을 통합적으로 관리한다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| loginUser | User | 현재 로그인된 사용자 |
| userList | List&lt;User&gt; | 시스템에 등록된 사용자 목록 |
| postList | List&lt;Post&gt; | 시스템에 등록된 질문 게시글 목록 |
| answerList | List&lt;Answer&gt; | 시스템에 등록된 답변 목록 |
| archiveList | List&lt;Archive&gt; | 채택된 질문과 답변이 저장된 아카이브 목록 |
| fileManager | FileManager | 파일 입출력을 담당하는 객체 |
| labelManager | LabelManager | 사용자 라벨링을 담당하는 객체 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| SystemManager() | 시스템 관리자 객체를 생성하는 생성자 |
| loadData(): void | User.txt, Post.txt의 데이터를 메모리로 불러옴 |
| registerUser(userId: String, password: String, nickname: String, experienceYear: int): boolean | 회원가입 기능 수행 |
| login(userId: String, password: String): boolean | 로그인 기능 수행 |
| logout(): void | 현재 로그인된 사용자의 세션 종료 |
| showProfile(userId: String): User | 사용자 프로필 정보를 조회 |
| createPost(title: String, content: String, category: String, rewardPoint: int): boolean | 질문 게시글 등록과 보상 포인트 예치 수행 |
| registerAnswer(postId: String, content: String): boolean | 특정 질문에 답변 등록 |
| selectAnswer(postId: String, answerId: String): PointTransaction | 답변 채택과 포인트 정산 수행 |
| showTransactionResult(transaction: PointTransaction): String | 포인트 정산 결과를 사용자에게 출력 |
| getSystemStatistics(): StatisticsReport | 관리자용 시스템 통계 정보 생성 |
| controlUser(userId: String, command: String): boolean | 사용자 차단, 차단 해제 등의 관리 명령 수행 |
| controlPost(postId: String, command: String): boolean | 게시글 삭제, 복구 등의 관리 명령 수행 |
| saveData(): void | 메모리상의 변경 데이터를 파일에 저장 |
| updateData(): void | 메모리상의 변경 데이터를 업데이트 및 갱신 |

## 7) FileManager

FileManager 클래스는 실제 데이터베이스 대신 사용되는 User.txt, Post.txt 파일을 관리하는 클래스이다. 프로그램 실행 시 파일 데이터를 메모리로 불러오고, 회원가입, 질문 등록, 답변 등록, 답변 채택, 관리자 제어 등으로 데이터가 변경될 때 파일에 저장하거나 갱신한다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| userFilePath | String | 사용자 정보가 저장되는 User.txt 경로 |
| postFilePath | String | 게시글 및 답변 정보가 저장되는 Post.txt 경로 |
| userData | String | 파일에서 읽어온 사용자 데이터 |
| postData | String | 파일에서 읽어온 게시글 및 답변 데이터 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| FileManager(userFilePath: String, postFilePath: String) | 파일 관리자 객체를 생성하는 생성자 |
| loadUsers(): List&lt;User&gt; | User.txt에서 사용자 정보를 읽어 사용자 목록으로 변환 |
| loadPosts(): List&lt;Post&gt; | Post.txt에서 질문 게시글 정보를 읽어 게시글 목록으로 변환 |
| loadAnswers(): List&lt;Answer&gt; | Post.txt에서 답변 정보를 읽어 답변 목록으로 변환 |
| saveUser(user: User): boolean | 신규 사용자 정보를 User.txt에 저장 |
| savePost(post: Post): boolean | 신규 게시글 정보를 Post.txt에 저장 |
| saveAnswer(answer: Answer): boolean | 신규 답변 정보를 Post.txt에 저장 |
| updateUser(user: User): boolean | 변경된 사용자 정보를 User.txt에 갱신 |
| updatePost(post: Post): boolean | 변경된 게시글 정보를 Post.txt에 갱신 |
| updateAnswer(answer: Answer): boolean | 변경된 답변 정보를 Post.txt에 갱신 |
| validateUserData(line: String): boolean | 사용자 데이터 형식이 올바른지 검사 |
| validatePostData(line: String): boolean | 게시글 데이터 형식이 올바른지 검사 |

## 8) LabelManager

LabelManager 클래스는 사용자의 자취 경력과 누적 포인트를 기준으로 사용자 라벨을 부여하는 클래스이다. 사용자 라벨은 프로필 화면과 답변 화면에 표시되어 답변자의 신뢰도를 판단하는 기준으로 사용된다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| experienceYear | int | 라벨 판단에 사용되는 자취 경력 |
| totalPoint | int | 라벨 판단에 사용되는 누적 포인트 |
| labelRule | String | 라벨 부여 기준 |
| resultLabel | String | 최종적으로 사용자에게 부여된 라벨 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| assignLabel(experienceYear: int, totalPoint: int): String | 자취 경력과 누적 포인트를 기준으로 최종 라벨을 부여 |
| assignExperienceLabel(experienceYear: int): String | 자취 경력을 기준으로 경험 라벨을 부여 |
| assignPointGrade(totalPoint: int): String | 누적 포인트를 기준으로 등급 라벨을 부여 |

## 9) Archive

Archive 클래스는 채택된 질문과 답변을 저장하는 클래스이다. 질문자가 답변을 채택하면 해당 질문과 답변은 아카이브에 저장되어 이후 다른 초보 자취생들이 참고할 수 있는 데이터가 된다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| archiveId | String | 아카이브 데이터의 고유 ID |
| postId | String | 저장된 질문 게시글 ID |
| answerId | String | 채택된 답변 ID |
| category | String | 아카이브 분류 |
| keyword | String | 검색 또는 분류에 활용할 키워드 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| Archive(archiveId: String, postId: String, answerId: String, category: String, keyword: String) | 아카이브 객체를 생성하는 생성자 |
| archiveSelectedAnswer(post: Post, answer: Answer): void | 채택된 질문과 답변을 아카이브에 저장 |
| searchArchive(keyword: String): List&lt;Archive&gt; | 키워드로 아카이브 목록을 검색하여 반환 |

10) StatisticsReport

StatisticsReport 클래스는 관리자가 확인할 수 있는 시스템 통계 정보를 계산하고 저장하는 클래스이다. 전체 사용자 수, 게시글 수, 답변 수, 채택된 답변 수, 총 포인트 유통량 등을 계산하여 관리자 화면에 출력할 수 있도록 한다.

### (1) Attributes

| Attribute | Type | Description |
| :--- | :--- | :--- |
| totalUserCount | int | 전체 사용자 수 |
| totalPostCount | int | 전체 게시글 수 |
| totalAnswerCount | int | 전체 답변 수 |
| selectedAnswerCount | int | 채택된 답변 수 |
| totalPointFlow | int | 시스템 내 총 포인트 유통량 |

### (2) Methods

| Method | Description |
| :--- | :--- |
| calculate(userList: List&lt;User&gt;, postList: List&lt;Post&gt;, answerList: List&lt;Answer&gt;): void | 사용자, 게시글, 답변 데이터를 바탕으로 통계 정보를 계산 |
| getTotalUserCount(): int | 전체 사용자 수를 반환 |
| getTotalPostCount(): int | 전체 게시글 수를 반환 |
| getTotalAnswerCount(): int | 전체 답변 수를 반환 |
| getSelectedAnswerCount(): int | 채택된 답변 수를 반환 |
| getTotalPointFlow(): int | 총 포인트 유통량을 반환 |
