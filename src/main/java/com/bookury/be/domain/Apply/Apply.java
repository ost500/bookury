package com.bookury.be.domain.Apply;

import com.bookury.be.domain.Lecture.Lecture;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "apply")
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Apply_SEQ")
    @SequenceGenerator(name = "Apply_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private String employee_number;

    @Builder
    public Apply(Long id, Lecture lecture, String employee_number) {
        this.id = id;
        this.lecture = lecture;
        this.employee_number = employee_number;
    }

    @Override
    public String toString() {
        return "Apply{" +
                "id=" + id +
                ", lecture=" + lecture +
                ", employee_number='" + employee_number + '\'' +
                '}';
    }
}