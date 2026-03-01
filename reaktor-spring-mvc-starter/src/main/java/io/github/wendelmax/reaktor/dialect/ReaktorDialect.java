package io.github.wendelmax.reaktor.dialect;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

/**
 * The standard Thymeleaf Custom Dialect for Reaktor.
 * <p>
 * This dialect exposes the {@code react:component} and {@code react:context}
 * processing instructions.
 * </p>
 *
 * @since 0.1.0
 */
public class ReaktorDialect extends AbstractProcessorDialect {

    private static final String DIALECT_NAME = "Reaktor";
    private static final String PREFIX = "react";
    private static final int PROCESSOR_PRECEDENCE = 1000;

    private final ReaktorComponentResolver componentResolver;
    private final io.github.wendelmax.reaktor.config.ReaktorProperties properties;

    /**
     * Creates the Dialect integrating it into the Spring Engine.
     *
     * @param componentResolver the path mapping resolver.
     * @param properties        the application Reaktor property definitions.
     */
    public ReaktorDialect(ReaktorComponentResolver componentResolver,
            io.github.wendelmax.reaktor.config.ReaktorProperties properties) {
        super(DIALECT_NAME, PREFIX, PROCESSOR_PRECEDENCE);
        this.componentResolver = componentResolver;
        this.properties = properties;
    }

    /**
     * Discovers processors mapped by this dialect.
     *
     * @param dialectPrefix the namespace prefix, normally {@code react}.
     * @return a strictly immutable set of executable Element and Attribute
     *         processors.
     */
    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return Set.of(
                new ReaktorComponentProcessor(dialectPrefix, componentResolver, properties),
                new ReaktorContextProcessor(dialectPrefix, null));
    }
}
