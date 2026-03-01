package io.github.wendelmax.reaktor.fragment;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for constructing multiple view fragments efficiently.
 * <p>
 * This provides a fluent builder API to compile a list of {@link ModelAndView}
 * objects
 * sequentially, which can then be returned via Reaktor controllers to render
 * multiple slots at once.
 * </p>
 *
 * @since 0.1.0
 */
public final class ReaktorFragments {

    private ReaktorFragments() {
    }

    /**
     * Initializes a new Fragment Builder starting with the given base view.
     *
     * @param viewName the name of the initial component fragment (e.g.,
     *                 "products/Card").
     * @return a mutable {@link Builder} instance.
     */
    public static Builder fragment(String viewName) {
        return new Builder().fragment(viewName);
    }

    /**
     * Fluent API builder for composing multiple fragment models sequentially.
     */
    public static class Builder {
        private final List<ModelAndView> fragments = new ArrayList<>();

        /**
         * Appends an empty view fragment to the rendering stack.
         *
         * @param viewName the name of the component view.
         * @return the fluent builder reference.
         */
        public Builder fragment(String viewName) {
            fragments.add(new ModelAndView(viewName));
            return this;
        }

        /**
         * Appends a view fragment containing a single named data attribute to the
         * rending stack.
         *
         * @param viewName   the name of the component view.
         * @param modelName  the attribute key.
         * @param modelValue the data object representation.
         * @return the fluent builder reference.
         */
        public Builder fragment(String viewName, String modelName, Object modelValue) {
            ModelAndView mav = new ModelAndView(viewName);
            mav.addObject(modelName, modelValue);
            fragments.add(mav);
            return this;
        }

        /**
         * Appends a view fragment parsing an entire block of contextual data into
         * bindings.
         *
         * @param viewName the name of the component view.
         * @param model    the dictionary of rendering properties.
         * @return the fluent builder reference.
         */
        public Builder fragment(String viewName, Map<String, ?> model) {
            ModelAndView mav = new ModelAndView(viewName);
            if (model != null) {
                mav.addAllObjects(model);
            }
            fragments.add(mav);
            return this;
        }

        /**
         * Finalizes the fluent composition block.
         *
         * @return an immutable list of the constructed Model And View fragments.
         */
        public List<ModelAndView> build() {
            return List.copyOf(fragments);
        }
    }
}
