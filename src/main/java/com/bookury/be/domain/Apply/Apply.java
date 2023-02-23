package com.bookury.be.domain.Apply;

import com.bookury.be.domain.Lecture.Lecture;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
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

    @Column(columnDefinition = "boolean default true")
    @ColumnDefault("true")
    private boolean is_valid;

    @Builder
    public Apply(Long id, Lecture lecture, String employee_number) {
        this.id = id;
        this.lecture = lecture;
        this.employee_number = employee_number;
        this.is_valid = true;
    }

    @Override
    public String toString() {
        return "Apply{" +
                "id=" + id +
                ", lecture=" + lecture +
                ", employee_number='" + employee_number + '\'' +
                '}';
    }

    public void cancel() {
        this.is_valid = false;
    }
}