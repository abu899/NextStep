package util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import webserver.dto.RequestLineInfo;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpParser {

    public static RequestLineInfo parseRequestLine(String line) {
        String[] splitHeader = line.split(" ");
        if (splitHeader[1].split("\\?").length > 1) {
            String[] splitPath = splitHeader[1].split("\\?");
            return new RequestLineInfo(splitHeader[0], splitPath[0], splitPath[1]);
        }
        return new RequestLineInfo(splitHeader[0], splitHeader[1], null);
    }

    public static Map<String, String> parseContents(String contents) {
        String[] splitQuery = contents.split("&");
        Map<String, String> queryMap = new HashMap<>();
        for (String query : splitQuery) {
            String[] split = query.split("=");
            queryMap.put(split[0], split[1]);
        }
        return queryMap;
    }

    public static Integer readContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    public static boolean isLogin(String line) {
        String[] headerTokens = line.split(":");
        Map<String, String> cookies = parseContents(headerTokens[1].trim());
        if(!cookies.containsKey("logined")) {
            return false;
        }

        return Boolean.parseBoolean(cookies.get("logined"));
    }
}
