package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.dto.HeaderInfo;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class HttpParserTest {

    @Test
    @DisplayName("헤더 정보를 파싱해서 Mehtod와 Path를 반환한다")
    void header() {
        // given
        String line = "GET /index.html HTTP/1.1";

        // when
        HeaderInfo headerInfo = HttpParser.parseHeaderInfo(line);

        // then
        assertThat(headerInfo.method()).isEqualTo("GET");
        assertThat(headerInfo.path()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("헤더 정보를 파싱해서 Mehtod와 Path, queryString를 반환한다")
    void headerWithQuery() {
        // given
        String line = "GET /user/create?userId=java&password=password HTTP/1.1";

        // when
        HeaderInfo headerInfo = HttpParser.parseHeaderInfo(line);

        // then
        assertThat(headerInfo.method()).isEqualTo("GET");
        assertThat(headerInfo.path()).isEqualTo("/user/create");
        assertThat(headerInfo.queryString()).isEqualTo("userId=java&password=password");
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
}