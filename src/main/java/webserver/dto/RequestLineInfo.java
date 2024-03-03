package webserver.dto;

public record RequestLineInfo(String method, String path, String queryString) {
}
