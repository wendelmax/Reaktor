const { createElement } = window.React;
import { useReaktorContext } from '/reaktor/reaktor.js';

export function StudentList({ data }) {
    const context = useReaktorContext();
    const students = data || [];

    return createElement('div', { className: 'container animate-fade-in' },
        context.message && createElement('div', { className: 'alert alert-success' }, context.message),
        createElement('div', { style: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' } },
            createElement('h2', null, 'Students'),
            createElement('a', { href: '/students/create', className: 'btn btn-primary' }, 'Create New')
        ),
        createElement('table', { className: 'table glass-panel' },
            createElement('thead', null,
                createElement('tr', null,
                    createElement('th', null, 'First Name'),
                    createElement('th', null, 'Last Name'),
                    createElement('th', null, 'Enrollment Date'),
                    createElement('th', null, 'Actions')
                )
            ),
            createElement('tbody', null,
                students.map(s => createElement('tr', { key: s.id },
                    createElement('td', null, s.firstName),
                    createElement('td', null, s.lastName),
                    createElement('td', null, s.enrollmentDate),
                    createElement('td', null,
                        createElement('a', { href: `/students/edit/${s.id}` }, 'Edit'),
                        ' | ',
                        createElement('a', { href: `/students/${s.id}` }, 'Details'),
                        ' | ',
                        createElement('a', { href: `/students/delete/${s.id}` }, 'Delete')
                    )
                ))
            )
        )
    );
}

// Hydration
document.querySelectorAll('[data-react-component="StudentList"]').forEach((el) => {
    const props = JSON.parse(el.getAttribute('data-react-props') || '{}');
    window.ReactDOM.createRoot(el).render(createElement(StudentList, props));
});
