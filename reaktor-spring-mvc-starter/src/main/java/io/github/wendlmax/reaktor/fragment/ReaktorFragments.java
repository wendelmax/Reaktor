package io.github.wendlmax.reaktor.fragment;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ReaktorFragments {

    private ReaktorFragments() {
    }

    public static Builder fragment(String viewName) {
        return new Builder().fragment(viewName);
    }

    public static class Builder {
        private final List<ModelAndView> fragments = new ArrayList<>();

        public Builder fragment(String viewName) {
            fragments.add(new ModelAndView(viewName));
            return this;
        }

        public Builder fragment(String viewName, String modelName, Object modelValue) {
            ModelAndView mav = new ModelAndView(viewName);
            mav.addObject(modelName, modelValue);
            fragments.add(mav);
            return this;
        }

        public Builder fragment(String viewName, Map<String, ?> model) {
            ModelAndView mav = new ModelAndView(viewName);
            if (model != null) {
                mav.addAllObjects(model);
            }
            fragments.add(mav);
            return this;
        }

        public List<ModelAndView> build() {
            return List.copyOf(fragments);
        }
    }
}
