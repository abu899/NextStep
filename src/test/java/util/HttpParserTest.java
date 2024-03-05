package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.dto.RequestLineInfo;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class HttpParserTest {

    @Test
    @DisplayName("헤더 정보를 파싱해서 Mehtod와 Path를 반환한다")
    void header() {
        // given
        String line = "GET /index.html HTTP/1.1";

        // when
        RequestLineInfo requestLineInfo = HttpParser.parseRequestLine(line);

        // then
        assertThat(requestLineInfo.method()).isEqualTo("GET");
        assertThat(requestLineInfo.path()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("헤더 정보를 파싱해서 Mehtod와 Path, queryString를 반환한다")
    void headerWithQuery() {
        // given
        String line = "GET /user/create?userId=java&password=password HTTP/1.1";

        // when
        RequestLineInfo requestLineInfo = HttpParser.parseRequestLine(line);

        // then
        assertThat(requestLineInfo.method()).isEqualTo("GET");
        assertThat(requestLineInfo.path()).isEqualTo("/user/create");
        assertThat(requestLineInfo.queryString()).isEqualTo("userId=java&password=password");
    }

    @Test
    @DisplayName("queryString을 파싱하여 Map에 저장한다")
    void parseQueryString() {
        // given
        String queryString = "userId=java&password=password";

        // when
        Map<String, String> queryMap = HttpParser.parseContents(queryString);

        // then
        assertThat(queryMap.get("userId")).isEqualTo("java");
        assertThat(queryMap.get("password")).isEqualTo("password");
    }

    @Test
    @DisplayName("Content-Length를 파싱하여 반환한다")
    void parseContentLength() {
        // given
        String line = "Content-Length: 11";

        // when
        Integer contentLength = HttpParser.readContentLength(line);

        // then
        assertThat(contentLength).isEqualTo(11);
    }

    @Test
    @DisplayName("Content-Length가 없으면 null을 반환한다")
    void parseContentLengthNull() {
        // given
        String line = "Content-Type: text/html;charset=utf-8";

        // when
        Integer contentLength = HttpParser.readContentLength(line);

        // then
        assertThat(contentLength).isNull();
    }

    @Test
    @DisplayName("쿠키에서 로그인 여부를 반환한다")
    void isLogin() {
        // given
        String line = "Cookie: logined=true";

        // when
        boolean logined = HttpParser.isLogin(line);

        // then
        assertThat(logined).isTrue();
    }

    @Test
    @DisplayName("쿠키가 없는 경우 로그인 상태는 false를 반환한다")
    void isLoginFalse() {
        // given
        String line = "Content-Type: text/html;charset=utf-8";

        // when
        boolean logined = HttpParser.isLogin(line);

        // then
        assertThat(logined).isFalse();
    }
}