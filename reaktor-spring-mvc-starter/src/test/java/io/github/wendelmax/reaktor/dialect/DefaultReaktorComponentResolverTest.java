package io.github.wendelmax.reaktor.dialect;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultReaktorComponentResolverTest {

    @Test
    void resolveScriptPath_withDefaultConfiguration() {
        var props = new io.github.wendelmax.reaktor.config.ReaktorProperties();
        props.setComponentBasePath("/react");
        props.setComponentExtension(".tsx");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("ProductList")).isEqualTo("/react/ProductList.tsx");
    }

    @Test
    void resolveScriptPath_withJsExtension() {
        var props = new io.github.wendelmax.reaktor.config.ReaktorProperties();
        props.setComponentBasePath("/react");
        props.setComponentExtension(".js");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("ProductList")).isEqualTo("/react/ProductList.js");
    }

    @Test
    void resolveScriptPath_withCustomBasePath() {
        var props = new io.github.wendelmax.reaktor.config.ReaktorProperties();
        props.setComponentBasePath("/assets/components");
        props.setComponentExtension(".tsx");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("Cart")).isEqualTo("/assets/components/Cart.tsx");
    }

    @Test
    void resolveScriptPath_withNullBasePath_usesDefault() {
        var props = new io.github.wendelmax.reaktor.config.ReaktorProperties();
        props.setComponentBasePath(null);
        props.setComponentExtension(".tsx");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("X")).isEqualTo("/react/X.tsx");
    }

    @Test
    void resolveScriptPath_withEmptyExtension_usesDefault() {
        var props = new io.github.wendelmax.reaktor.config.ReaktorProperties();
        props.setComponentBasePath("/react");
        props.setComponentExtension("");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("X")).isEqualTo("/react/X.tsx");
    }

    @Test
    void resolveScriptPath_withBasePathEndingWithSlash() {
        var props = new io.github.wendelmax.reaktor.config.ReaktorProperties();
        props.setComponentBasePath("/react/");
        props.setComponentExtension(".tsx");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("X")).isEqualTo("/react/X.tsx");
    }
}
