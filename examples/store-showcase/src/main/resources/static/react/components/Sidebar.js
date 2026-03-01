const { createElement } = window.React;

export function Sidebar({ categories, onSearch, currentCategory }) {
    return createElement('aside', { className: 'sidebar glass-panel' },
        createElement('div', { style: { marginBottom: '2rem' } },
            createElement('h4', { style: { marginBottom: '1rem', color: 'var(--secondary)' } }, 'Search'),
            createElement('input', {
                type: 'text',
                placeholder: 'Looking for something?',
                onChange: (e) => onSearch(e.target.value),
                style: {
                    width: '100%',
                    padding: '0.75rem',
                    borderRadius: '8px',
                    border: '1px solid var(--glass-border)',
                    background: 'rgba(255,255,255,0.05)',
                    color: 'white',
                    outline: 'none'
                }
            })
        ),
        createElement('div', null,
            createElement('h4', { style: { marginBottom: '1rem', color: 'var(--secondary)' } }, 'Categories'),
            createElement('ul', { style: { listStyle: 'none' } },
                createElement('li', { style: { marginBottom: '0.5rem' } },
                    createElement('a', {
                        href: '/products',
                        style: { color: !currentCategory ? 'var(--accent)' : 'var(--text-muted)', textDecoration: 'none' }
                    }, 'All Categories')
                ),
                categories.map(cat =>
                    createElement('li', { key: cat, style: { marginBottom: '0.5rem' } },
                        createElement('a', {
                            href: `/products?category=${cat}`,
                            style: { color: currentCategory === cat ? 'var(--accent)' : 'var(--text-muted)', textDecoration: 'none' }
                        }, cat.charAt(0).toUpperCase() + cat.slice(1))
                    )
                )
            )
        )
    );
}
