package webserver;

import lombok.extern.slf4j.Slf4j;
import util.HttpParser;
import util.IOUtils;
import webserver.controller.Controller;
import webserver.db.DataBase;
import webserver.domain.User;
import webserver.dto.RequestLineInfo;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

            Controller controller = RequestMapping.getController(request.getPath());
            if (controller == null) {
                String url = getDefaultUrl(request.getPath());
                response.forward(url);
            } else {
                controller.service(request, response);
            }
        } catch (
                IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultUrl(String path) {
        if (path.equals("/")) {
            path = "/index.html";
        }
        return path;
    }
}
