package shoyan.di;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Context {
    static Map<String, Class> types = new HashMap();
    static Map<String, Object> beans = new HashMap();

    public Context() {
    }

    static void register(String name, Class type) {
        types.put(name, type);
    }

    static Object getBean(String name) {
        return beans.computeIfAbsent(name, (key) -> {
            Class type = types.get(name);
            Objects.requireNonNull(type, name + "not found.");

            try {
                return type.newInstance();
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new RuntimeException(name + " can not instanciate", ex);
            }
        });
    }

}
