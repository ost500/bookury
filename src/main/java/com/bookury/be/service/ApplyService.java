package com.bookury.be.service;

import com.bookury.be.api.dto.ApplyRequestDto;
import com.bookury.be.domain.Apply.Apply;
import com.bookury.be.domain.Apply.ApplyRepository;
import com.bookury.be.domain.Lecture.Lecture;
import com.bookury.be.domain.Lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public Long apply(Long lectureId, ApplyRequestDto applyRequestDto) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의가 없습니다"));




        Apply applyEntity = Apply.builder()
                .lecture(lecture)
                .employee_number(applyRequestDto.getEmployee_number())
                .build();

        return applyRepository.save(applyEntity).getId();
    }

}
