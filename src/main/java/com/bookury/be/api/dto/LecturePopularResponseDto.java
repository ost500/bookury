package com.bookury.be.api.dto;

import com.bookury.be.domain.Lecture.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LecturePopularResponseDto {

    private LectureResponseDto lectureResponseDto;
    private Long applyCount;

    @Builder
    public LecturePopularResponseDto(LectureResponseDto entity, Long applyCount) {
        this.lectureResponseDto = entity;
        this.applyCount = applyCount;
    }

    @Override
    public String toString() {
        return "LecturePopularResponseDto{" +
                "lectureResponseDto=" + lectureResponseDto +
                ", applyCount=" + applyCount +
                '}';
    }
}
