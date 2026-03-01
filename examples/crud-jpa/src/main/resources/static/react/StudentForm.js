const { createElement, useState } = window.React;
import { useReaktorErrors, useReaktorStatus } from '/reaktor/reaktor.js';

export function StudentForm({ data, isEdit }) {
    const serverErrors = useReaktorErrors();
    const { message } = useReaktorStatus();

    // Form data from props (initial state)
    const [student, setStudent] = useState(data || { firstName: '', lastName: '', enrollmentDate: '' });

    const getError = (field) => serverErrors.find(e => e.field === field)?.message;

    return createElement('div', { className: 'container glass-panel animate-fade-in' },
        createElement('h2', null, isEdit ? 'Edit Student' : 'Create Student'),
        message && createElement('div', { className: 'alert alert-info' }, message),
        createElement('form', { method: 'POST', action: isEdit ? `/students/edit/${student.id}` : '/students/create' },
            createElement('div', { className: 'form-group' },
                createElement('label', null, 'First Name'),
                createElement('input', {
                    name: 'firstName',
                    className: 'form-control',
                    value: student.firstName,
                    onChange: (e) => setStudent({ ...student, firstName: e.target.value })
                }),
                getError('firstName') && createElement('span', { className: 'text-danger' }, getError('firstName'))
            ),
            createElement('div', { className: 'form-group' },
                createElement('label', null, 'Last Name'),
                createElement('input', {
                    name: 'lastName',
                    className: 'form-control',
                    value: student.lastName,
                    onChange: (e) => setStudent({ ...student, lastName: e.target.value })
                }),
                getError('lastName') && createElement('span', { className: 'text-danger' }, getError('lastName'))
            ),
            createElement('div', { className: 'form-group' },
                createElement('label', null, 'Enrollment Date'),
                createElement('input', {
                    type: 'date',
                    name: 'enrollmentDate',
                    className: 'form-control',
                    value: student.enrollmentDate,
                    onChange: (e) => setStudent({ ...student, enrollmentDate: e.target.value })
                }),
                getError('enrollmentDate') && createElement('span', { className: 'text-danger' }, getError('enrollmentDate'))
            ),
            createElement('div', { style: { marginTop: '1rem' } },
                createElement('button', { type: 'submit', className: 'btn btn-primary' }, 'Save'),
                ' ',
                createElement('a', { href: '/students', className: 'btn btn-secondary' }, 'Back to List')
            )
        )
    );
}

// Hydration
document.querySelectorAll('[data-react-component="StudentForm"]').forEach((el) => {
    const props = JSON.parse(el.getAttribute('data-react-props') || '{}');
    window.ReactDOM.createRoot(el).render(createElement(StudentForm, props));
});
