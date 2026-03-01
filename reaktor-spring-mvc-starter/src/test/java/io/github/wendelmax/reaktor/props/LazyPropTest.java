package io.github.wendelmax.reaktor.props;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class LazyPropTest {

    @Test
    void of_withSupplier_returnsValue() {
        assertThat(LazyProp.of(() -> "value")).isEqualTo("value");
    }

    @Test
    void of_withNullSupplier_returnsNull() {
        Object result = LazyProp.of(null);
        assertThat(result == null).isTrue();
    }

    @Test
    void optional_withSupplier_returnsOptionalOf() {
        assertThat(LazyProp.optional(() -> "x")).hasValue("x");
    }

    @Test
    void optional_withNullSupplier_returnsEmpty() {
        Optional<String> result = LazyProp.optional(null);
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void optional_withSupplierReturningNull_returnsEmpty() {
        Optional<String> result = LazyProp.optional(() -> (String) null);
        assertThat(result.isEmpty()).isTrue();
    }
}
