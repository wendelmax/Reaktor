package io.github.wendelmax.reaktor.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReaktorPropertiesTest {

    @Test
    void defaultValues() {
        var props = new ReaktorProperties();
        assertThat(props.getComponentBasePath()).isEqualTo("/react");
        assertThat(props.getComponentExtension()).isEqualTo(".tsx");
    }

    @Test
    void setters() {
        var props = new ReaktorProperties();
        props.setComponentBasePath("/assets");
        props.setComponentExtension(".js");
        assertThat(props.getComponentBasePath()).isEqualTo("/assets");
        assertThat(props.getComponentExtension()).isEqualTo(".js");
    }
}
