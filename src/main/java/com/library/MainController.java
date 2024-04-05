package com.library;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/secondindex")
    public String secondIndex(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        return "secondindex";
    }
}
