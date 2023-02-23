package com.bookury.be.domain.Apply;

import com.bookury.be.domain.Lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    @Query("select a from Apply a where a.lecture = ?1 and a.employee_number = ?2")
    List<Apply> findByLectureAndEmployee_number(Lecture lecture, String employee_number);

    @Query("select a from Apply a where a.lecture = ?1 and a.employee_number = ?2 and a.is_valid = true")
    List<Apply> findByLectureAndEmployee_numberAndIs_validTrue(Lecture lecture, String employee_number);

    @Query("select a from Apply a where a.employee_number = ?1")
    List<Apply> findByEmployee_number(String employee_number);

    @Query("select a from Apply a where a.lecture = ?1")
    List<Apply> findByLecture(Lecture lecture);
}