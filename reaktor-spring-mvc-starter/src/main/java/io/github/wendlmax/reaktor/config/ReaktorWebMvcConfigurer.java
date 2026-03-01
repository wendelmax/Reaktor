package io.github.wendlmax.reaktor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wendlmax.reaktor.web.ReaktorResponseReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class ReaktorWebMvcConfigurer implements WebMvcConfigurer {

    private static final String API_PATH_PATTERN = "/api/**";
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"};
    private static final String ALLOWED_HEADERS = "*";

    private final ReaktorProperties properties;
    private final ObjectMapper objectMapper;

    public ReaktorWebMvcConfigurer(ReaktorProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var allowedOrigins = properties.getApi().getCorsAllowedOrigins();
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            return;
        }
        registry.addMapping(API_PATH_PATTERN)
                .allowedOrigins(allowedOrigins.toArray(new String[0]))
                .allowedMethods(ALLOWED_METHODS)
                .allowedHeaders(ALLOWED_HEADERS)
                .allowCredentials(true);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(0, new ReaktorResponseReturnValueHandler(objectMapper));
    }
}
