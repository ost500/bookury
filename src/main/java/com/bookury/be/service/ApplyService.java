package com.bookury.be.service;

import com.bookury.be.api.dto.ApplyRequestDto;
import com.bookury.be.api.dto.LectureResponseDto;
import com.bookury.be.domain.Apply.Apply;
import com.bookury.be.domain.Apply.ApplyRepository;
import com.bookury.be.domain.Lecture.Lecture;
import com.bookury.be.domain.Lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public Long apply(Long lectureId, ApplyRequestDto applyRequestDto) {

        Lecture lecture = findLectureById(lectureId);

        List<Apply> applyAlreadyExists = findApplyByLecIdAndEmpNum(lecture, applyRequestDto);

        if (!applyAlreadyExists.isEmpty()) {
            throw new DuplicateKeyException("이미 신청한 강의 입니다");
        }

        Apply applyEntity = Apply.builder()
                .lecture(lecture)
                .employee_number(applyRequestDto.getEmployee_number())
                .build();

        return applyRepository.save(applyEntity).getId();
    }

    public List<LectureResponseDto> getApplyListByEmployeeNumber(ApplyRequestDto applyRequestDto) {
        List<Apply> applies = applyRepository.findByEmployee_number(applyRequestDto.getEmployee_number());

        return applies.stream()
                .map(apply -> LectureResponseDto.builder().entity(apply.getLecture()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public Long cancelApply(Long lectureId, ApplyRequestDto applyRequestDto) {
        Lecture lecture = findLectureById(lectureId);

        List<Apply> myAppllies = applyRepository.findByLectureAndEmployee_numberAndIs_validTrue(lecture, applyRequestDto.getEmployee_number());

        if (myAppllies.isEmpty()) {
            throw new DuplicateKeyException("기존 신청 내용이 없습니다");
        }

        Apply myApply = myAppllies.get(0);
        myApply.cancel();

        applyRepository.save(myApply);

        return myApply.getId();
    }


    private Lecture findLectureById(Long lectureId) {

        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의가 없습니다"));
    }

    private List<Apply> findApplyByLecIdAndEmpNum(Lecture lecture, ApplyRequestDto applyRequestDto) {
        return applyRepository.findByLectureAndEmployee_number(lecture, applyRequestDto.getEmployee_number());
    }
}
