package core.nmvc;

import core.annotation.Controller;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ControllerScanner {

    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerAnnotationClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerAnnotationClasses);
    }

    Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        Map<Class<?>, Object> controllerMap = new HashMap<>();
        for (Class<?> controller : controllers) {
            try {
                controllerMap.put(controller, controller.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                log.error(e.getMessage());
            }
        }
        return controllerMap;
    }
}
