package com.bookury.be.repository;


import com.bookury.be.api.dto.LectureResponseDto;
import com.bookury.be.domain.Lecture.Lecture;
import com.bookury.be.domain.Lecture.LectureRepository;
import org.junit.After;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LectureRepositoryTest {

    @Autowired
    LectureRepository lectureRepository;

    @Test
    public void 강의저장_불러오기() {
        // given
        String speacker = "오상택";
        String place = "롯데홀";
        Integer capacity = 10;
        LocalDateTime starttime = LocalDateTime.now();
        String content = "안녕하세요 오상택입니다";

        lectureRepository.save(Lecture.builder()
                .speaker(speacker)
                .place(place)
                .capacity(capacity)
                .starttime(starttime)
                .content(content)
                .build());

        // when
        List<Lecture> lectureList = lectureRepository.findAll();

        // then
        Lecture lectures = lectureList.get(0);
        assertThat(lectures.getSpeaker()).isEqualTo(speacker);
    }


    @Test
    public void 강의목록() {
        // given

        // when
        List<Lecture> lectureList = lectureRepository.findAllByStarttimeGreaterThanAndStarttimeLessThan(LocalDateTime.now(), LocalDateTime.now().plusWeeks(1));

        // then
        lectureList.forEach((lecture -> System.out.println(lecture.toString())));
    }
}
