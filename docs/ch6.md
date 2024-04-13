# 6장. 서블릿/JSP를 활용해 동적인 웹 어플리케이션 개발하기

서블릿이 HTTP 지원과 관련해 많은 부분을 제공하고 있지만 서블릿만으로는 웹 어플리케이션을 
빠르게 개발하는 데 한계가 있다. 단점을 보완해 개발하도록 프레임워크를 만들 수 있다.

자바 뿐만 아니라 대부분 언어가 제공하는 웹 프레임워크는 MVC(Model-View-Controller) 패턴을 기반으로 한다.
MVC 패턴 기반으로 프레임워크를 만들며 MVC에 대한 개념을 경험해보자.

## 6.1 서블릿/JSP로 회원관리 기능 다시 개발하기

앞서 개발한 ListUserController는 HTML을 StringBuilder를 이용해 동적으로 생성하지만,
6장에서 ListUserServlet은 JSP 파일로 위임한다. 서블릿 또한 동적으로 HTML을 생성하기 위해서는 기존과 마찬가지 방식으로 프로그래밍해야한다.
이런 서블릿의 문제를 극복하기 위해 등장한 것이 JSP이다.

JSP는 정적인 HTML은 그대로 두고 동적으로 변경된 부분만 JSP 구문을 활용해 프로그래밍으로 구현하면 된다.

웹 어플리케이션의 요구사항의 복잡도가 증가하면서 많은 로직이 JSP에 자바 코드로 구현되면서 유지보수가 힘들어지게 되었고,
이를 극복하기 위해 JSTL(JavaServer Pages Standard Tag Library)과 EL(Expression Language)이 등장하게 되었다.
JSP의 복잡도를 낮춰 유지보수를 쉽게 하자는 목적으로 MVC 패턴을 적용한 프레임워크가 등장하게 되었다.

JSTL과 EL을 활용하면 JSP에서 자바 구문을 완전히 제거할 수 있다.
완벽히 이를 제거하기 위해선 추가적으로 `JSP가 출력할 데이터를 전달해줄 컨트롤러`가 필요하다.
즉, MVC 패턴 기반으로 개발해야 JSP에서 자바 구문을 완전히 제거할 수 있다.

## 6.2 세션 요구사항 및 실습

HTTP는 무상태 프로토콜로서, 클라이언트와 서버가 연결된 후 상태를 유지할 수 없다.

그래서 사용하는 방법이 Cookie Header를 이용하는 방법인데, Set-Cookie 헤더를 통해 설정이 가능하다.
하지만 쿠키는 보안상 취약하다는 문제가 있다. 따라 쿠키에 비밀번호나 이메일 등 개인 정보를 전달하는 것은 부적합하다.

이런 쿠키의 단점을 보완하기 위해 세션이 등장한다. 세션은 상태 값으로 유지할 정보를 브라우저가 아닌 `서버에 저장`한다.
서버에 저장된 후 `클라이언트마다 고유한 아이디를 발급`해 이를 Set-Cookie 헤더를 통해 전달한다.

HTTP에서 상태를 유지하는 방법은 쿠키 밖에 없다. 세션이 상태 데이터를 서버에서 관리할 뿐 상태를 유지하는 방법은 쿠키를 사용한다.

### 6.2.1 요구사항

Http Session API 중 구현할 메서드는 getId(), setAttribute(), getAttribute(), removeAttribute(), invalidate()이다.
HttpSession의 가장 중요하고 핵심이 되는 메서드이다.

- String getId()
  - 현재 세션에 할당되어 있는 고유한 세션 아이디 반환
- void setAttribute(String name, Object value)
  - 현재 세션에 value 인자로 전달되는 객체를 name 인자 이름으로 저장
- Object getAttribute(String name)
  - 현재 세션에 name 인자로 전달되는 이름으로 저장된 객체 반환
- void removeAttribute(String name)
  - 현재 세션에 name 인자로 전달되는 이름으로 저장된 객체 삭제
- void invalidate()
  - 현재 세션에 저장되어 있는 모든 값을 삭제

