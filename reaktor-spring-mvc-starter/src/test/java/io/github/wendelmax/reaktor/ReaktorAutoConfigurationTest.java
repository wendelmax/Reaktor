package io.github.wendelmax.reaktor;

import io.github.wendelmax.reaktor.dialect.ReaktorComponentResolver;
import io.github.wendelmax.reaktor.dialect.ReaktorDialect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ReaktorAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(io.github.wendelmax.reaktor.config.ReaktorAutoConfiguration.class))
            .withPropertyValues("reaktor.component-base-path=/custom", "reaktor.component-extension=.js");

    @Test
    void autoConfigurationRegistersBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ReaktorComponentResolver.class);
            assertThat(context).hasSingleBean(ReaktorDialect.class);
        });
    }

    @Test
    void componentResolverUsesProperties() {
        contextRunner.run(context -> {
            var resolver = context.getBean(ReaktorComponentResolver.class);
            assertThat(resolver.resolveScriptPath("Test")).isEqualTo("/custom/Test.js");
        });
    }

    @Test
    void reaktorWebMvcConfigurerIsRegistered() {
        contextRunner.run(context -> {
            assertThat(context).hasBean("reaktorWebMvcConfigurer");
        });
    }
}
