package webserver.db;

import webserver.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataBase {
    public static final Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.of(users.get(userId));
    }

    public static List<User> findAll() {
        return users.values().stream().toList();
    }
}
