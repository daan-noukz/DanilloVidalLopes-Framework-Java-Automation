
package com.automation.framework.infrastructure.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties props = new Properties();

    static {
        try {
            InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties");
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
