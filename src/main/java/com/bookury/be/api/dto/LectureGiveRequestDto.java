package com.bookury.be.api.dto;

import com.bookury.be.domain.Lecture.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LectureGiveRequestDto {

    private String speaker;
    private String place;
    private Integer capacity;
    private LocalDateTime starttime;
    private String content;

    @Builder
    public LectureGiveRequestDto(String speaker, String place, Integer capacity, LocalDateTime starttime, String content) {
        this.speaker = speaker;
        this.place = place;
        this.capacity = capacity;
        this.starttime = starttime;
        this.content = content;
    }

    public Lecture toEntity() {
        return Lecture.builder()
                .speaker(speaker)
                .place(place)
                .capacity(capacity)
                .starttime(starttime)
                .content(content)
                .build();
    }
}
