package com.bookury.be.domain.Lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findAllByStarttimeGreaterThanAndStarttimeLessThan(LocalDateTime now, LocalDateTime starttime);
}
