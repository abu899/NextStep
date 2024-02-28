package webserver.dto;

import lombok.Getter;

public record HeaderInfo(String method, String path, String queryString) {
}
