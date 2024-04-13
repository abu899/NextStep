import controller.Controller;
import http.HttpRequest;
import http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import util.HttpParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class RequestHandler extends Thread {

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.info("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if (request.getCookies().getCookie("JSESSIONID") == null) {
                response.addHeader("Set-Cookie", "JSESSIONID=" + UUID.randomUUID());
            }

            Controller controller = RequestMapping.getController(request.getPath());
            if (controller == null) {
                String url = getDefaultUrl(request.getPath());
                response.forward(url);
            } else {
                controller.service(request, response);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultUrl(String path) {
        if (path.equals("/")) {
            path = "/index.html";
        }
        return path;
    }

    private String getSessionId(String cookieValue) {
        Map<String, String> cookies = HttpParser.parseCookies(cookieValue);
        return cookies.get("JSESSIONID");
    }
}
