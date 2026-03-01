package io.github.wendlmax.reaktor.samples.basics;

import io.github.wendlmax.reaktor.web.ReaktorResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class BasicsController {

    @GetMapping("/")
    public ReaktorResponse<?> counter() {
        return ReaktorResponse.ok()
                .with("initialCount", 10)
                .withContext("siteTheme", "dark")
                .withContext("userRole", "admin");
    }

    @GetMapping("/demo-flash")
    public String demoFlash(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("flashMessage", "Success! This came from TempData (Flash).");
        return "redirect:/";
    }
}
