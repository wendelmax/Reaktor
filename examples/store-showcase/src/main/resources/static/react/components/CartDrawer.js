const { createElement } = window.React;

export function CartDrawer({ isOpen, items, onClose, onRemove }) {
    return createElement('div', { className: `cart-drawer ${isOpen ? 'open' : ''}` },
        createElement('div', { style: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' } },
            createElement('h2', null, 'Your Cart'),
            createElement('button', {
                onClick: onClose,
                style: { background: 'transparent', border: 'none', color: 'white', fontSize: '1.5rem', cursor: 'pointer' }
            }, '×')
        ),
        createElement('div', { style: { overflowY: 'auto', maxHeight: 'calc(100vh - 200px)' } },
            items.length === 0 ? createElement('p', { style: { color: 'var(--text-muted)' } }, 'Cart is empty') :
                items.map((item, idx) =>
                    createElement('div', { key: `${item.id}-${idx}`, className: 'cart-item' },
                        createElement('div', null,
                            createElement('p', { style: { fontWeight: 'bold' } }, item.name),
                            createElement('p', { style: { fontSize: '0.8rem', color: 'var(--text-muted)' } }, item.category)
                        ),
                        createElement('button', {
                            onClick: () => onRemove(idx),
                            style: { background: 'transparent', border: 'none', color: '#ff4444', cursor: 'pointer' }
                        }, 'Remove')
                    )
                )
        ),
        items.length > 0 && createElement('div', { style: { marginTop: '2rem' } },
            createElement('button', { className: 'btn btn-primary', style: { width: '100%' } }, 'Checkout Now')
        )
    );
}
