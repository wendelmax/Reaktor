package io.github.wendlmax.reaktor.dialect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ReaktorComponentProcessorTest {

    private TemplateEngine templateEngine;

    @BeforeEach
    void setup() {
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(new StringTemplateResolver());
        var props = new io.github.wendlmax.reaktor.config.ReaktorProperties();
        props.setComponentBasePath("/react");
        props.setComponentExtension(".tsx");
        var resolver = new DefaultReaktorComponentResolver(props);
        templateEngine.addDialect(new ReaktorDialect(resolver, props));

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shouldRenderBasicComponent() {
        String template = "<react:component react:name=\"'MyComp'\" react:prop1=\"'val1'\" />";
        Context context = new Context();

        String result = templateEngine.process(template, context);

        assertThat(result).contains("data-react-component=\"&#39;MyComp&#39;\"");
        assertThat(result).contains("data-react-props='{\"prop1\":\"val1\"}'");
        assertThat(result).contains("src=\"/react/&#39;MyComp&#39;.tsx\"");
    }

    @Test
    void shouldRenderComponentWithSlots() {
        String template = "<react:component react:name=\"'Card'\">" +
                "  <react:slot react:name=\"'header'\"><h1>Title</h1></react:slot>" +
                "  <p>Body content</p>" +
                "</react:component>";
        Context context = new Context();

        String result = templateEngine.process(template, context);

        assertThat(result).contains("data-react-props=");
        assertThat(result).contains("\"slot&#39;header&#39;\":\"<h1>Title</h1>\"");
        assertThat(result).contains("\"children\":\"\\n  \\n  <p>Body content</p>\"");
    }

    @Test
    void shouldFilterPartialProps() {
        MockHttpServletRequest request = (MockHttpServletRequest) ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        request.addHeader("X-Partial-Props", "prop1");

        String template = "<react:component react:name=\"'MyComp'\" react:prop1=\"'val1'\" react:prop2=\"'val2'\" />";
        Context context = new Context();

        String result = templateEngine.process(template, context);

        assertThat(result).contains("\"prop1\":\"val1\"");
        assertThat(result).doesNotContain("\"prop2\":\"val2\"");
    }
}
