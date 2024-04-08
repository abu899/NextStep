package webserver;

import lombok.extern.slf4j.Slf4j;
import util.HttpParser;
import util.IOUtils;
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
            String url = getDefaultUrl(request.getPath());

            if (url.equals("/user/create")) {
                User user = new User(
                        request.getParameter("userId"),
                        request.getParameter("password"),
                        request.getParameter("name"),
                        request.getParameter("email")
                );
                log.debug("user = {}", user);
                DataBase.addUser(user);
                response.sendRedirect("/index.html");
            } else if (url.equals("/user/login")) {
                Optional<User> userOptional = DataBase.findUserById(request.getParameter("userId"));

                DataOutputStream dataOutputStream = new DataOutputStream(out);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    if (user.getPassword().equals(request.getParameter("password"))) {
                        response.addHeader("Set-Cookie", "logined=true");
                        response.sendRedirect("/index.html");
                        return;
                    }
                    response.sendRedirect("/user/login_failed.html");
                } else {
                    response.sendRedirect("/user/login_failed.html");
                }
            } else if (url.equals("/user/list")) {
                DataOutputStream dataOutputStream = new DataOutputStream(out);

                if (!request.isLogin()) {
                    response.sendRedirect("/user/login.html");
                    return;
                }

                List<User> users = DataBase.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("<table border='1'>");
                for (User user : users) {
                    sb.append("<tr>");
                    sb.append("<td>" + user.getUserId() + "</td>");
                    sb.append("<td>" + user.getName() + "</td>");
                    sb.append("<td>" + user.getEmail() + "</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");
                response.forwardBody(sb.toString());
            } else {
                response.forward(url);
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
