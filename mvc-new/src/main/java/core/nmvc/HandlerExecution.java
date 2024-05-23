package core.nmvc;

import core.mvc.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class HandlerExecution {
    private final Object declaredObject;
    private final Method method;

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(declaredObject, request, response);
        } catch (IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e) {
            log.error("{} method invoke fail", method, e);
            throw new RuntimeException(e);
        }
    }
}
