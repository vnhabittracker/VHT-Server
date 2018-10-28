package com.example.habitweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("hello");
    }
}
