package core.ref;

import next.model.Question;
import next.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        // public
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            logger.debug("Field : {}", field);
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            logger.debug("Constructor : {}", constructor);
        }

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            logger.debug("Method : {}", method);
        }

        // all
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            logger.debug("Declared Field : {}", field);
        }

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            logger.debug("Declared Constructor : {}", constructor);
        }

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            logger.debug("Declared Method : {}", method);
        }
    }

    @Test
    public void newInstanceWithConstructorArgs() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            User user = (User) declaredConstructor.newInstance("brett", "passwd", "Ahn", " test@test.com");
            logger.debug("User : {}", user);
            Assertions.assertThat(user.getUserId()).isEqualTo("brett");
        }
    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();

        Field name = clazz.getDeclaredField("name");
        Field age = clazz.getDeclaredField("age");

        name.setAccessible(true);
        age.setAccessible(true);

        name.set(student, "brett");
        age.setInt(student, 30);

        Assertions.assertThat(student.getName()).isEqualTo("brett");
        Assertions.assertThat(student.getAge()).isEqualTo(30);
    }
}
