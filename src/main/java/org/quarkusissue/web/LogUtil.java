package org.quarkusissue.web;

import java.util.Optional;

public class LogUtil {

    public static void log(String key, Optional<String> value, String usageType) {
        System.out.println(String.format("Read value [%30s] for key [%20s] using [%s]", value, key, usageType));
    }
}
