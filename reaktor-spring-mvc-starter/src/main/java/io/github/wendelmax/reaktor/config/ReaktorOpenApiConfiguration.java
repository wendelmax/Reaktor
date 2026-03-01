package io.github.wendelmax.reaktor.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI Configuration.
 * <p>
 * This configuration isolates the API routes to automatically generate Swagger
 * documentation
 * for the {@code /api/**} endpoints exposed by Reaktor endpoints.
 * </p>
 *
 * @since 0.1.0
 */
@Configuration
@ConditionalOnClass(GroupedOpenApi.class)
public class ReaktorOpenApiConfiguration {

    /**
     * Builds the default GroupedOpenApi targeting Reaktor routes.
     *
     * @return the {@link GroupedOpenApi} for the "api" group.
     */
    @Bean
    @ConditionalOnMissingBean(name = "reaktorApiGroup")
    public GroupedOpenApi reaktorApiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }
}
