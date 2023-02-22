package com.bookury.be.api.dto;

import com.bookury.be.domain.Apply.Apply;
import com.bookury.be.domain.Lecture.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplyRequestDto {

    private String employee_number;

    @Builder
    public ApplyRequestDto(String employee_number) {
        this.employee_number = employee_number;
    }
}
