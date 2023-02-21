package com.bookury.be.domain.Lecture;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Length;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Lecture {

    @Id
    @GeneratedValue
    private Long id;

    private String speaker;

    private String place;

    private Integer capacity;

    private LocalDateTime starttime;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Lecture(String speaker, String place, Integer capacity, LocalDateTime starttime, String content) {
        this.speaker = speaker;
        this.place = place;
        this.capacity = capacity;
        this.starttime = starttime;
        this.content = content;
    }
}
