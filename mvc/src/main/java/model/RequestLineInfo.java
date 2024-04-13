package model;

import http.HttpMethod;
import lombok.Getter;

@Getter
public class RequestLineInfo {
    private final HttpMethod method;
    private final String path;
    private final String queryString;

    public RequestLineInfo(String method, String path, String queryString) {
        this.method = HttpMethod.valueOf(method);
        this.path = path;
        this.queryString = queryString;
    }
}
