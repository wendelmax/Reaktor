package io.github.wendlmax.reaktor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "reaktor")
public class ReaktorProperties {

    private String componentBasePath = "/react";
    private String componentExtension = ".tsx";
    private String defaultLoadingComponent;
    private boolean devMode = false;
    private String devBase = "/react";
    private String prodBase;
    private Api api = new Api();

    public static class Api {
        private List<String> corsAllowedOrigins = new ArrayList<>();

        public List<String> getCorsAllowedOrigins() {
            return corsAllowedOrigins;
        }

        public void setCorsAllowedOrigins(List<String> corsAllowedOrigins) {
            this.corsAllowedOrigins = corsAllowedOrigins;
        }
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public String getComponentBasePath() {
        return componentBasePath;
    }

    public void setComponentBasePath(String componentBasePath) {
        this.componentBasePath = componentBasePath;
    }

    public String getComponentExtension() {
        return componentExtension;
    }

    public void setComponentExtension(String componentExtension) {
        this.componentExtension = componentExtension;
    }

    public String getDefaultLoadingComponent() {
        return defaultLoadingComponent;
    }

    public void setDefaultLoadingComponent(String defaultLoadingComponent) {
        this.defaultLoadingComponent = defaultLoadingComponent;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public String getDevBase() {
        return devBase;
    }

    public void setDevBase(String devBase) {
        this.devBase = devBase;
    }

    public String getProdBase() {
        return prodBase;
    }

    public void setProdBase(String prodBase) {
        this.prodBase = prodBase;
    }
}
