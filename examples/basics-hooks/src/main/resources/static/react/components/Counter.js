const { useState, useEffect, createElement } = React;

export function Counter({ initialValue = 0 }) {
    const [count, setCount] = useState(initialValue);
    const [lastAction, setLastAction] = useState('None');

    // Using the global Reaktor Context
    const reaktorContext = window.__REAKTOR_CONTEXT__ || {};

    useEffect(() => {
        console.log(`Count changed to: ${count}`);
    }, [count]);

    return createElement('div', {
        className: 'counter-container',
        style: { border: reaktorContext.siteTheme === 'dark' ? '2px solid #555' : '1px solid #ccc', padding: '20px', borderRadius: '8px' }
    },
        createElement('h2', null, `Count: ${count}`),
        createElement('p', null, `Last Action: ${lastAction}`),
        reaktorContext.flashMessage && createElement('p', {
            style: { padding: '10px', background: '#d4edda', color: '#155724', borderRadius: '4px', margin: '10px 0' }
        }, reaktorContext.flashMessage),
        createElement('p', { style: { fontSize: '0.8em', color: '#666' } }, `Theme: ${reaktorContext.siteTheme}, User: ${reaktorContext.userRole}`),
        createElement('div', { style: { display: 'flex', gap: '10px', justifyContent: 'center', marginTop: '10px' } },
            createElement('button', {
                onClick: () => {
                    setCount(c => c + 1);
                    setLastAction('Increment');
                },
                style: { padding: '10px 20px', cursor: 'pointer', background: '#4CAF50', color: 'white', border: 'none', borderRadius: '4px' }
            }, '+'),
            createElement('button', {
                onClick: () => {
                    setCount(c => c - 1);
                    setLastAction('Decrement');
                },
                style: { padding: '10px 20px', cursor: 'pointer', background: '#f44336', color: 'white', border: 'none', borderRadius: '4px' }
            }, '-')
        )
    );
}
