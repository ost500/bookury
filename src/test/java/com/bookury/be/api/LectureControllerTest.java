package com.bookury.be.api;


import com.bookury.be.api.dto.ApplyRequestDto;
import com.bookury.be.api.dto.LectureGiveRequestDto;
import com.bookury.be.api.dto.LectureResponseDto;
import com.bookury.be.domain.Apply.Apply;
import com.bookury.be.domain.Apply.ApplyRepository;
import com.bookury.be.domain.Lecture.Lecture;
import com.bookury.be.domain.Lecture.LectureRepository;
import com.bookury.be.service.ApplyService;
import com.bookury.be.service.LectureService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LectureControllerTest {

    @Autowired
    LectureService lectureService;

    @Autowired
    ApplyService applyService;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    ApplyRepository applyRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;


    @Test
    public void 강의_열기() throws Exception {
        // given
        String speacker = "OST";
        String place = "LOTTE HALL";
        Integer capacity = 10;
        LocalDateTime starttime = LocalDateTime.now();
        String content = "Hello I am OST2";


        LectureGiveRequestDto lectureGiveRequestDto = LectureGiveRequestDto.builder()
                .speaker(speacker)
                .place(place)
                .capacity(capacity)
                .starttime(starttime)
                .content(content)
                .build();

        String url = "http://localhost:" + port + "/api/v1/lectures/give";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(lectureGiveRequestDto)))
                .andExpect(status().isOk());

        //then
        List<Lecture> all = lectureRepository.findAll();
        assertThat(all.get(0).getSpeaker()).isEqualTo(speacker);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }


    @Test
    public void 강의_목록() throws Exception {
        // given
        String url = "http://localhost:" + port + "/api/v1/lectures";

        //when
        MvcResult result = mvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();

        //then
        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    public void 강의_신청() throws Exception {
        // given
        Long lectureId = Long.valueOf(52);
        String employee_number = "123123";

        ApplyRequestDto applyRequestDto = ApplyRequestDto.builder()
                .employee_number(employee_number)
                .build();

        String url = "http://localhost:" + port + "/api/v1/lectures/" + lectureId + "/apply";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(applyRequestDto)))
                .andExpect(status().isOk());

        //then
        List<Apply> all = applyRepository.findAll();
    }


    @Test
    public void 강의_사번_조회() throws Exception {
        // given
        String employee_number = "1231234";

        ApplyRequestDto applyRequestDto = ApplyRequestDto.builder()
                .employee_number(employee_number)
                .build();

        String url = "http://localhost:" + port + "/api/v1/lectures/employeenumber";

        //when
        MvcResult result = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(applyRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        //then

        List<LectureResponseDto> lectureResponseDtoList = applyService.getApplyListByEmployeeNumber(applyRequestDto);
        String speaker = JsonPath.read(result.getResponse().getContentAsString(), "$.[0].speaker");
        assertThat(lectureResponseDtoList.get(0).getSpeaker()).isEqualTo(speaker);

        System.out.println(speaker);
    }


    @Test
    public void 강의_수강_취소() throws Exception {
        // given
        Long lectureId = Long.valueOf(52);
        String employee_number = "1231234";

        ApplyRequestDto applyRequestDto = ApplyRequestDto.builder()
                .employee_number(employee_number)
                .build();

        String url = "http://localhost:" + port + "/api/v1/lectures/" + lectureId + "/employeenumber/cancel";

        //when
        MvcResult result = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(applyRequestDto)))
                .andExpect(status().isOk()).andReturn();

        //then
        Optional<Lecture> lecture = lectureRepository.findById(lectureId);
        if (lecture.isPresent()) {
            List<Apply> apply = applyRepository.findByLectureAndEmployee_number(lecture.get(), applyRequestDto.getEmployee_number());
            assertThat(result.getResponse().getContentAsString()).isEqualTo(apply.get(0).getId().toString());
        }

        System.out.println(result.getResponse().getContentAsString());
    }

}
