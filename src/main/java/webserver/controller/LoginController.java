package webserver.controller;

import lombok.extern.slf4j.Slf4j;
import webserver.db.DataBase;
import webserver.domain.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.Optional;

@Slf4j
public class LoginController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        Optional<User> userOptional = DataBase.findUserById(request.getParameter("userId"));

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
    }
}
