package io.github.wendlmax.reaktor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties mapped to the {@code reaktor.*} prefix in
 * {@code application.yml}.
 *
 * @since 0.1.0
 */
@ConfigurationProperties(prefix = "reaktor")
public class ReaktorProperties {

    private String componentBasePath = "/react";
    private String componentExtension = ".tsx";
    private String defaultLoadingComponent;
    private boolean devMode = false;
    private String devBase = "/react";
    private String prodBase;
    private Api api = new Api();

    /**
     * API specific properties Configuration mapping inside {@code reaktor.api.*}.
     */
    public static class Api {
        private List<String> corsAllowedOrigins = new ArrayList<>();

        /**
         * Gets the allowed origins for CORS.
         *
         * @return a list of allowed origin URLs.
         */
        public List<String> getCorsAllowedOrigins() {
            return corsAllowedOrigins;
        }

        /**
         * Sets the allowed origins for CORS.
         *
         * @param corsAllowedOrigins the list of allowed origin URLs.
         */
        public void setCorsAllowedOrigins(List<String> corsAllowedOrigins) {
            this.corsAllowedOrigins = corsAllowedOrigins;
        }
    }

    /**
     * Retrieves the nested API properties mapping.
     *
     * @return the {@link Api} sub-properties.
     */
    public Api getApi() {
        return api;
    }

    /**
     * Sets the nested API properties mapping.
     *
     * @param api the {@link Api} sub-properties block.
     */
    public void setApi(Api api) {
        this.api = api;
    }

    /**
     * Gets the base path where React components exist.
     *
     * @return the component base path. default is {@code /react}.
     */
    public String getComponentBasePath() {
        return componentBasePath;
    }

    /**
     * Sets the base directory path for components.
     *
     * @param componentBasePath the folder structure indicating the components root.
     */
    public void setComponentBasePath(String componentBasePath) {
        this.componentBasePath = componentBasePath;
    }

    /**
     * Gets the expected extension for component files automatically mounted.
     *
     * @return the file extension suffix. defaults to {@code .tsx}.
     */
    public String getComponentExtension() {
        return componentExtension;
    }

    /**
     * Sets the component extension mapping.
     *
     * @param componentExtension file extension suffix.
     */
    public void setComponentExtension(String componentExtension) {
        this.componentExtension = componentExtension;
    }

    /**
     * Gets the default loading or suspense component fallback.
     *
     * @return the default loading component name.
     */
    public String getDefaultLoadingComponent() {
        return defaultLoadingComponent;
    }

    /**
     * Sets the loading component fallback global.
     *
     * @param defaultLoadingComponent the exact React component string map.
     */
    public void setDefaultLoadingComponent(String defaultLoadingComponent) {
        this.defaultLoadingComponent = defaultLoadingComponent;
    }

    /**
     * Exposes if Dev Mode is active globally.
     *
     * @return true if Dev Mode is enabled, false otherwise.
     */
    public boolean isDevMode() {
        return devMode;
    }

    /**
     * Sets the Dev Mode.
     *
     * @param devMode true to enable Vite injection and non-caching component
     *                parsing.
     */
    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    /**
     * Gets the base module URI used strictly in Dev Mode.
     *
     * @return the dev URI.
     */
    public String getDevBase() {
        return devBase;
    }

    /**
     * Sets the Dev Base.
     *
     * @param devBase the dev mode base component injection URL.
     */
    public void setDevBase(String devBase) {
        this.devBase = devBase;
    }

    /**
     * Gets the static build URI base used strictly in Production Mode.
     *
     * @return the static base.
     */
    public String getProdBase() {
        return prodBase;
    }

    /**
     * Sets the Production compile base URL.
     *
     * @param prodBase mapping URL containing the built React components in prod
     *                 memory.
     */
    public void setProdBase(String prodBase) {
        this.prodBase = prodBase;
    }
}
