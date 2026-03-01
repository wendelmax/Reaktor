package io.github.wendlmax.reaktor.dialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;

public class ReaktorContextProcessor extends AbstractAttributeTagProcessor {

    private static final String ATTR_NAME = "context";
    private static final int PRECEDENCE = 1100;
    private final ObjectMapper objectMapper;

    protected ReaktorContextProcessor(String dialectPrefix, ObjectMapper objectMapper) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        Object reaktorContext = context.getVariable("__reaktor_context");
        if (!(reaktorContext instanceof Map)) {
            return;
        }

        try {
            String json = objectMapper.writeValueAsString(reaktorContext);
            IModelFactory modelFactory = context.getModelFactory();
            IModel scriptModel = modelFactory.createModel();

            scriptModel
                    .add(modelFactory.createText("\n<script id=\"__reaktor_context__\" type=\"application/json\">\n"));
            scriptModel.add(modelFactory.createText(json));
            scriptModel.add(modelFactory.createText("\n</script>\n"));
            scriptModel.add(modelFactory.createText("<script>\n"));
            scriptModel.add(modelFactory.createText(
                    "  window.__REAKTOR_CONTEXT__ = JSON.parse(document.getElementById('__reaktor_context__').textContent);\n"));
            scriptModel.add(modelFactory.createText("</script>\n"));

            structureHandler.insertImmediatelyAfter(scriptModel, false);
        } catch (Exception e) {
            // Log error or ignore
        }
    }
}
