# 5장. 웹 서버 리팩토링, 서블릿 컨테이너와 서블릿의 관계

리팩토링을 하는데 있어 리팩토링이 필요한 시점과 종료해야하는 시점을 판단해야 하는
능력이 중요하다. 다른 사람이 구현한 코드를 많이 읽어보고 소스 코드를 직접 구현해가며 경험을 쌓아보자

## 웹 서버 리팩토링 실습

### 요청 데이터를 처리하는 로직을 별도의 클래스로 분리한다

클라이언트의 요청 데이터에서 요청 라인을 읽고, 헤더를 읽는 로직을 별도의 클래스로 분리한다.

해당 클래스는 단순히 요청 데이터를 읽어 사용하기 쉬운 형태로 분리하는 역할을 담당하고,
실제 사용은 RequestHandler에서 담당한다.

