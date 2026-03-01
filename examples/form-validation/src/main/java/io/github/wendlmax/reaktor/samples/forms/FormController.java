package io.github.wendlmax.reaktor.samples.forms;

import io.github.wendlmax.reaktor.web.ReaktorResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class FormController {

    @GetMapping("/")
    public ReaktorResponse<?> index() {
        return ReaktorResponse.ok().withView("forms/contact");
    }

    @PostMapping("/contact")
    public ReaktorResponse<?> submitContact(@Valid ContactDTO contact,
            org.springframework.validation.BindingResult result) {
        if (result.hasErrors()) {
            return ReaktorResponse.ok(contact)
                    .withView("forms/contact")
                    .withErrors(result);
        }

        // Simulating processing
        System.out.println("Processing contact: " + contact);

        return ReaktorResponse.redirect("/?success=true");
    }

    public record ContactDTO(
            @NotBlank(message = "Name is required") String name,

            @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,

            @NotBlank(message = "Message cannot be empty") @Size(min = 10, message = "Message must be at least 10 characters") String message) {
    }
}
