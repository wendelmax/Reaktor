const { useState, createElement } = React;

export function ContactForm() {
    const [formData, setFormData] = useState({ name: '', email: '', message: '' });
    const [status, setStatus] = useState({ type: 'idle', message: '', errors: {} });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
        // Clear error for field
        if (status.errors[name]) {
            setStatus(prev => ({ ...prev, errors: { ...prev.errors, [name]: null } }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setStatus({ type: 'loading', message: 'Sending...', errors: {} });

        try {
            const response = await fetch('/api/contact', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            });

            const result = await response.json();

            if (result.success) {
                setStatus({ type: 'success', message: result.data, errors: {} });
                setFormData({ name: '', email: '', message: '' });
            } else {
                // Mapping field errors from simplified ReaktorResponse
                const fieldErrors = {};
                if (result.errors) {
                    result.errors.forEach(err => {
                        fieldErrors[err.field] = err.message;
                    });
                }
                setStatus({ type: 'error', message: result.message || 'Check the fields below', errors: fieldErrors });
            }
        } catch (err) {
            setStatus({ type: 'error', message: 'Connection failed', errors: {} });
        }
    };

    const inputStyle = (field) => ({
        width: '100%',
        padding: '10px',
        border: `1px solid ${status.errors[field] ? '#dc3545' : '#ccc'}`,
        borderRadius: '4px',
        marginBottom: '5px'
    });

    return createElement('form', { onSubmit: handleSubmit, style: { display: 'flex', flexDirection: 'column', gap: '15px' } },
        createElement('div', null,
            createElement('label', { style: { display: 'block', marginBottom: '5px' } }, 'Name'),
            createElement('input', { name: 'name', value: formData.name, onChange: handleChange, style: inputStyle('name') }),
            status.errors.name && createElement('span', { style: { color: '#dc3545', fontSize: '0.8rem' } }, status.errors.name)
        ),
        createElement('div', null,
            createElement('label', { style: { display: 'block', marginBottom: '5px' } }, 'Email'),
            createElement('input', { name: 'email', type: 'email', value: formData.email, onChange: handleChange, style: inputStyle('email') }),
            status.errors.email && createElement('span', { style: { color: '#dc3545', fontSize: '0.8rem' } }, status.errors.email)
        ),
        createElement('div', null,
            createElement('label', { style: { display: 'block', marginBottom: '5px' } }, 'Message'),
            createElement('textarea', { name: 'message', value: formData.message, onChange: handleChange, style: { ...inputStyle('message'), height: '100px' } }),
            status.errors.message && createElement('span', { style: { color: '#dc3545', fontSize: '0.8rem' } }, status.errors.message)
        ),
        createElement('button', {
            disabled: status.type === 'loading',
            type: 'submit',
            style: {
                padding: '12px',
                background: '#007bff',
                color: 'white',
                border: 'none',
                borderRadius: '4px',
                cursor: status.type === 'loading' ? 'not-allowed' : 'pointer'
            }
        }, status.type === 'loading' ? 'Submitting...' : 'Send Message'),

        status.message && createElement('div', {
            style: {
                padding: '10px',
                borderRadius: '4px',
                background: status.type === 'success' ? '#d4edda' : '#f8d7da',
                color: status.type === 'success' ? '#155724' : '#721c24'
            }
        }, status.message)
    );
}
