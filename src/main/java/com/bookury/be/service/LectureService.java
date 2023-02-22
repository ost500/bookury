package com.bookury.be.service;

import com.bookury.be.api.dto.LectureGiveRequestDto;
import com.bookury.be.domain.Lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    @Transactional
    public Long giveLecture(LectureGiveRequestDto lectureGiveRequestDto) {
        return lectureRepository.save(lectureGiveRequestDto.toEntity()).getId();
    }

}
