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

/**
 * Automatic Spring Boot configuration for Reaktor.
 * <p>
 * This configuration class registers the core components of Reaktor:
 * the component resolver, the Thymeleaf dialect, the global exception handler,
 * and the Spring Web MVC configuration required to support Reaktor's return
 * handlers.
 * </p>
 *
 * @since 0.1.0
 */
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(SpringTemplateEngine.class)
@EnableConfigurationProperties(ReaktorProperties.class)
public class ReaktorAutoConfiguration {

    /**
     * Provides the default {@link ReaktorComponentResolver} bean.
     *
     * @param properties the Reaktor properties configuration.
     * @param registry   an optional {@link ReaktorComponentRegistry} to resolve
     *                   dynamic components.
     * @return the configured {@link ReaktorComponentResolver}.
     */
    @Bean
    @ConditionalOnMissingBean
    public ReaktorComponentResolver reaktorComponentResolver(ReaktorProperties properties,
            Optional<ReaktorComponentRegistry> registry) {
        return new DefaultReaktorComponentResolver(properties, registry.orElse(null));
    }

    /**
     * Provides the {@link ReaktorDialect} bean for Thymeleaf.
     *
     * @param componentResolver the resolver used to map components to paths.
     * @param properties        the Reaktor properties configuration.
     * @return the {@link ReaktorDialect} instance.
     */
    @Bean
    @ConditionalOnMissingBean
    public ReaktorDialect reaktorDialect(ReaktorComponentResolver componentResolver, ReaktorProperties properties) {
        return new ReaktorDialect(componentResolver, properties);
    }

    /**
     * Provides a global exception handler to standardise unhandled server errors.
     *
     * @return the {@link ReaktorGlobalExceptionHandler} instance.
     */
    @Bean
    @ConditionalOnMissingBean
    public ReaktorGlobalExceptionHandler reaktorGlobalExceptionHandler() {
        return new ReaktorGlobalExceptionHandler();
    }

    /**
     * Registers Reaktor's custom return value handlers into Spring MVC framework.
     *
     * @param properties           the Reaktor properties configuration.
     * @param objectMapperProvider the Jackson {@link ObjectMapper} provider.
     * @return the configuration bean mapping handlers and CORS rules.
     */
    @Bean
    @ConditionalOnMissingBean(name = "reaktorWebMvcConfigurer")
    public WebMvcConfigurer reaktorWebMvcConfigurer(ReaktorProperties properties,
            ObjectProvider<ObjectMapper> objectMapperProvider) {
        ObjectMapper objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);
        return new ReaktorWebMvcConfigurer(properties, objectMapper);
    }
}
