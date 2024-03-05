package webserver;

import lombok.extern.slf4j.Slf4j;
import util.HttpParser;
import util.IOUtils;
import webserver.db.DataBase;
import webserver.domain.User;
import webserver.dto.RequestLineInfo;

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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = bufferedReader.readLine();

            log.debug("line = {}", line);
            if (line == null) {
                return;
            }

            RequestLineInfo requestLineInfo = HttpParser.parseRequestLine(line);
            String url = getDefaultUrl(requestLineInfo.path());
            int contentLength = 0;
            boolean logined = false;
            while (!line.equals("")) {
                log.debug("header : {}", line);
                line = bufferedReader.readLine();
                if (line.contains("Content-Length")) {
                    contentLength = HttpParser.readContentLength(line);
                }
                if (line.contains("Cookie")) {
                    logined = HttpParser.isLogin(line);
                }
            }

            if (url.equals("/user/create")) {
                String body = IOUtils.readData(bufferedReader, contentLength);
                Map<String, String> params = HttpParser.parseContents(body);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("user = {}", user);
                DataBase.addUser(user);
                DataOutputStream dataOutputStream = new DataOutputStream(out);
                response302Header(dataOutputStream, "/index.html");
            } else if (url.equals("/user/login")) {
                String body = IOUtils.readData(bufferedReader, contentLength);
                Map<String, String> params = HttpParser.parseContents(body);
                Optional<User> userOptional = DataBase.findUserById(params.get("userId"));

                DataOutputStream dataOutputStream = new DataOutputStream(out);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    if (user.getPassword().equals(params.get("password"))) {
                        response302WithCookie(dataOutputStream);
                        return;
                    }
                    responseWithResource(dataOutputStream, "/user/login_failed.html");
                } else {
                    responseWithResource(dataOutputStream, "/user/login_failed.html");
                }
            } else if (url.equals("/user/list")) {
                DataOutputStream dataOutputStream = new DataOutputStream(out);

                if (!logined) {
                    responseWithResource(dataOutputStream, "/user/login.html");
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

                byte[] body = sb.toString().getBytes();
                response200Header(dataOutputStream, body.length);
                responseBody(dataOutputStream, body);
            } else if (url.endsWith(".css")) {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200WithCss(dos, body.length);
                responseBody(dos, Files.readAllBytes(new File("./webapp" + url).toPath()));

            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200WithCss(DataOutputStream dos, int length) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302WithCookie(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Set-Cookie: logined=true \r\n");
            dos.writeBytes("Location: /index.html" + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseWithResource(DataOutputStream dos, String resource) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + resource).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
