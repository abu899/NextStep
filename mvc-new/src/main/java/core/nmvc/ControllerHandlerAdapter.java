package core.nmvc;

import core.mvc.Controller;
import core.mvc.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ((Controller) handler).execute(request, response);
    }
}
