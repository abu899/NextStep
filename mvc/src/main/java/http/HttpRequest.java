package http;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import model.RequestLineInfo;
import util.HttpParser;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequest {
    @Getter private HttpMethod method;
    @Getter private String path;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    public HttpRequest(InputStream in) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = bufferedReader.readLine();

            log.debug("line = {}", line);
            if (line == null) {
                return;
            }

            processRequestLine(line);

            line = bufferedReader.readLine();
            while (!line.equals("")) {
                log.debug("header : {}", line);
                String[] tokens = line.split(":");
                headers.put(tokens[0].trim(), tokens[1].trim());
                line = bufferedReader.readLine();
            }

            if(HttpMethod.POST == method) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                String body = IOUtils.readData(bufferedReader, contentLength);
                params.putAll(HttpParser.parseContents(body, "&"));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void processRequestLine(String requestLine) {
        RequestLineInfo requestLineInfo = HttpParser.parseRequestLine(requestLine);
        this.method = requestLineInfo.getMethod();
        this.path = requestLineInfo.getPath();
        this.params = HttpParser.parseContents(requestLineInfo.getQueryString(), "&");
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public HttpCookie getCookies() {
        return new HttpCookie(headers.get("Cookie"));
    }

    public HttpSession getSession() {
        return HttpSessions.getSession(getCookies().getCookie("JSESSIONID"));
    }
}
