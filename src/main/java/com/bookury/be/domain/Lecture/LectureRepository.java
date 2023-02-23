package com.bookury.be.domain.Lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("select l from Lecture l where l.starttime > ?1 and l.starttime < ?2")
    List<Lecture> findAllByStarttimeGreaterThanAndStarttimeLessThan(LocalDateTime now, LocalDateTime starttime);

    @Query("select l, count(a) as counta from Lecture l left join Apply a on l.id = a.lecture.id " +
            "where l.starttime > ?1 and l.starttime < ?2 " +
            "group by l.id order by counta desc")
    List<Object[]> findAllWithApplyCount(LocalDateTime now, LocalDateTime starttime);

    Lecture findTopByOrderByIdDesc();
}
