package core.nmvc;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        // Controller 어노테이션 클래스 탐색
        ControllerScanner cs = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = cs.getControllers();

        // Controller 어노테이션 클래스 내의 RequestMapping 어노테이션 메서드 탐색
        Set<Method> methods = getRequestMappingMethods(controllers);

        // RequestMapping 어노테이션 메서드의 각 어노테이션의 정보를 통해 handlerExecutions에 저장
        for (Method method : methods) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            log.debug("등록된 url = {}, method = {}", rm.value(), rm.method());
            handlerExecutions.put(new HandlerKey(rm.value(), rm.method()),
                    new HandlerExecution(controllers.get(method.getDeclaringClass()), method));
        }
    }

    private Set<Method> getRequestMappingMethods(Map<Class<?>, Object> controllers) {
        Set<Method> methods = Sets.newHashSet();
        for (Class<?> controller : controllers.keySet()) {
            methods.addAll(
                    ReflectionUtils.getAllMethods(controller,
                            ReflectionUtils.withAnnotation(RequestMapping.class)));
        }

        return methods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
