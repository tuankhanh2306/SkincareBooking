package edu.uth.skincarebookingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "pages/login"; // KHÔNG cần .html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "pages/register"; // KHÔNG cần .html
    }

}
