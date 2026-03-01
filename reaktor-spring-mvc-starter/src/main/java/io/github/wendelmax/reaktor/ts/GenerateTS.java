package io.github.wendelmax.reaktor.ts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class or record to be automatically exported to TypeScript.
 *
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GenerateTS {

    /**
     * Optional custom name for the generated TypeScript interface.
     * If empty, the Java class name will be used.
     *
     * @return the custom TypeScript interface name overrides.
     */
    String name() default "";
}
