const { createElement } = window.React;

export function ProductCard({ product, onAddToCart, slotHeader, children }) {
    return createElement('div', { className: 'product-card glass-panel animate-fade-in' },
        slotHeader && createElement('div', {
            className: 'slot-header',
            style: { padding: '0.5rem', background: 'rgba(99, 102, 241, 0.2)', fontSize: '0.75rem', textAlign: 'center' },
            dangerouslySetInnerHTML: { __html: slotHeader }
        }),
        createElement('div', {
            style: {
                height: '140px',
                background: 'linear-gradient(45deg, #1e293b, #334155)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontSize: '2.5rem'
            }
        }, '📦'),
        createElement('div', { className: 'card-content' },
            createElement('span', { className: 'product-category' }, product.category),
            createElement('h3', { className: 'product-title' }, product.name),
            children && createElement('div', {
                className: 'slot-children',
                style: { margin: '0.5rem 0', fontSize: '0.875rem', color: 'var(--text-muted)' },
                dangerouslySetInnerHTML: { __html: children }
            }),
            createElement('div', {
                style: {
                    marginTop: 'auto',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                }
            },
                createElement('span', { style: { fontSize: '1.25rem', fontWeight: 'bold', color: 'var(--accent)' } }, '$99.99'),
                createElement('button', {
                    className: 'btn btn-primary',
                    onClick: () => onAddToCart ? onAddToCart(product) : alert('Added!')
                }, 'Add to Cart')
            )
        )
    );
}

// Hydration
document.querySelectorAll('[data-react-component="ProductCard"]').forEach((el) => {
    const propsAttr = el.getAttribute('data-react-props');
    const props = propsAttr ? JSON.parse(propsAttr) : {};
    window.ReactDOM.createRoot(el).render(createElement(ProductCard, props));
});
