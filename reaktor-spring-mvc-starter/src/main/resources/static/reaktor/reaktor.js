import React, { useState, useEffect, useContext, createContext } from 'react';

const ReaktorContext = createContext({
    props: {},
    context: {},
    errors: [],
    success: true,
    message: null
});

/**
 * Provider to wrap Reaktor components and provide access to global state and validation.
 */
export function ReaktorProvider({ children, props = {}, context = {} }) {
    const value = {
        props,
        context: context || window.__REAKTOR_CONTEXT__ || {},
        errors: props.errors || [],
        success: props.success !== undefined ? props.success : true,
        message: props.message || null
    };

    return React.createElement(ReaktorContext.Provider, { value }, children);
}

/**
 * Hook to access the Reaktor Context passed from the Spring Controller.
 */
export function useReaktorContext() {
    const { context } = useContext(ReaktorContext);
    const [localContext, setLocalContext] = useState(context);

    useEffect(() => {
        const handleContextUpdate = () => {
            if (window.__REAKTOR_CONTEXT__) {
                setLocalContext(window.__REAKTOR_CONTEXT__);
            }
        };
        window.addEventListener('reaktor-context-updated', handleContextUpdate);
        return () => window.removeEventListener('reaktor-context-updated', handleContextUpdate);
    }, []);

    return localContext;
}

/**
 * Hook to access validation errors passed from Spring's BindingResult.
 */
export function useReaktorErrors() {
    return useContext(ReaktorContext).errors;
}

/**
 * Hook to access the success status and global message.
 */
export function useReaktorStatus() {
    const { success, message } = useContext(ReaktorContext);
    return { success, message };
}

/**
 * Helper to render/hydrate a Reaktor component with the necessary providers.
 */
export function renderReaktorComponent(Component, rootElement) {
    const propsAttr = rootElement.getAttribute('data-react-props');
    const props = propsAttr ? JSON.parse(propsAttr) : {};

    const root = window.ReactDOM.createRoot(rootElement);
    root.render(
        React.createElement(ReaktorProvider, { props },
            React.createElement(Component, props)
        )
    );
}
