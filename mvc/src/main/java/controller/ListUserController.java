package controller;

import dao.UserDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import util.UserSessionUtils;

import java.io.Serial;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/users")
public class ListUserController implements Controller {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        if (UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/user/loginForm";
        }

        UserDao userDao = new UserDao();
        try {
            List<User> users = userDao.findAll();
            req.setAttribute("users", users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "/user/list.jsp";
    }
}
