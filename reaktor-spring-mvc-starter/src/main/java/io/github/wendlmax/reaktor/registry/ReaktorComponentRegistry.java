package io.github.wendlmax.reaktor.registry;

import java.util.Optional;

public interface ReaktorComponentRegistry {

    Optional<String> resolveScriptPath(String componentName);

    static ReaktorComponentRegistry fromMap(java.util.Map<String, String> mappings) {
        return new MapBasedReaktorComponentRegistry(mappings);
    }

    class MapBasedReaktorComponentRegistry implements ReaktorComponentRegistry {
        private final java.util.Map<String, String> mappings;

        public MapBasedReaktorComponentRegistry(java.util.Map<String, String> mappings) {
            this.mappings = mappings != null ? java.util.Map.copyOf(mappings) : java.util.Map.of();
        }

        @Override
        public Optional<String> resolveScriptPath(String componentName) {
            return Optional.ofNullable(mappings.get(componentName));
        }
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private final java.util.Map<String, String> mappings = new java.util.LinkedHashMap<>();

        public Builder register(String componentName, String scriptPath) {
            mappings.put(componentName, scriptPath);
            return this;
        }

        public ReaktorComponentRegistry build() {
            return new MapBasedReaktorComponentRegistry(mappings);
        }
    }
}
