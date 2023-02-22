package com.bookury.be.api.controller;

import com.bookury.be.api.dto.LectureGiveRequestDto;
import com.bookury.be.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/lectures")
    public String lectures() {
        return "hello";
    }

    @PostMapping(value = "/api/v1/lectures/give")
    public Long GiveLecture(@RequestBody LectureGiveRequestDto requestDto) {
        return lectureService.giveLecture(requestDto);
    }
}
