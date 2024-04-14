package core.mvc;

import controller.*;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class RequestMapping {
    private Map<String, Controller> mappings = new HashMap<>();

    public void initMapping() {
        mappings.put("/", new HomeController());
        mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        mappings.put("/users", new ListUserController());
        mappings.put("/user/login", new LoginController());
        mappings.put("/user/create", new CreateUserController());
   }

   public Controller findController(String requestURI) {
       return mappings.get(requestURI);
   }

   public void put(String url, Controller controller) {
       mappings.put(url, controller);
   }
}
