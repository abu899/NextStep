package core.nmvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ControllerScannerTest {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScannerTest.class);

    private ControllerScanner cf;

    @BeforeEach
    public void setup() {
        cf = new ControllerScanner("core.nmvc");
    }

    @Test
    public void getControllers() throws Exception {
        Map<Class<?>, Object> controllers = cf.getControllers();
        for (Class<?> controller : controllers.keySet()) {
            logger.debug("controller : {}", controller);
        }
    }
}
