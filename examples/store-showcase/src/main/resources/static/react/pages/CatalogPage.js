const { createElement } = window.React;
const { createRoot } = window.ReactDOM;
import { Layout } from '../components/Layout.js';
import { ProductCard } from '../components/ProductCard.js';

function CatalogPage({ products = [], currentCategory }) {
    return createElement(Layout, { title: currentCategory ? `Category: ${currentCategory}` : 'All Products' },
        createElement('div', { className: 'product-grid', style: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))' } },
            products.length > 0
                ? products.map(p => createElement(ProductCard, { key: p.id, product: p }))
                : createElement('p', null, 'No products found.')
        )
    );
}

// Hydration logic
document.querySelectorAll('[data-react-component="CatalogPage"]').forEach((el) => {
    const propsAttr = el.getAttribute('data-react-props');
    const props = propsAttr ? JSON.parse(propsAttr) : {};
    createRoot(el).render(createElement(CatalogPage, props));
});
