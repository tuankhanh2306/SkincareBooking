package edu.uth.skincarebookingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/login", "/"})
    public String loginPage() {
        // Trả về đường dẫn file HTML trong static/pages
        return "pages/login.html";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "pages/register.html";
    }
}
