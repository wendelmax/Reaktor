package io.github.wendelmax.reaktor.dialect;

/**
 * Abstract resolution strategy for locating the physical file path of a React
 * component.
 *
 * @since 0.1.0
 */
public interface ReaktorComponentResolver {

    /**
     * Resolves the frontend script URI path for a given component name.
     *
     * @param componentName the logical name of the React component (e.g.,
     *                      "products/Catalog").
     * @return the complete URI path resolvable by the browser (e.g.,
     *         "/react/products/Catalog.tsx").
     */
    String resolveScriptPath(String componentName);
}
