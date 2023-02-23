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

    // BackOffice
    // 강연 목록
    @GetMapping("/api/v1/lectures/all")
    public List<LectureResponseDto> allLectures() {
        return lectureService.allLectures();
    }

    @PostMapping(value = "/api/v1/lectures/give")
    public Long GiveLecture(@RequestBody LectureGiveRequestDto requestDto) {
        return lectureService.giveLecture(requestDto);
    }


    // Front
    // 강연 목록
    @GetMapping("/api/v1/lectures")
    public List<LectureResponseDto> lectures() {
        return lectureService.lectures();
    }

    // 강연 신청
    @PostMapping(value = "/api/v1/lectures/{lectureId}/apply")
    public Long ApplyLecture(@PathVariable Long lectureId, @RequestBody ApplyRequestDto requestDto) {
        return applyService.apply(lectureId, requestDto);
    }

    // 신청내역 조회
    @GetMapping("/api/v1/lectures/employeenumber")
    public List<LectureResponseDto> lecturesEmployeenumber(@RequestBody ApplyRequestDto requestDto) {
        return applyService.getApplyListByEmployeeNumber(requestDto);
    }

    // 신청한 강연 취소
    @PostMapping("/api/v1/lectures/{lectureId}/employeenumber/cancel")
    public Long cancelLecture(@PathVariable Long lectureId, @RequestBody ApplyRequestDto requestDto) {
        return applyService.cancelApply(lectureId, requestDto);
    }
}
