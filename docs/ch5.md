# 5장. 웹 서버 리팩토링, 서블릿 컨테이너와 서블릿의 관계

리팩토링을 하는데 있어 리팩토링이 필요한 시점과 종료해야하는 시점을 판단해야 하는
능력이 중요하다. 다른 사람이 구현한 코드를 많이 읽어보고 소스 코드를 직접 구현해가며 경험을 쌓아보자

## 웹 서버 리팩토링 실습

### 요청 데이터를 처리하는 로직을 별도의 클래스로 분리한다

클라이언트의 요청 데이터에서 요청 라인을 읽고, 헤더를 읽는 로직을 별도의 클래스로 분리한다.

해당 클래스는 단순히 요청 데이터를 읽어 사용하기 쉬운 형태로 분리하는 역할을 담당하고,
실제 사용은 RequestHandler에서 담당한다.

### 응답 데이터를 처리하는 로직을 별도의 클래스로 분리한다

응답 데이터의 상태에 따라 적절한 HTTP 헤더를 처리하는 부분을 추가한다.
HTML, CSS, 자바스크립트 파일을 읽어 반환하는 부분과 302 상태 코드를 처리하는 부분이
가능해야하며, 쿠키 추가와 같이 HTTP 헤더에 임의의 값을 추가할 수 있어야한다.

### 다형성을 활용해 클라이언트 요청 URL에 대한 분기 처리를 제거

RequestHandler 내 run 메서드의 단점은 기능이 추가될 때마다 else if 절이 추가되는 구조로 구현되어 있는 것이다.
이는 객체 지향 설계 원칙 중 OCP(Operation-Closed Principle)를 위반하는 것이다.

Controller라는 interface를 통해 새로운 기능이 추가된다면 더이상 run 메서드에 분기문을 추가할 필요가 없어졌다.
RequestMapping에 URL과 Controller를 매핑하는 방식으로 기능을 추가할 수 있게 되었다.

한발 더 나아가 HTTP 메서드에 따라 다른 처리를 할 수 있도록 추상 클래스를 추가할 수도 있다.

### HTTP 웹 서버의 문제점

- HTTP 요청과 응답 헤더, 본문 처리와 같은 데 시간을 투자함으로써 서비스 로직에 투자하는 시간이 줄어든다
- 동적인 HTML을 지원하는데 한계가 있다
- 사용자가 입력한 데이터가 서버를 재시작하면 사라진다

## 5.3 서블릿 컨테이너, 서블릿/JSP를 활용한 문제 해결

앞서 언급한 문제 중 2가지를 해결하기 위해 자바 진영에서 표준으로 정한 것이 서블릿 컨테이너와 서블릿/JSP이다.

서블릿은 앞서 구현한 Controller, HttpRequest, HttpResponse를 추상화하여 인터페이스로 정의해 놓은 표준이다.
즉, 서블릿은 `HTTP의 클라이언트 요청과 응답에 대한 표준`이고, 서블릿 컨테이너는 `서블릿 표준에 대한 구현`을 담당하며
앞서 구현한 웹서버라고 볼 수 있다.

서블릿 컨테이너는 서버가 시작할 때 서블릿 인스턴스를 생성해, 요청 URL과 서블릿 인스턴스를 연결한다.
클라이언트에서 요청이 들어오면 요청 URL에 해당하는 서블릿을 찾아 작업을 위임한다.

### WebServletLauncher, HelloWorldServlet

WebServletLauncher 소스는 앞서 구현한 Http 웹서버의 WebServer와 같은 역할을 하며, 웹 리소스가 위치하는 디렉토리와 자원을 접근할 때의 경로를 설정한다.
HelloWorldServlet 소스를 보면 앞서 구현한 Controller와 정확히 같은 역할을 하며 똑같은 방식으로 동작한다.

서블릿 컨테이너는 서버를 시작할 때 클래스패스에 있는 클래스 중 `HttpServlet을 상속하는 클래스`를 찾은 후,
`@WebServlet 어노테이션 값을 읽어 URL과 서블릿을 연결하는 Map을 생성`한다.

즉, 서블릿 컨테이너의 중요한 역할 중 하나는 서블릿 클래스의 인스턴스 생성, 요청 URL과 서블릿 인스턴스 매핑,
클라이언트 요청에 해당하는 서블릿을 찾은 후 서블릿에 작업을 위임하는 것이다. 이외에도 서블릿과 관련한
초기화와 소멸 작업도 담당한다.

### 서블릿 컨테이너가 동작하는 과정

1. 서블릿 컨테이너 시작
2. 클래스패스에 있는 Servlet 인터페이스를 구현하는 서블릿 클래스 탐색
3. @WebServlet 설정을 통해 요청 URL과 서블릿 매핑
4. 서블릿 인스턴스 생성
5. init() 메서드 호출해 초기화

서블릿 컨테이너는 서블릿의 생명주기를 관리한다고 볼 수 있다. 여러가지 컨테이너가 존재하지만,
기본적으로 컨테이너들은 `생명주기를 관리`하는 기능을 제공한다.

컨테이너가 관리하는 `객체의 인스턴스`는 개발자가 직접 생성하는 인스턴스가 아닌 `컨테이너에 의해 직접 관리`된다.
따라서 초기화, 소멸과 같은 작업을 위한 메서드를 인터페이스 규약으로 만들고 확장할 수 있도록 지원하는 것이다.

서블릿에서 알아야할 중요한 부분 중 하나는 `서블릿 컨테이너가 생성하는 서블릿 인스턴스의 갯수`이다.
HTTP 웹 서버 실습에서 RequestMapping의 Map을 보면 static으로 구현되어 있음을 살펴볼 수 있다.
즉, 서버가 시작할 때 한번 초기화되고 더이상 초기화하지 않고 재사용하게 된다. 서블릿 컨테이너가 시작할 때
한번 생성되면 모든 스레드가 같은 인스턴스를 재사용한다.



