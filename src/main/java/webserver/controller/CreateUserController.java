package webserver.controller;

import lombok.extern.slf4j.Slf4j;
import webserver.db.DataBase;
import webserver.domain.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

@Slf4j
public class CreateUserController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        log.debug("user = {}", user);
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
