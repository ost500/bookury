package com.bookury.be.api.dto;

import com.bookury.be.domain.Apply.Apply;
import com.bookury.be.domain.Lecture.Lecture;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Getter
@NoArgsConstructor
public class ApplyResponseDto implements Serializable {

    private Lecture lecture;
    private String employee_number;
    private boolean is_valid;

    @Builder
    public ApplyResponseDto(Apply entity) {
        this.lecture = entity.getLecture();
        this.employee_number = entity.getEmployee_number();
        this.is_valid = entity.is_valid();
    }
}
