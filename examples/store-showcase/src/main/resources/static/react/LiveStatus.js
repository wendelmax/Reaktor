const { createElement, useState, useEffect } = window.React;

export function LiveStatus({ preloadUrl, refetchInterval }) {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);

    const fetchData = async () => {
        try {
            const response = await fetch(preloadUrl || '/products/api/status');
            const result = await response.json();
            if (result.success) {
                setData(result.data);
            }
        } catch (error) {
            console.error('Status fetch failed', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
        if (refetchInterval) {
            const timer = setInterval(fetchData, refetchInterval);
            return () => clearInterval(timer);
        }
    }, [preloadUrl, refetchInterval]);

    return createElement('div', { className: 'glass-panel', style: { padding: '1rem', marginBottom: '1rem', borderLeft: '4px solid var(--accent)' } },
        createElement('h4', { style: { marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' } },
            createElement('span', { className: 'pulse', style: { width: '10px', height: '10px', background: '#4ade80', borderRadius: '50%' } }),
            'Live System Status'
        ),
        loading ? createElement('p', null, 'Loading...') :
            data && createElement('div', { style: { fontSize: '0.875rem' } },
                createElement('p', null, `Status: ${data.status}`),
                createElement('p', null, `Active Users: ${data.activeUsers}`),
                createElement('p', { style: { color: 'var(--text-muted)', fontSize: '0.75rem' } }, `Server Time: ${data.serverTime}`)
            )
    );
}

// Hydration
document.querySelectorAll('[data-react-component="LiveStatus"]').forEach((el) => {
    const propsAttr = el.getAttribute('data-react-props');
    const props = propsAttr ? JSON.parse(propsAttr) : {};
    window.ReactDOM.createRoot(el).render(createElement(LiveStatus, props));
});
