const { createElement } = window.React;

export function StudentDetails({ data }) {
    if (!data) return createElement('div', null, 'Student not found.');

    return createElement('div', { className: 'container glass-panel animate-fade-in' },
        createElement('h2', null, 'Student Details'),
        createElement('dl', { className: 'row' },
            createElement('dt', { className: 'col-sm-2' }, 'First Name'),
            createElement('dd', { className: 'col-sm-10' }, data.firstName),
            createElement('dt', { className: 'col-sm-2' }, 'Last Name'),
            createElement('dd', { className: 'col-sm-10' }, data.lastName),
            createElement('dt', { className: 'col-sm-2' }, 'Enrollment Date'),
            createElement('dd', { className: 'col-sm-10' }, data.enrollmentDate)
        ),
        createElement('div', null,
            createElement('a', { href: `/students/edit/${data.id}`, className: 'btn btn-primary' }, 'Edit'),
            ' ',
            createElement('a', { href: '/students', className: 'btn btn-secondary' }, 'Back to List')
        )
    );
}

// Hydration
document.querySelectorAll('[data-react-component="StudentDetails"]').forEach((el) => {
    const props = JSON.parse(el.getAttribute('data-react-props') || '{}');
    window.ReactDOM.createRoot(el).render(createElement(StudentDetails, props));
});
