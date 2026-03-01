package io.github.wendelmax.reaktor.registry;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReaktorComponentRegistryTest {

    @Test
    void builder_registersComponents() {
        var registry = ReaktorComponentRegistry.builder()
                .register("ProductList", "/custom/ProductList.js")
                .register("Cart", "/assets/Cart.js")
                .build();
        assertThat(registry.resolveScriptPath("ProductList")).hasValue("/custom/ProductList.js");
        assertThat(registry.resolveScriptPath("Cart")).hasValue("/assets/Cart.js");
    }

    @Test
    void fromMap_createsRegistry() {
        var registry = ReaktorComponentRegistry.fromMap(
                java.util.Map.of("X", "/x.js", "Y", "/y.js"));
        assertThat(registry.resolveScriptPath("X")).hasValue("/x.js");
        assertThat(registry.resolveScriptPath("Z")).isEmpty();
    }

    @Test
    void resolveScriptPath_unknownComponent_returnsEmpty() {
        var registry = ReaktorComponentRegistry.builder()
                .register("A", "/a.js")
                .build();
        assertThat(registry.resolveScriptPath("B")).isEmpty();
    }
}
