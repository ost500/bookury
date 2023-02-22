package com.bookury.be.api;


import com.bookury.be.api.dto.LectureGiveRequestDto;
import com.bookury.be.domain.Lecture.Lecture;
import com.bookury.be.domain.Lecture.LectureRepository;
import com.bookury.be.service.LectureService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    LectureRepository lectureRepository;

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
}
