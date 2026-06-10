# 1. Sequence Diagram

아래 그림들은 Conceptualization에서 표현한 기능들을 Sequence Diagram 으로 표현한 것으로 UI, SystemManager, FileManager, PointTransaction, LabelManager 등의 객체가 어떤 순서로 메시지를 주고받는지 보여준다.

## 1.1 Registration

<img width="1522" height="1018" alt="image" src="https://github.com/user-attachments/assets/19527a59-6030-4104-8194-79e6cf08ae1d" />

-----
Registration Sequence Diagram은 사용자가 K-Living Solution에 회원가입하는 과정을 나타낸다. 사용자는 회원가입 화면에서 ID, Password, 닉네임, 자취 경력 정보를 입력하고 회원가입 버튼을 누른다. RegistrationUI는 입력된 정보를 SystemManager의 registerUser() 메소드로 전달한다.

SystemManager는 먼저 입력값이 비어 있거나 자취 경력 값이 올바르지 않은지 검사한다. 입력값이 올바르지 않으면 오류 메시지를 반환하고 회원가입 화면에 머무른다. 입력값이 올바른 경우 FileManager를 통해 User.txt 파일에서 기존 사용자 목록을 불러오고, 동일한 ID가 이미 존재하는지 확인한다.

중복 ID가 존재하면 회원가입 실패 메시지를 출력한다. 중복 ID가 없으면 LabelManager를 통해 사용자의 자취 경력과 초기 포인트를 기준으로 라벨을 부여한다. 이후 SystemManager는 현재 포인트와 누적 포인트가 100P인 새로운 User 객체를 생성하고, FileManager의 saveUser() 메소드를 통해 User.txt에 저장한다. 저장이 성공하면 회원가입 성공 메시지를 출력하고 로그인 화면으로 이동한다. 저장에 실패하면 회원가입 실패 메시지를 출력하고 회원가입 화면을 유지한다.

## 1.2 Login and Profile Labeling

<img width="1659" height="906" alt="image" src="https://github.com/user-attachments/assets/82011e57-c507-4f4c-984e-b605f91eda16" />

-----
Login and Profile Labeling Sequence Diagram은 사용자가 로그인하고 프로필 라벨 정보를 불러오는 과정을 나타낸다. 사용자는 로그인 화면에서 ID와 Password를 입력한 뒤 로그인 버튼을 누른다. LoginUI는 입력된 로그인 정보를 SystemManager의 login() 메소드로 전달한다.

SystemManager는 FileManager를 통해 User.txt에서 사용자 목록을 불러온다. 이후 입력한 ID에 해당하는 사용자가 존재하는지 확인하고, 존재하는 경우 비밀번호가 일치하는지 검사한다. 등록되지 않은 ID이거나 비밀번호가 일치하지 않으면 로그인 실패 메시지를 반환한다. 또한 해당 사용자가 관리자에 의해 차단된 상태라면 접근 제한 메시지를 출력하고 로그인을 허용하지 않는다.

로그인에 성공하면 SystemManager는 로그인한 사용자의 자취 경력과 누적 포인트를 기준으로 LabelManager의 assignLabel() 메소드를 호출하여 사용자 라벨을 갱신한다. 갱신된 라벨 정보는 FileManager를 통해 User.txt에 반영된다. 이후 로그인한 사용자가 관리자이면 관리자 메인 화면으로 이동하고, 일반 사용자이면 일반 사용자 메인 화면으로 이동한다.

## 1.3 Post and reward

<img width="1634" height="872" alt="image" src="https://github.com/user-attachments/assets/24b765be-34c3-4d13-940c-c98257f6f29e" />

------
Post and Reward Sequence Diagram은 사용자가 질문을 등록하고 보상 포인트를 예치하는 과정을 나타낸다. 사용자는 질문 작성 화면에서 제목, 내용, 카테고리, 보상 포인트를 입력한 뒤 질문 등록 버튼을 누른다. PostWriteUI는 입력된 정보를 SystemManager의 createPost() 메소드로 전달한다.

SystemManager는 먼저 사용자가 로그인된 상태인지 확인하고, 질문 제목과 내용, 카테고리, 보상 포인트가 올바르게 입력되었는지 검사한다. 또한 사용자의 현재 포인트가 보상 포인트보다 크거나 같은지 확인한다. 로그인 상태가 아니거나 입력값이 올바르지 않거나 포인트가 부족한 경우 질문 등록은 실패하고 오류 메시지가 출력된다.

질문 등록이 가능한 경우 PointTransaction의 depositPoint() 메소드가 실행되어 질문자의 현재 포인트에서 보상 포인트가 차감된다. 차감된 포인트 정보는 FileManager를 통해 User.txt에 갱신된다. 이후 SystemManager는 새로운 Post 객체를 생성하고 FileManager의 savePost() 메소드를 통해 Post.txt에 저장한다. 게시글 저장이 성공하면 질문 목록 화면으로 이동한다. 만약 게시글 저장에 실패하면 PointTransaction의 rollback() 메소드를 통해 차감된 포인트를 복구하고 질문 등록 실패 메시지를 출력한다.

## 1.4 Answer Registration

<img width="1482" height="904" alt="image" src="https://github.com/user-attachments/assets/890c0c0f-613f-4dbd-abdb-213915e57c2a" />

------
Answer Registration Sequence Diagram은 사용자가 특정 질문 게시글에 답변을 등록하는 과정을 나타낸다. 사용자는 질문 목록에서 게시글을 선택하고 질문 상세 화면으로 이동한다. 이후 답변 내용을 입력하고 답변 등록 버튼을 누르면 PostDetailUI가 SystemManager의 registerAnswer() 메소드를 호출한다.

SystemManager는 먼저 사용자가 로그인된 상태인지 확인한다. 이후 FileManager를 통해 Post.txt에서 게시글과 답변 데이터를 불러오고, 답변을 작성하려는 게시글이 실제로 존재하는지 확인한다. 게시글이 존재하지 않으면 질문 목록 화면으로 돌아간다.

게시글이 존재하는 경우 답변 내용이 비어 있는지, 현재 로그인한 사용자가 질문 작성자 본인인지, 이미 답변 채택이 완료된 게시글인지 검사한다. 답변 내용이 비어 있거나, 질문 작성자 본인이 답변을 등록하려 하거나, 이미 채택이 완료된 질문이면 답변 등록을 허용하지 않는다. 모든 조건을 통과하면 SystemManager는 새로운 Answer 객체를 생성하고 FileManager의 saveAnswer() 메소드를 통해 Post.txt에 저장한다. 저장이 성공하면 질문 상세 화면의 답변 목록이 갱신된다.

## 1.5 Answer Selection and Transaction Result

<img width="2107" height="1182" alt="image" src="https://github.com/user-attachments/assets/ff2bd5cb-7c42-491c-bcbf-8b643d976aeb" />

-------
Answer Selection and Transaction Result Sequence Diagram은 질문자가 등록된 답변 중 하나를 채택하고 포인트 정산 결과를 확인하는 과정을 나타낸다. 질문자는 자신의 질문 상세 화면에서 채택할 답변을 선택한 뒤 채택 버튼을 누른다. PostDetailUI는 선택된 게시글 ID와 답변 ID를 SystemManager의 selectAnswer() 메소드로 전달한다.

SystemManager는 FileManager를 통해 Post.txt에서 게시글 및 답변 데이터를 불러오고, User.txt에서 사용자 데이터를 불러온다. 이후 채택하려는 게시글과 답변이 존재하는지 확인하고, 현재 로그인한 사용자가 해당 질문의 작성자인지 검사한다. 질문 작성자가 아닌 사용자가 답변 채택을 시도하면 권한 없음 메시지가 출력된다. 또한 이미 채택이 완료된 게시글이면 추가 채택을 허용하지 않는다.

채택이 가능한 경우 SystemManager는 답변 작성자를 찾고, PointTransaction의 transferPoint() 메소드를 실행하여 예치된 보상 포인트를 답변자에게 지급한다. 이때 답변자의 현재 포인트와 누적 포인트가 함께 증가한다. 이후 게시글은 채택 완료 상태로 변경되고, 선택된 답변은 채택 상태로 변경된다. 변경된 게시글, 답변, 사용자 정보는 FileManager를 통해 각각 Post.txt, User.txt에 갱신된다.

포인트 정산과 데이터 갱신이 완료되면 Archive의 archiveSelectedAnswer() 메소드가 실행되어 채택된 질문과 답변이 아카이브에 저장된다. 마지막으로 PointTransaction은 정산 결과 메시지를 생성하고, TransactionResultUI는 질문자에게 지급된 포인트, 답변자의 최종 포인트, 거래 성공 여부를 출력한다.

## 1.6 System Statistics

<img width="1622" height="885" alt="image" src="https://github.com/user-attachments/assets/ce93754b-9019-4b75-b91b-e6b2deb397e8" />

------
System Statistics Sequence Diagram은 관리자가 시스템 통계 정보를 확인하는 과정을 나타낸다. 관리자는 관리자 페이지에서 시스템 통계 메뉴를 선택하고, AdminPageUI는 SystemManager의 getSystemStatistics() 메소드를 호출한다.

SystemManager는 먼저 현재 로그인한 사용자가 관리자 권한을 가지고 있는지 확인한다. 관리자 권한이 없으면 접근 권한 없음 메시지를 출력하고 통계 기능을 실행하지 않는다. 관리자 권한이 확인되면 FileManager를 통해 User.txt에서 사용자 데이터를 불러오고, Post.txt에서 게시글과 답변 데이터를 불러온다.

이후 StatisticsReport 객체가 생성되고, calculate() 메소드가 실행된다. 이 메소드는 전체 사용자 수, 전체 게시글 수, 전체 답변 수, 채택된 답변 수, 총 포인트 유통량 등을 계산한다. 계산된 통계 정보는 SystemManager를 통해 AdminPageUI로 전달되고, 관리자는 화면에서 시스템 운영 상태와 포인트 흐름을 확인할 수 있다.

## 1.7 Data Control

<img width="1523" height="1183" alt="image" src="https://github.com/user-attachments/assets/318f69e9-51c7-40de-bee1-ebd1c4e45443" />

------
Data Control Sequence Diagram은 관리자가 사용자 또는 게시글을 제어하는 과정을 나타낸다. 관리자는 관리자 제어 화면에서 제어할 대상과 명령을 선택한다. 제어 명령은 사용자 차단, 사용자 차단 해제, 게시글 삭제, 게시글 복구 등이 될 수 있다. AdminControlUI는 선택된 대상 ID, 대상 유형, 명령을 SystemManager의 controlData() 메소드로 전달한다.

SystemManager는 먼저 현재 사용자가 관리자 권한을 가지고 있는지 확인한다. 관리자 권한이 없으면 접근 권한 없음 메시지를 출력한다. 관리자 권한이 있는 경우 대상 유형이 사용자라면 FileManager를 통해 User.txt에서 사용자 목록을 불러오고, 대상 사용자가 존재하는지 확인한다. 대상 사용자가 존재하면 명령에 따라 차단 또는 차단 해제를 수행하고, 변경된 정보를 User.txt에 갱신한다.

대상 유형이 게시글이라면 FileManager를 통해 Post.txt에서 게시글 목록을 불러오고, 대상 게시글이 존재하는지 확인한다. 대상 게시글이 존재하면 명령에 따라 삭제 또는 복구 처리를 수행하고, 변경된 게시글 정보를 Post.txt에 갱신한다. 모든 처리가 끝나면 처리 결과를 관리자 화면에 출력한다.
