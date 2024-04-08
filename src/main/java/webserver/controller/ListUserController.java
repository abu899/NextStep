package webserver.controller;

import lombok.extern.slf4j.Slf4j;
import webserver.db.DataBase;
import webserver.domain.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.List;

@Slf4j
public class ListUserController extends AbstractController {
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
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
    }
}
