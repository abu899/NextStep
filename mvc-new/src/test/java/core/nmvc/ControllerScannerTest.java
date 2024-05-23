package core.nmvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class ControllerScannerTest {
    private static ControllerScanner cf;

    @BeforeAll
    public static void setup() {
        cf = new ControllerScanner("core.nmvc");
    }

    @Test
    void getControllers() {
        Map<Class<?>, Object> controllers = cf.getControllers();
        for (Class<?> controller : controllers.keySet()) {
            log.debug("Controller : {}", controller.getName());
        }
    }
}
