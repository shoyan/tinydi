package shoyan.di;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Named;

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
            Objects.requireNonNull(type, name + " not found.");

            try {
                return createObject(type);
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new RuntimeException(name + " can not instanciate", ex);
            }
        });
    }

    public static void autoRegister() {
        try {
            URL res = Context.class.getResource(
                    "/" + Context.class.getName().replace('.', '/') + ".class");
            Path classPath = Paths.get(res.toURI()).getParent().resolve("../../");
            Files.walk(classPath)
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().endsWith(".class"))
                    .map(p -> classPath.relativize(p))
                    .map(p -> p.toString().replace(File.separatorChar, '.'))
                    .map(n -> n.substring(0, n.length() - 6))
                    .forEach(n -> {
                        try {
                            Class c = Class.forName(n);
                            if (c.isAnnotationPresent(Named.class)) {
                                String simpleName = c.getSimpleName();
                                register(simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1), c);
                            }
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static <T> T createObject(Class<T> type) throws InstantiationException, IllegalAccessException {
        T object = type.newInstance();
        for (Field field : type.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            field.setAccessible(true);
            field.set(object, getBean(field.getName()));
        }
        return object;
    }
}
