package io.github.wendlmax.reaktor.dialect;

import io.github.wendlmax.reaktor.config.ReaktorProperties;
import io.github.wendlmax.reaktor.registry.ReaktorComponentRegistry;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class DefaultReaktorComponentResolver implements ReaktorComponentResolver {

    private final ReaktorProperties properties;
    private final ReaktorComponentRegistry registry;
    private final String fixedBasePath;
    private final String fixedExtension;

    public DefaultReaktorComponentResolver(String basePath, String extension) {
        this.properties = null;
        this.registry = null;
        this.fixedBasePath = basePath != null ? basePath : "/react";
        this.fixedExtension = StringUtils.hasText(extension) ? extension : ".tsx";
    }

    public DefaultReaktorComponentResolver(ReaktorProperties properties) {
        this(properties, null);
    }

    public DefaultReaktorComponentResolver(ReaktorProperties properties, ReaktorComponentRegistry registry) {
        this.properties = properties;
        this.registry = registry;
        this.fixedBasePath = null;
        this.fixedExtension = null;
    }

    @Override
    public String resolveScriptPath(String componentName) {
        if (registry != null) {
            Optional<String> path = registry.resolveScriptPath(componentName);
            if (path.isPresent()) {
                return path.get();
            }
        }
        String basePath = fixedBasePath != null ? fixedBasePath : resolveBasePath();
        String extension = fixedExtension != null ? fixedExtension : resolveExtension();
        String path = basePath.endsWith("/") ? basePath : basePath + "/";
        String ext = extension.startsWith(".") ? extension : "." + extension;
        return path + componentName + ext;
    }

    private String resolveBasePath() {
        if (properties == null) {
            return "/react";
        }
        if (properties.isDevMode() && StringUtils.hasText(properties.getDevBase())) {
            return properties.getDevBase();
        }
        if (!properties.isDevMode() && StringUtils.hasText(properties.getProdBase())) {
            return properties.getProdBase();
        }
        return StringUtils.hasText(properties.getComponentBasePath()) ? properties.getComponentBasePath() : "/react";
    }

    private String resolveExtension() {
        if (properties == null) {
            return ".tsx";
        }
        return StringUtils.hasText(properties.getComponentExtension()) ? properties.getComponentExtension() : ".tsx";
    }
}
