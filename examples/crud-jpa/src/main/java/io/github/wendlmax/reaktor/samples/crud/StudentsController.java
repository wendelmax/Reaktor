package io.github.wendlmax.reaktor.samples.crud;

import io.github.wendlmax.reaktor.web.ReaktorResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private final StudentRepository repository;

    public StudentsController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ReaktorResponse<?> index() {
        return ReaktorResponse.ok(repository.findAll())
                .withView("students/index");
    }

    @GetMapping("/{id}")
    public ReaktorResponse<?> details(@PathVariable Long id) {
        return ReaktorResponse.ok(repository.findById(id))
                .withView("students/details");
    }

    @GetMapping("/create")
    public ReaktorResponse<?> create() {
        return ReaktorResponse.ok(new Student())
                .withView("students/create");
    }

    @PostMapping("/create")
    public ReaktorResponse<?> createPost(@Valid @ModelAttribute Student student, BindingResult result) {
        if (result.hasErrors()) {
            return ReaktorResponse.ok(student)
                    .withView("students/create")
                    .withErrors(result);
        }
        repository.save(student);
        return ReaktorResponse.redirect("/students")
                .withContext("message", "Student created successfully!");
    }

    @GetMapping("/edit/{id}")
    public ReaktorResponse<?> edit(@PathVariable Long id) {
        return ReaktorResponse.ok(repository.findById(id))
                .withView("students/edit");
    }

    @PostMapping("/edit/{id}")
    public ReaktorResponse<?> editPost(@PathVariable Long id, @Valid @ModelAttribute Student student,
            BindingResult result) {
        if (result.hasErrors()) {
            return ReaktorResponse.ok(student)
                    .withView("students/edit")
                    .withErrors(result);
        }

        return repository.findById(id).map(existing -> {
            existing.setFirstName(student.getFirstName());
            existing.setLastName(student.getLastName());
            existing.setEnrollmentDate(student.getEnrollmentDate());
            repository.save(existing);
            return ReaktorResponse.redirect("/students")
                    .withContext("message", "Student updated successfully!");
        }).orElseGet(ReaktorResponse::notFound);
    }

    @GetMapping("/delete/{id}")
    public ReaktorResponse<?> delete(@PathVariable Long id) {
        return ReaktorResponse.ok(repository.findById(id))
                .withView("students/delete");
    }

    @PostMapping("/delete/{id}")
    public ReaktorResponse<?> deletePost(@PathVariable Long id) {
        repository.deleteById(id);
        return ReaktorResponse.redirect("/students")
                .withContext("message", "Student deleted successfully!");
    }
}
