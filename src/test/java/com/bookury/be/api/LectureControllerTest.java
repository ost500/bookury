package com.bookury.be.api;


import com.bookury.be.api.dto.*;
import com.bookury.be.domain.Apply.Apply;
import com.bookury.be.domain.Apply.ApplyRepository;
import com.bookury.be.domain.Lecture.Lecture;
import com.bookury.be.domain.Lecture.LectureRepository;
import com.bookury.be.service.ApplyService;
import com.bookury.be.service.LectureService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.App;
import com.github.javafaker.Faker;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private MockMvc mvc;

    @BeforeEach
    public void 목데이터_입력() {
        List<Lecture> lectures = lectureRepository.findAll();
        if (lectures.isEmpty()) {
            for (int i = 0; i <= 20; i++) {
                Faker faker = new Faker(new Locale("ko"));
                String speacker = faker.name().fullName();
                String place = faker.address().streetAddress();
                Integer capacity = faker.number().numberBetween(5, 200);
                LocalDateTime starttime = LocalDateTime.now().plusDays(i - 5);
                String content = faker.book().title();

                LectureGiveRequestDto lectureGiveRequestDto = LectureGiveRequestDto.builder()
                        .speaker(speacker)
                        .place(place)
                        .capacity(capacity)
                        .starttime(starttime)
                        .content(content)
                        .build();

                Long lectureId = lectureService.giveLecture(lectureGiveRequestDto);

                int randomApplyCount = faker.number().numberBetween(1, 4);

                for (int j = 0; j <= randomApplyCount; j++) {

                    String employee_number = String.valueOf(50000 + (j * 100));
                    ApplyRequestDto applyRequestDto = ApplyRequestDto.builder()
                            .employee_number(employee_number)
                            .build();
                    applyService.apply(lectureId, applyRequestDto);
                }
            }
        }

        System.out.println("Mock 데이터 입력");
    }


    @Test
    public void 강의_열기() throws Exception {
        // given
        Faker faker = new Faker(new Locale("ko"));
        String speacker = faker.name().fullName();
        String place = faker.address().streetAddress();
        Integer capacity = faker.number().numberBetween(5, 200);
        LocalDateTime starttime = LocalDateTime.now();
        String content = faker.book().title();


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
        assertThat(all.get(all.size() - 1).getSpeaker()).isEqualTo(speacker);
        assertThat(all.get(all.size() - 1).getContent()).isEqualTo(content);

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
    public void 강의_목록_전체() throws Exception {
        // given
        String url = "http://localhost:" + port + "/api/v1/lectures/all";

        //when
        MvcResult result = mvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();

        //then
        String content = result.getResponse().getContentAsString();

        List<LectureResponseDto> lectureResponseDtoList = lectureService.allLectures();
        String speaker = JsonPath.read(result.getResponse().getContentAsString(), "$.[0].speaker");
        assertThat(lectureResponseDtoList.get(0).getSpeaker()).isEqualTo(speaker);


    }

    @Test
    public void 강의_신청자_목록() throws Exception {
        // given
        Faker faker = new Faker();
        List<Lecture> lectures = lectureRepository.findAll();
        Lecture oneLecture = lectures.get(faker.number().numberBetween(0, lectures.size() - 1));

        Long lectureId = oneLecture.getId();

        String url = "http://localhost:" + port + "/api/v1/lectures/" + lectureId + "/applies";

        //when
        MvcResult result = mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        //then
        List<ApplyResponseDto> applyResponseDtos = applyService.appliesLecture(lectureId);

        String employeeNum = JsonPath.read(result.getResponse().getContentAsString(), "$.[0].employee_number");
        assertThat(applyResponseDtos.get(0).getEmployee_number()).isEqualTo(employeeNum);
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void 강의_신청() throws Exception {
        // given
        Faker faker = new Faker();
        List<Lecture> lectures = lectureRepository.findAll();
        Lecture oneLecture = lectures.get(faker.number().numberBetween(0, lectures.size() - 1));

        Long lectureId = oneLecture.getId();
        String employee_number = String.valueOf(faker.number().numberBetween(1000, 9999));

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
        List<Apply> allApplies = applyRepository.findAll();
        assertThat(allApplies.get(allApplies.size() - 1).getEmployee_number()).isEqualTo(employee_number);
    }


    @Test
    public void 강의_사번_조회() throws Exception {
        // given
        Apply apply = applyRepository.findTopByOrderByIdDesc();
        String employee_number = apply.getEmployee_number();

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
        Lecture lecture = lectureRepository.findTopByOrderByIdDesc();
        Apply apply = applyRepository.findByLecture(lecture).get(0);

        Long lectureId = lecture.getId();
        String employee_number = apply.getEmployee_number();

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
        Optional<Lecture> oneLecture = lectureRepository.findById(lectureId);
        if (oneLecture.isPresent()) {
            List<Apply> oneApply = applyRepository.findByLectureAndEmployee_number(oneLecture.get(), applyRequestDto.getEmployee_number());
            assertThat(result.getResponse().getContentAsString()).isEqualTo(oneApply.get(0).getId().toString());
        }

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void 실시간_인기_강연() throws Exception {
        // given
        String url = "http://localhost:" + port + "/api/v1/lectures/popular";

        //when
        MvcResult result = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        List<LecturePopularResponseDto> lectureResponseDtoList = lectureService.popularLectures();
        Integer applyCount = JsonPath.read(result.getResponse().getContentAsString(), "$.[0].applyCount");
        System.out.println(applyCount);
        assertThat(lectureResponseDtoList.get(0).getApplyCount()).isEqualTo(applyCount.longValue());

        for (LecturePopularResponseDto lecturePopularResponseDto : lectureResponseDtoList) {
            System.out.println(lecturePopularResponseDto.toString());
        }

    }
}
