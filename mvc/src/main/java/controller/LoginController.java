package controller;

import core.db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import model.User;

import java.util.Optional;


@Slf4j
public class LoginController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        Optional<User> userOptional = DataBase.findUserById(request.getParameter("userId"));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getParameter("password"))) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("/index.html");
                return;
            }
            response.sendRedirect("/user/login_failed.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
