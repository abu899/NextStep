# 9장. 두 번째 양파 껍질을 벗기기 위한 중간 점검

## 9.3 자체 점검 확인

### 9.3.1서블릿 컨테이너와 MVC 프레임워크 초기화 과정

- 서블릿 컨테이너가 웹 어플리케이션 상태 관리를 위한 ServletContext 생성
- ServletContext가 초기화되면서 컨테스트 초기화 이벤트가 발생된다
  - 이때 등록되어 있는 ServletContextLister 구현체들에 대한 콜백 메서드가 호출
- 서블릿 컨테이너는 설정에 따라 최초 요청 또는 그 이전에 DispatcherServlet 인스턴스를 생성
- DispatcherServlet 인스턴스의 초기화 작업을 진행하고 RequestMapping 객체를 생성
- RequestMapping 인스턴스에서 요청 URL과 Controller를 매핑

### 9.3.2 첫 화면("/")에 접근했을 때 사용자 요청부터 응답까지 흐름

- 서블릿 접근 전에 ResourceFilter를 거치게 됨
  - 해당 요청이 정적 자원(CSS, js, image)가 아닌 경우 서블릿으로 요청을 위임
- `/`로 매핑된 DispatcherServlet이 요청을 처리
- 요청받은 URL을 분석해 해당 URL에 매핑된 컨트롤러를 반환하고 해당 컨트롤러 실행

### 9.3.3 스택과 힙 메모리

클래스의 인스턴스를 생성할 때 비용이 발생하고 더이상 사용하지 않는 경우 가비지 컬렉션을 통해
메모리에서 해제하는데, 이때에도 비용이 발생한다. 따라서 인스턴스를 매번 생성할 필요가 없는 경우
매번 생성하지 않는 것이 성능상 유리하다.

각 클라이언트마다 다른 상태를 유지할 필요가 있는 경우 요청마다 인스턴스를 생성해야한다.
반면, 모든 요청이 동일한 상태를 유지해도 무방한 경우 인스턴스 하나를 생성 후 재사용할 수 있다.
기존에 구현한 서블릿 컨테이너가 시작할 때 인스턴스 하나를 생성 후 재사용한다.
인스턴스를 공유하는 경우 멀티스레딩 환경에 주의해야한다.

JVM은 코드를 실행하기 위해 메모리를 `스택과 힙`에 나눠서 관리한다. 스택은 각 메서드가 실행 될 때
메서드의 파라미터, 로컬 변수 등을 관리하고 각 스레드마다 서로 다른 스택 영역을 가진다.
반면 힙 영역은 클래스의 인스턴스 상태 데이터를 관리하며 스레드가 서로 공유한다.

따라서 멀티스레드에서 자신이 구현한 코드가 어떻게 동작하는지 정확히 파악하고 개발해야한다.