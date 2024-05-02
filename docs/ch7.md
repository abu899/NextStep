# 7장. DB를 활용해 데이터를 영구적으로 저장하기

지금까지는 데이터가 서버를 재시작하면 사라지게 된다. 데이터를 영구적으로 저장하고
조회할 필요가 있는데 데이터베이스 서버를 도입해 해결할 수 있다.

자바 진영은 JDBC라는 표준을 통해 데이터베이스와의 통신을 담당한다.
JDBC는 인터페이스만 제공하는데, 통신을 위한 규약만 제공하고 구현체는 서비스를 제공하는 회사가 만든다.
이처럼 표준을 정의함으로써 데이터베이스에 대한 연결 설정만 변경해 다른 데이터베이스를 지원함으로써 소스코드의 변경을 최소화하고 있다.

## 7.1 회원 데이터를 DB에 저장하기 실습

### 7.1.1 실습 코드 리뷰 및 JDBC 복습

ql 파일을 읽어 DB 초기화를 하기 위해, ContextLoaderListener에서 초기화를 진행한다.
ContextLoaderListener는 ServletContextListener 인터페이스를 구현하며, @WebListener 어노테이션
설정을 통해 서블릿 컨테이너를 시작하면 contextInitialized() 메서드가 호출된다.
ServletContextListener의 초기화는 서블릿 초기화보다 먼저 진행된다.

자바에서는 DB 접근 로직처리를 담당하는 객체를 별도로 분리해 구현하는 것을 추천한다.
이런 객체를 DAO(Data Access Object)라고 부른다. 초기 UserDao에는 유저 추가와 조회밖에 없지만 구현할 소스가 많고 코드 중복이 존재한다.

또한 기존 DataBase 클래스 대신 UserDao를 사용하는 모든 곳은 SqlException을 처리하기 위한
try-catch 구문으로 감싸줘야 하는 단점이 존재한다.

## 7.2 DAO 리팩토링 실습

현재 UserDao의 문제는 중복 코드가 너무 많고 쿼리 하나를 실행하기 위해 구현해야할 코드가 너무 많다는 점이다.
반복적인 코드는 공통 라이브러리로 만들어 제거 가능하다.

중복코드 리팩토링을 위해 변화가 발생하는 부분(개발자가 구현해야 하는 부분)과 변화가 없는 부분(공통 라이브러리로 분리 가능한 부분)으로
분리해야한다. 개발자가 구현해야 할 부분은 SQL 쿼리, 쿼리 전달 인자, SELECT 문의 경우 조회 데이터 추출이다.
추가적으로 SQLException을 처리하는 코드도 중복이 발생하므로 이를 제거해야 한다.

Exception은 반드시 필요한 부분이지만 무분별하게 사용 시 가독성을 떨어뜨리게 된다.
컴파일타입 Exception(Checked exception)과 런타임 Exception(Unchecked exception)의 가이드라인은 다음과 같다.

- API를 사용하는 모든 곳에서 예외를 처리해야 하는가?
  - Checked Exception
- API를 사용하는 소수 중 이 예외를 처리 해야하는가?
  - Unchecked Exception
- 무언가 문제가 발생하고 이를 복구할 방법이 없나?
  - Unchecked Exception
- 위 질문에도 명확하지 않다면 Unchecked Exception으로 구현하고 문서화를 진행