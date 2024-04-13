package http;

import util.HttpParser;

import java.util.Map;

public class HttpCookie {
    private final Map<String, String> cookies;

    public HttpCookie(String cookieValue) {
        cookies = HttpParser.parseCookies(cookieValue);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }
}
