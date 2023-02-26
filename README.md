# 키다리스튜디오 코딩테스트 
## 예약시스템 구축
_2023년 6월 20일 (월) ~ 6월 27일 (월) 오후 4시_
***

### 개발언어
- Java 
  - openjdk version "19.0.2"

### 프레임워크
- Spring Framework
  - 3.0.2

### RDBMS
- h2

### Entity

### 1. Lecture
|ID|CAPACITY|CONTENT|PLACE|SPEAKER|STARTTIME|
|----|-----|-------|-------|-------|-------|
|5|139|A Summer Bird-Cage|245 행신동|오 우진|2023-02-22 03:08:58.553628|
### 2. Apply
|ID|EMPLOYEE_NUMBER|IS_VALID|LECTURE_ID|
|----|---------|-------|-------|
|1|50000|TRUE|5|

### 동시성 이슈 
Service 단위로 Transaction을 묶어 해결 

### 단위테스트 
데이터가 없을 때 Mock 데이터를 추가

### 데이터 일관성 고려 
고려완료 

### 응집도는 높이고
- Serivce 클래스와 Repository 클래스의 용도를 완전히 구분하고 각각의 역할을 벗어나는 용도로 전혀 사용하지 않았습니다 

### 결합도는 낮추고 
- Lecture Entity 관련 코드는 Lecture이 앞에 들어간 클래스들로 나눈다 
- Lecture와 Apply 코드를 나눌 수 있습니다 
