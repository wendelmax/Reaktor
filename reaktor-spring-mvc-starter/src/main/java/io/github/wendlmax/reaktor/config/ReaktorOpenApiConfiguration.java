package io.github.wendlmax.reaktor.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(GroupedOpenApi.class)
public class ReaktorOpenApiConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "reaktorApiGroup")
    public GroupedOpenApi reaktorApiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }
}
