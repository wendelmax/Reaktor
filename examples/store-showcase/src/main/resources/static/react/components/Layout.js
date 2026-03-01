const { createElement } = window.React;

export function Layout({ children }) {
    return createElement('div', { className: 'app-container' },
        children
    );
}
