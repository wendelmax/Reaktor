package io.github.wendlmax.reaktor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wendlmax.reaktor.web.ReaktorGlobalExceptionHandler;
import io.github.wendlmax.reaktor.dialect.DefaultReaktorComponentResolver;
import io.github.wendlmax.reaktor.dialect.ReaktorComponentResolver;
import io.github.wendlmax.reaktor.dialect.ReaktorDialect;
import io.github.wendlmax.reaktor.registry.ReaktorComponentRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Optional;

@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(SpringTemplateEngine.class)
@EnableConfigurationProperties(ReaktorProperties.class)
public class ReaktorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReaktorComponentResolver reaktorComponentResolver(ReaktorProperties properties,
            Optional<ReaktorComponentRegistry> registry) {
        return new DefaultReaktorComponentResolver(properties, registry.orElse(null));
    }

    @Bean
    @ConditionalOnMissingBean
    public ReaktorDialect reaktorDialect(ReaktorComponentResolver componentResolver, ReaktorProperties properties) {
        return new ReaktorDialect(componentResolver, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReaktorGlobalExceptionHandler reaktorGlobalExceptionHandler() {
        return new ReaktorGlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "reaktorWebMvcConfigurer")
    public WebMvcConfigurer reaktorWebMvcConfigurer(ReaktorProperties properties,
            ObjectProvider<ObjectMapper> objectMapperProvider) {
        ObjectMapper objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);
        return new ReaktorWebMvcConfigurer(properties, objectMapper);
    }
}
