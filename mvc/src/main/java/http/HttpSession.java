package http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class HttpSession {
    private Map<String, Object> values = new HashMap<>();
    @Getter private final String id;

    public void setAttribute(String key, Object value) {
        values.put(key, value);
    }
    public Object getAttribute(String key) {
        return values.get(key);
    }

    public void removeAttribute(String key) {
        values.remove(key);
    }

    public void invalidate() {
        HttpSessions.remove(id);
    }
}
