const { createElement } = window.React;

export function StudentDelete({ data }) {
    if (!data) return createElement('div', null, 'Student not found.');

    return createElement('div', { className: 'container glass-panel animate-fade-in' },
        createElement('h2', { className: 'text-danger' }, 'Delete Student'),
        createElement('p', null, `Are you sure you want to delete ${data.firstName} ${data.lastName}?`),
        createElement('form', { method: 'POST', action: `/students/delete/${data.id}` },
            createElement('button', { type: 'submit', className: 'btn btn-danger' }, 'Delete'),
            ' ',
            createElement('a', { href: '/students', className: 'btn btn-secondary' }, 'Cancel')
        )
    );
}

// Hydration
document.querySelectorAll('[data-react-component="StudentDelete"]').forEach((el) => {
    const props = JSON.parse(el.getAttribute('data-react-props') || '{}');
    window.ReactDOM.createRoot(el).render(createElement(StudentDelete, props));
});
