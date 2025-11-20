package edu.uth.skincarebookingsystem.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRoleController {

    @GetMapping("/test/whoami")
    public Object whoAmI() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return new Object() {
            public final String name = auth.getName();
            public final Object authorities = auth.getAuthorities(); // Đây là mấu chốt
        };
    }
}