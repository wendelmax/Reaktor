package io.github.wendlmax.reaktor.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ReaktorResponseReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final List<HttpMessageConverter<?>> messageConverters;

    public ReaktorResponseReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters != null && !messageConverters.isEmpty()
                ? messageConverters
                : List.of(new MappingJackson2HttpMessageConverter(new ObjectMapper()));
    }

    public ReaktorResponseReturnValueHandler(ObjectMapper objectMapper) {
        this.messageConverters = List.of(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return ReaktorResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            return;
        }

        ReaktorResponse<?> reaktorResponse = (ReaktorResponse<?>) returnValue;
        boolean isResponseBody = returnType
                .hasMethodAnnotation(org.springframework.web.bind.annotation.ResponseBody.class)
                || returnType.getContainingClass()
                        .isAnnotationPresent(org.springframework.web.bind.annotation.ResponseBody.class);

        if (isResponseBody) {
            handleJsonResponse(reaktorResponse, mavContainer, webRequest);
        } else {
            handleViewResponse(reaktorResponse, returnType, mavContainer, webRequest);
        }
    }

    private void handleJsonResponse(ReaktorResponse<?> reaktorResponse, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(response);

        // Set status and headers from ReaktorResponse's underlying ResponseEntity
        outputMessage.setStatusCode(reaktorResponse.entity().getStatusCode());
        if (reaktorResponse.entity().getHeaders() != null) {
            outputMessage.getHeaders().addAll(reaktorResponse.entity().getHeaders());
        }

        // Write the ReaktorResponse itself as the body
        MediaType contentType = outputMessage.getHeaders().getContentType();
        if (contentType == null) {
            contentType = MediaType.APPLICATION_JSON;
        }

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);

        writeWithMessageConverter(reaktorResponse, reaktorResponse.getClass(), contentType, inputMessage,
                outputMessage);
    }

    @SuppressWarnings("unchecked")
    private void handleViewResponse(ReaktorResponse<?> reaktorResponse, MethodParameter returnType,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {
        String viewName = reaktorResponse.view();

        if (viewName == null) {
            // Reaktor Convention: controllerPrefix/methodName
            String controllerName = returnType.getContainingClass().getSimpleName();
            if (controllerName.endsWith("Controller")) {
                controllerName = controllerName.substring(0, controllerName.length() - 10);
            }
            // CamelCase to kebab-case or just lowercase?
            // Let's go with simple lowercase for now as requested by user example
            // "basics/counter"
            controllerName = controllerName.toLowerCase();
            String methodName = returnType.getMethod().getName();
            viewName = controllerName + "/" + methodName;
        }

        mavContainer.setViewName(viewName);

        Object responseBody = reaktorResponse.entity() != null ? reaktorResponse.entity().getBody() : null;

        if (responseBody != null) {
            if (responseBody instanceof java.util.Map) {
                mavContainer.addAllAttributes((java.util.Map<String, ?>) responseBody);
            } else {
                mavContainer.addAttribute("data", responseBody);
            }
        }

        // Expose errors and success status
        if (reaktorResponse.errors() != null && !reaktorResponse.errors().isEmpty()) {
            mavContainer.addAttribute("errors", reaktorResponse.errors());
        }
        mavContainer.addAttribute("success", reaktorResponse.success());
        if (reaktorResponse.message() != null) {
            mavContainer.addAttribute("message", reaktorResponse.message());
        }

        java.util.Map<String, Object> combinedContext = new java.util.HashMap<>();

        // 1. Load Flash Attributes (TempData)
        HttpServletRequest request = ((NativeWebRequest) webRequest).getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            java.util.Map<String, ?> flashAttributes = org.springframework.web.servlet.support.RequestContextUtils
                    .getInputFlashMap(request);
            if (flashAttributes != null) {
                combinedContext.putAll(flashAttributes);
            }
        }

        // 2. Merge Reaktor Context (Current Request)
        if (reaktorResponse.context() != null) {
            combinedContext.putAll(reaktorResponse.context());
        }

        if (!combinedContext.isEmpty()) {
            mavContainer.addAttribute("__reaktor_context", combinedContext);
        }

        // Set status if possible
        if (reaktorResponse.entity() != null
                && reaktorResponse.entity().getStatusCode() != org.springframework.http.HttpStatus.OK) {
            if (reaktorResponse.entity().getStatusCode() instanceof org.springframework.http.HttpStatus s) {
                mavContainer.setStatus(s);
            }
        }
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path != null && path.startsWith("/api");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void writeWithMessageConverter(Object body, Class<?> valueType, MediaType contentType,
            ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage) throws Exception {
        for (HttpMessageConverter converter : messageConverters) {
            if (converter.canWrite(valueType, contentType)) {
                converter.write(body, contentType, outputMessage);
                return;
            }
        }
        throw new IllegalStateException("No HttpMessageConverter for " + valueType + " and " + contentType);
    }
}
