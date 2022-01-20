package com.devthink.devthink_server.controllers;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableJpaAuditing
@RequestMapping("/")
public class HelloController {

    @GetMapping
    public String sayHello() {
        return "Hello world!";
    }
}
