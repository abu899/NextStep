package controller;

import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.sql.SQLException;
import java.util.List;

public class HomeController implements Controller {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        UserDao userDao = new UserDao();
        try {
            List<User> users = userDao.findAll();
            req.setAttribute("users", users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "home.jsp";
    }
}