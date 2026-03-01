package io.github.wendelmax.reaktor.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.net.URI;

/**
 * The standard uniform server response mechanism used by Reaktor.
 * <p>
 * This record wraps the Spring {@link ResponseEntity} exposing fluent APIs to
 * construct
 * view triggers, API JSON payloads, redirects, and state contexts (like
 * TempData or Flash Attributes).
 * </p>
 *
 * @param <T>     The payload data type.
 * @param entity  The underlying HTTP response structure.
 * @param success Indicates whether the operation was successful.
 * @param message An optional global message (e.g., feedback or toast message).
 * @param errors  An optional list of structured field validation errors.
 * @param view    An optional explicit view name to be rendered by
 *                Thymeleaf/Reaktor.
 * @param context Additional metadata and layout context passed seamlessly to
 *                React.
 * @since 0.1.0
 */
public record ReaktorResponse<T>(
        ResponseEntity<T> entity,
        boolean success,
        String message,
        List<Error> errors,
        String view,
        java.util.Map<String, Object> context) {
    /**
     * Represents a discrete error typically resulting from form field validation.
     *
     * @param code    a system message code.
     * @param field   the form field producing the violation.
     * @param message the localized human-readable reason.
     */
    public record Error(String code, String field, String message) {
    }

    public ResponseEntity<T> toResponseEntity() {
        return entity;
    }

    /**
     * Converts a raw data model into a successful 200 OK ReaktorResponse.
     *
     * @param <T>  the data payload type.
     * @param data the data model object.
     * @return the constructed ReaktorResponse.
     */
    public static <T> ReaktorResponse<T> ok(T data) {
        return new ReaktorResponse<>(ResponseEntity.ok(data), true, null, null, null, java.util.Collections.emptyMap());
    }

    public static <T> ReaktorResponse<T> ok() {
        return ok((T) null);
    }

    /**
     * Unwraps an Optional, returning 200 OK if present or returning 404 NOT FOUND
     * if empty.
     *
     * @param <T>      the type encapsulated by the Optional.
     * @param optional the nullable container evaluation.
     * @return 200 OK with payload or 404.
     */
    public static <T> ReaktorResponse<T> ok(java.util.Optional<T> optional) {
        return optional.map(ReaktorResponse::ok).orElseGet(() -> ReaktorResponse.notFound());
    }

    /**
     * Explicitly requests a specific frontend React component to be rendered as the
     * main layout view.
     *
     * @param <T>      the empty payload type.
     * @param viewName the script path mapped to a component (e.g. "auth/Login").
     * @return the constructed ReaktorResponse holding the specific view name
     *         trigger.
     */
    public static <T> ReaktorResponse<T> view(String viewName) {
        return new ReaktorResponse<>(ResponseEntity.ok().build(), true, null, null, viewName,
                java.util.Collections.emptyMap());
    }

    public static <T> ReaktorResponse<T> view(String viewName, T data) {
        return new ReaktorResponse<>(ResponseEntity.ok(data), true, null, null, viewName,
                java.util.Collections.emptyMap());
    }

    /**
     * Yields a 400 Bad Request error containing a global string message.
     *
     * @param <T>     the empty payload type.
     * @param message human readable error.
     * @return the error response.
     */
    public static <T> ReaktorResponse<T> error(String message) {
        return new ReaktorResponse<>(ResponseEntity.badRequest().build(), false, message, null, null,
                java.util.Collections.emptyMap());
    }

    public static <T> ReaktorResponse<T> error(List<Error> errors) {
        return new ReaktorResponse<>(ResponseEntity.badRequest().build(), false, null, errors, null,
                java.util.Collections.emptyMap());
    }

    /**
     * Yields a 201 Created successfully executed response.
     *
     * @param <T>      the data type.
     * @param location the new resource URI location header.
     * @param data     the created representation.
     * @return the response object.
     */
    public static <T> ReaktorResponse<T> created(URI location, T data) {
        return new ReaktorResponse<>(ResponseEntity.created(location).body(data), true, null, null, null,
                java.util.Collections.emptyMap());
    }

    public static <T> ReaktorResponse<T> notFound() {
        return new ReaktorResponse<>(ResponseEntity.notFound().build(), false, "Resource not found", null, null,
                java.util.Collections.emptyMap());
    }

    /**
     * Yields a 302 Redirect operation commanding the browser to fetch a different
     * controller path.
     * Allows chained operations using Flash data via the {@code withContext()}
     * builders.
     *
     * @param <T> the empty type.
     * @param url the destination mapping URL.
     * @return the redirect instruction response.
     */
    public static <T> ReaktorResponse<T> redirect(String url) {
        return new ReaktorResponse<>(ResponseEntity.status(HttpStatus.FOUND).build(), true, null, null,
                "redirect:" + url, java.util.Collections.emptyMap());
    }

    public ReaktorResponse<T> withData(T data) {
        ResponseEntity<T> newEntity = ResponseEntity.status(entity.getStatusCode()).headers(entity.getHeaders())
                .body(data);
        return new ReaktorResponse<>(newEntity, success, message, errors, view, context);
    }

    /**
     * Binds Spring's BindingResult constraint violations onto the structural
     * response payload.
     * This turns the current response into a 400 Bad Request, extracting all
     * violations neatly.
     *
     * @param springErrors the evaluated Spring Validation {@code Errors} output.
     * @return a new mutated ReaktorResponse loaded with parsed {@link Error} field
     *         data.
     */
    public ReaktorResponse<T> withErrors(org.springframework.validation.Errors springErrors) {
        if (springErrors == null || !springErrors.hasErrors()) {
            return this;
        }

        java.util.List<Error> newErrors = new java.util.ArrayList<>();
        if (this.errors != null) {
            newErrors.addAll(this.errors);
        }

        springErrors.getFieldErrors()
                .forEach(e -> newErrors.add(new Error(e.getCode(), e.getField(), e.getDefaultMessage())));
        springErrors.getGlobalErrors().forEach(e -> newErrors.add(new Error(e.getCode(), null, e.getDefaultMessage())));

        ResponseEntity<T> badRequestEntity = ResponseEntity.badRequest().headers(entity.getHeaders())
                .body(entity.getBody());
        return new ReaktorResponse<>(badRequestEntity, false, message, newErrors, view, context);
    }

    public ReaktorResponse<T> withView(String viewName) {
        return new ReaktorResponse<>(entity, success, message, errors, viewName, context);
    }

    public ReaktorResponse<T> withRedirect(String url) {
        ResponseEntity<T> redirectEntity = ResponseEntity.status(HttpStatus.FOUND).headers(entity.getHeaders())
                .body(entity.getBody());
        return new ReaktorResponse<>(redirectEntity, success, message, errors, "redirect:" + url, context);
    }

    /**
     * Injects a specific property map entry dynamically into the payload
     * representation body.
     * Works natively if the data is a Map context.
     *
     * @param name  json payload key.
     * @param value json payload value object.
     * @return copy yielding the updated payload model.
     */
    @SuppressWarnings("unchecked")
    public ReaktorResponse<Object> with(String name, Object value) {
        java.util.Map<String, Object> newData = extractBodyAsMap();
        newData.put(name, value);
        return new ReaktorResponse<>(shallowCopyEntity(newData), success, message, errors, view, context);
    }

    @SuppressWarnings("unchecked")
    public ReaktorResponse<Object> withAll(java.util.Map<String, ?> attributes) {
        java.util.Map<String, Object> newData = extractBodyAsMap();
        newData.putAll(attributes);
        return new ReaktorResponse<>(shallowCopyEntity(newData), success, message, errors, view, context);
    }

    @SuppressWarnings("unchecked")
    private java.util.Map<String, Object> extractBodyAsMap() {
        if (entity.getBody() instanceof java.util.Map) {
            return new java.util.HashMap<>((java.util.Map<String, Object>) entity.getBody());
        }
        java.util.Map<String, Object> newData = new java.util.HashMap<>();
        if (entity.getBody() != null) {
            newData.put("data", entity.getBody());
        }
        return newData;
    }

    private <R> ResponseEntity<R> shallowCopyEntity(R newBody) {
        return ResponseEntity.status(entity.getStatusCode()).headers(entity.getHeaders()).body(newBody);
    }

    /**
     * Binds Flash architectural data or generic runtime states into the window
     * context payload seamlessly.
     * Extremely useful when chaining Redirects.
     *
     * @param name  the name of the memory context key.
     * @param value the memory state object to embed.
     * @return a mutated copy with the added context payload.
     */
    public ReaktorResponse<T> withContext(String name, Object value) {
        java.util.Map<String, Object> newContext = new java.util.HashMap<>(context);
        newContext.put(name, value);
        return new ReaktorResponse<>(entity, success, message, errors, view, newContext);
    }

    public ReaktorResponse<T> withStatus(HttpStatus status) {
        ResponseEntity<T> newEntity = ResponseEntity.status(status).headers(entity.getHeaders()).body(entity.getBody());
        return new ReaktorResponse<>(newEntity, success, message, errors, view, context);
    }

    // JSON ignore these metadata fields when serializing to client if needed
    // But for now, let's keep it simple and see how it looks.
}
