package controller;

import core.db.DataBase;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.UserSessionUtils;

import java.io.Serial;

@WebServlet("/users")
public class ListUserController implements Controller {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/user/loginForm";
        }
        req.setAttribute("users", DataBase.findAll());
        return "/user/list.jsp";
    }
}
