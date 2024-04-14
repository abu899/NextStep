package controller;

import core.db.DataBase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import model.User;
import util.UserSessionUtils;

import java.util.Optional;


@Slf4j
public class LoginController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        Optional<User> userOptional = DataBase.findUserById(userId);
        if (userOptional.isEmpty()) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }

        User user = userOptional.get();
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }
}
