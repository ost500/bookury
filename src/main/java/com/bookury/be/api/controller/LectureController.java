package com.bookury.be.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LectureController {

    @GetMapping("/lectures")
    public String lectures() {
        return "hello";
    }
}
