package com.bookury.be.service;

import com.bookury.be.api.dto.LectureGiveRequestDto;
import com.bookury.be.api.dto.LecturePopularResponseDto;
import com.bookury.be.api.dto.LectureResponseDto;
import com.bookury.be.domain.Lecture.Lecture;
import com.bookury.be.domain.Lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    @Transactional
    public Long giveLecture(LectureGiveRequestDto lectureGiveRequestDto) {
        return lectureRepository.save(lectureGiveRequestDto.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public List<LectureResponseDto> lectures() {
        return lectureRepository.findAllByStarttimeGreaterThanAndStarttimeLessThan(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusWeeks(1))
                .stream()
                .map(LectureResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LectureResponseDto> allLectures() {
        return lectureRepository.findAll()
                .stream()
                .map(LectureResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<LecturePopularResponseDto> popularLectures() {
        return lectureRepository.findAllWithApplyCount(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusWeeks(1))
                .stream()
                .map(object -> {
                    Lecture lecture = (Lecture) object[0];
                    LectureResponseDto lectureResponseDto = LectureResponseDto.builder().entity(lecture).build();
                    Long applyCount = (Long) object[1];
                    return LecturePopularResponseDto.builder().entity(lectureResponseDto).applyCount(applyCount).build();
                })
                .collect(Collectors.toList());
    }
}
