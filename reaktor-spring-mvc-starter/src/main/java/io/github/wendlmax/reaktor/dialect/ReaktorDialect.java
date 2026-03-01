package io.github.wendlmax.reaktor.dialect;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

public class ReaktorDialect extends AbstractProcessorDialect {

    private static final String DIALECT_NAME = "Reaktor";
    private static final String PREFIX = "react";
    private static final int PROCESSOR_PRECEDENCE = 1000;

    private final ReaktorComponentResolver componentResolver;
    private final io.github.wendlmax.reaktor.config.ReaktorProperties properties;

    public ReaktorDialect(ReaktorComponentResolver componentResolver,
            io.github.wendlmax.reaktor.config.ReaktorProperties properties) {
        super(DIALECT_NAME, PREFIX, PROCESSOR_PRECEDENCE);
        this.componentResolver = componentResolver;
        this.properties = properties;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return Set.of(
                new ReaktorComponentProcessor(dialectPrefix, componentResolver, properties),
                new ReaktorContextProcessor(dialectPrefix, null));
    }
}
