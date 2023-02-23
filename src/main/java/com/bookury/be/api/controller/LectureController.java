package com.bookury.be.api.controller;

import com.bookury.be.api.dto.ApplyRequestDto;
import com.bookury.be.api.dto.LectureGiveRequestDto;
import com.bookury.be.api.dto.LectureResponseDto;
import com.bookury.be.service.ApplyService;
import com.bookury.be.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;
    private final ApplyService applyService;

    @GetMapping("/api/v1/lectures")
    public List<LectureResponseDto> lectures() {
        return lectureService.lectures();
    }

    @PostMapping(value = "/api/v1/lectures/give")
    public Long GiveLecture(@RequestBody LectureGiveRequestDto requestDto) {
        return lectureService.giveLecture(requestDto);
    }

    @PostMapping(value = "/api/v1/lectures/{lectureId}/apply")
    public Long ApplyLecture(@PathVariable Long lectureId, @RequestBody ApplyRequestDto requestDto) {
        return applyService.apply(lectureId, requestDto);
    }

    @GetMapping("/api/v1/lectures/employeenumber")
    public List<LectureResponseDto> lecturesEmployeenumber(@RequestBody ApplyRequestDto requestDto) {
        return applyService.getApplyListByEmployeeNumber(requestDto);
    }
}
