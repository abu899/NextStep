package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.dto.RequestLineInfo;
import webserver.http.HttpMethod;

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
        assertThat(requestLineInfo.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLineInfo.getPath()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("헤더 정보를 파싱해서 Mehtod와 Path, queryString를 반환한다")
    void headerWithQuery() {
        // given
        String line = "GET /user/create?userId=java&password=password HTTP/1.1";

        // when
        RequestLineInfo requestLineInfo = HttpParser.parseRequestLine(line);

        // then
        assertThat(requestLineInfo.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLineInfo.getPath()).isEqualTo("/user/create");
        assertThat(requestLineInfo.getQueryString()).isEqualTo("userId=java&password=password");
    }

    @Test
    @DisplayName("queryString을 파싱하여 Map에 저장한다")
    void parseQueryString() {
        // given
        String queryString = "userId=java&password=password";

        // when
        Map<String, String> queryMap = HttpParser.parseContents(queryString, "&");

        // then
        assertThat(queryMap.get("userId")).isEqualTo("java");
        assertThat(queryMap.get("password")).isEqualTo("password");
    }

    @Test
    @DisplayName("쿠키에서 로그인 여부를 반환한다")
    void isLogin() {
        // given
        String line = "logined=true;test=false";

        // when
        Map<String, String> cookieMap = HttpParser.parseCookies(line);

        // then
        assertThat(cookieMap.size()).isEqualTo(2);
        assertThat(Boolean.parseBoolean(cookieMap.get("logined"))).isTrue();
        assertThat(Boolean.parseBoolean(cookieMap.get("test"))).isFalse();
    }
}