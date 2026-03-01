const { createElement } = window.React;

export function Navbar({ cartCount, onCartClick }) {
    return createElement('nav', { className: 'navbar glass-panel' },
        createElement('div', { className: 'logo', style: { fontSize: '1.5rem', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '0.5rem' } },
            createElement('span', { style: { color: 'var(--primary)' } }, '⚛'),
            'Reaktor Store'
        ),
        createElement('div', { className: 'nav-actions', style: { display: 'flex', gap: '1.5rem', alignItems: 'center' } },
            createElement('button', {
                className: 'btn',
                style: { background: 'transparent', color: 'var(--text-main)', position: 'relative' },
                onClick: onCartClick
            },
                '🛒 Cart',
                cartCount > 0 && createElement('span', { className: 'cart-badge', style: { position: 'absolute', top: '-5px', right: '-5px' } }, cartCount)
            )
        )
    );
}
