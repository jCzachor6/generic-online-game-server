package generic.online.game.server.gogs;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertiesLoader {
    private final Map<String, Map<String, Object>> propertyStorage = new HashMap<>();

    public Object getProperty(String filepath, String key) {
        if (propertyStorage.containsKey(filepath)) {
            return propertyStorage.get(filepath).get(key);
        }

        Map<String, Object> propertiesMap = new HashMap<>();
        Properties loadedProperties = new Properties();
        try (InputStream is = getClass().getResourceAsStream(filepath)) {
            loadedProperties.load(is);
            loadedProperties.forEach((key1, value) -> propertiesMap.put(key1.toString(), value));
            propertyStorage.put(filepath, propertiesMap);
            return getProperty(filepath, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> getProperties(String filepath) {
        return getProperties(filepath, StringUtils.EMPTY);
    }

    public Map<String, Object> getProperties(String filepath, String prefix) {
        if (propertyStorage.containsKey(filepath)) {
            return propertyStorage.get(filepath).entrySet().stream()
                    .filter(s -> s.getKey().startsWith(prefix))
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().replace(prefix, StringUtils.EMPTY),
                            Map.Entry::getValue
                    ));
        }

        try (InputStream is = getClass().getResourceAsStream(filepath)) {
            Properties loadedProperties = new Properties();
            loadedProperties.load(is);
            Map<String, Object> propertiesMap = loadedProperties.entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));
            propertyStorage.put(filepath, propertiesMap);
            return getProperties(filepath, prefix);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
