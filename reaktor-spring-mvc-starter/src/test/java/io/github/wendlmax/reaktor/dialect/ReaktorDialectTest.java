package io.github.wendlmax.reaktor.dialect;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Mockito;
import io.github.wendlmax.reaktor.config.ReaktorProperties;

class ReaktorDialectTest {

    @Test
    void dialectHasCorrectName() {
        var resolver = new DefaultReaktorComponentResolver("/react", ".tsx");
        var properties = new ReaktorProperties(); // Instantiate ReaktorProperties
        var dialect = new ReaktorDialect(resolver, properties); // Pass properties to constructor
        assertThat(dialect.getName()).isEqualTo("Reaktor");
    }

    @Test
    void testGetProcessors() {
        ReaktorComponentResolver mockResolver = Mockito.mock(ReaktorComponentResolver.class);
        io.github.wendlmax.reaktor.config.ReaktorProperties properties = new io.github.wendlmax.reaktor.config.ReaktorProperties();
        ReaktorDialect dialect = new ReaktorDialect(mockResolver, properties);
        var processors = dialect.getProcessors("react");
        assertThat(processors).isNotEmpty();
    }
}
