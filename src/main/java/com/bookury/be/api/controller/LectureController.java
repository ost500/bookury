package com.bookury.be.api.controller;

import com.bookury.be.api.dto.LectureGiveRequestDto;
import com.bookury.be.api.dto.LectureResponseDto;
import com.bookury.be.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/api/v1/lectures")
    public List<LectureResponseDto> lectures() {
        return lectureService.lectures();
    }

    @PostMapping(value = "/api/v1/lectures/give")
    public Long GiveLecture(@RequestBody LectureGiveRequestDto requestDto) {
        return lectureService.giveLecture(requestDto);
    }
}
