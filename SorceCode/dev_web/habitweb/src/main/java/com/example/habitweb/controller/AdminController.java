package com.example.habitweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    ResponseEntity<String> a(){
        return ResponseEntity.ok("a");
    }
}
