package edu.uth.skincarebookingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/login")
    public String login() {
        return "pages/login.html"; // trỏ tới file trong static/pages
    }
}

