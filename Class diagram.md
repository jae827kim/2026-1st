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

# 1) User

(1) Attributes

Attribute	Type	Description

userId	String	사용자의 고유 ID

password	String	로그인에 필요한 비밀번호

nickname	String	시스템에서 표시되는 사용자 이름

experienceYear	int	사용자의 자취 경력

currentPoint	int	질문 등록 시 사용할 수 있는 현재 포인트

totalPoint	int	사용자가 누적해서 획득한 전체 포인트

userLabel	String	자취 경력과 누적 포인트에 따라 부여되는 사용자 라벨

isBlocked	boolean	관리자가 차단한 사용자인지 나타내는 상태값

(2) Methods

Method	Description

User(userId: String, password: String, nickname: String, experienceYear: int)	사용자 객체를 생성하는 생성자

getUserId(): String	사용자의 ID를 반환

getPassword(): String	사용자의 비밀번호를 반환

getNickname(): String	사용자의 닉네임을 반환

getExperienceYear(): int	사용자의 자취 경력을 반환

getCurrentPoint(): int	사용자의 현재 포인트를 반환

getTotalPoint(): int	사용자의 누적 포인트를 반환

getUserLabel(): String	사용자에게 부여된 라벨을 반환

isBlocked(): boolean	사용자가 차단 상태인지 확인

setCurrentPoint(point: int): void	사용자의 현재 포인트를 설정

setTotalPoint(point: int): void	사용자의 누적 포인트를 설정

setUserLabel(label: String): void	사용자의 라벨을 설정

setBlocked(blocked: boolean): void	사용자의 차단 여부를 설정

canUsePoint(point: int): boolean	사용자가 특정 포인트를 사용할 수 있는지 확인

decreasePoint(point: int): void	사용자의 현재 포인트를 차감

increasePoint(point: int): void	사용자의 현재 포인트를 증가
