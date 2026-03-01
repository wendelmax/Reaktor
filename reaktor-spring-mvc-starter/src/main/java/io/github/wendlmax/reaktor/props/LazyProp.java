package io.github.wendlmax.reaktor.props;

import java.util.Optional;
import java.util.function.Supplier;

public final class LazyProp {

    private LazyProp() {
    }

    public static <T> T of(Supplier<T> supplier) {
        return supplier != null ? supplier.get() : null;
    }

    public static <T> Optional<T> optional(Supplier<T> supplier) {
        if (supplier == null) {
            return Optional.empty();
        }
        T value = supplier.get();
        return value != null ? Optional.of(value) : Optional.empty();
    }
}
