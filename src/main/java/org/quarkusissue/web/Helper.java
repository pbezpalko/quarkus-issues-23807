package org.quarkusissue.web;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;

@ApplicationScoped
public class Helper {

    @Inject
    private Config config;

    public Optional<String> getConfigValue(String key) {
        return config.getOptionalValue(key, String.class);
    }
}
