package com.bookury.be.api.dto;

import com.bookury.be.domain.Lecture.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LectureResponseDto {


    private String speaker;
    private String place;
    private Integer capacity;
    private LocalDateTime starttime;
    private String content;

    @Builder
    public LectureResponseDto(Lecture entity) {
        this.speaker = entity.getSpeaker();
        this.place = entity.getPlace();
        this.capacity = entity.getCapacity();
        this.starttime = entity.getStarttime();
        this.content = entity.getContent();
    }
}
