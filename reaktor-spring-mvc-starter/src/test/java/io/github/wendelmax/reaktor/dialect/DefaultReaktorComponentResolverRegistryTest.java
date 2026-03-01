package io.github.wendelmax.reaktor.dialect;

import io.github.wendelmax.reaktor.config.ReaktorProperties;
import io.github.wendelmax.reaktor.registry.ReaktorComponentRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultReaktorComponentResolverRegistryTest {

    @Test
    void withRegistry_usesExplicitMapping() {
        var props = new ReaktorProperties();
        var registry = ReaktorComponentRegistry.builder()
                .register("ProductList", "/custom/ProductList.bundle.js")
                .build();
        var resolver = new DefaultReaktorComponentResolver(props, registry);
        assertThat(resolver.resolveScriptPath("ProductList")).isEqualTo("/custom/ProductList.bundle.js");
    }

    @Test
    void withRegistry_fallsBackToConventionWhenNotRegistered() {
        var props = new ReaktorProperties();
        props.setComponentBasePath("/react");
        props.setComponentExtension(".tsx");
        var registry = ReaktorComponentRegistry.builder()
                .register("ProductList", "/custom/ProductList.js")
                .build();
        var resolver = new DefaultReaktorComponentResolver(props, registry);
        assertThat(resolver.resolveScriptPath("Cart")).isEqualTo("/react/Cart.tsx");
    }

    @Test
    void withProperties_devModeUsesDevBase_otherwiseUsesComponentBasePath() {
        var props = new ReaktorProperties();
        props.setComponentBasePath("/custom");
        props.setComponentExtension(".js");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("Test")).isEqualTo("/custom/Test.js");
    }

    @Test
    void withProperties_devMode_usesDevBase() {
        var props = new ReaktorProperties();
        props.setDevMode(true);
        props.setDevBase("/react");
        props.setProdBase("/assets");
        props.setComponentExtension(".tsx");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("X")).isEqualTo("/react/X.tsx");
    }

    @Test
    void withProperties_prodModeExplicitProdBase_usesProdBase() {
        var props = new ReaktorProperties();
        props.setDevMode(false);
        props.setProdBase("/assets");
        props.setComponentExtension(".js");
        var resolver = new DefaultReaktorComponentResolver(props);
        assertThat(resolver.resolveScriptPath("X")).isEqualTo("/assets/X.js");
    }
}
