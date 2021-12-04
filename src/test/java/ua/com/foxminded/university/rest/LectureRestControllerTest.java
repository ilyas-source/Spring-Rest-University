package ua.com.foxminded.university.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.controller.ControllerExceptionHandler;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Timeslot;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.rest.ClassroomRestControllerTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.rest.ClassroomRestControllerTest.TestData.expectedClassroom2;
import static ua.com.foxminded.university.rest.GroupRestControllerTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.rest.GroupRestControllerTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.rest.LectureRestControllerTest.TestData.*;
import static ua.com.foxminded.university.rest.SubjectRestControllerTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.rest.SubjectRestControllerTest.TestData.expectedSubject2;
import static ua.com.foxminded.university.rest.TeacherRestControllerTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.rest.TeacherRestControllerTest.TestData.expectedTeacher2;

@DataJpaTest
public class LectureRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedLectureJson;
    String expectedLecturesJson;

    @Mock
    private LectureService lectureService;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private LectureRestController lectureRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(lectureRestController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        expectedLectureJson = objectMapper.writeValueAsString(expectedLecture1);
        expectedLecturesJson = objectMapper.writeValueAsString(expectedLectures);
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(lectureService.findAll()).thenReturn(expectedLectures);

        mockMvc.perform(get("/api/lectures"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedLecturesJson));
    }

    @Test
    void givenId_onGetLecture_shouldReturnCorrectJson() throws Exception {
        when(lectureService.getById(1)).thenReturn(expectedLecture1);

        mockMvc.perform(get("/api/lectures/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedLectureJson));

        verify(lectureService).getById(1);
    }

    @Test
    void givenLecture_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/lectures")
                        .content(expectedLectureJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(lectureService).create(expectedLecture1);
    }

    @Test
    void givenLecture_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(put("/api/lectures/{id}", 1)
                        .content(expectedLectureJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lectureService).update(expectedLecture1);
    }

    @Test
    void givenLecture_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/lectures/{id}", 1))
                .andExpect(status().isOk());

        verify(lectureService).delete(1);
    }

    @Test
    void givenTeacherIdAndDates_onReplaceTeacher_shouldCallServiceReplaceTeacher() throws Exception { // TODO
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);

        var request = post("/api/lectures/replacement")
                .param("teacher", "1")
                .param("start", "01.01.2000")
                .param("end", "01.02.2000");

        mockMvc.perform(request).andExpect(status().isOk());

        verify(lectureService).replaceTeacher(expectedTeacher1, startDate, endDate);
    }

    interface TestData {

        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2000, 2, 1);

        Timeslot expectedTimeslot1 = new Timeslot(1, LocalTime.of(9, 0), LocalTime.of(9, 45));
        Timeslot expectedTimeslot2 = new Timeslot(2, LocalTime.of(10, 0), LocalTime.of(10, 45));
        Timeslot expectedTimeslot3 = new Timeslot(3, LocalTime.of(11, 0), LocalTime.of(11, 45));

        Set<Group> expectedGroups1 = new HashSet<>(Arrays.asList(expectedGroup1, expectedGroup2));
        Set<Group> expectedGroups2 = new HashSet<>(List.of(expectedGroup1));

        Lecture lectureToCreate = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(0).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture1 = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture2 = Lecture.builder().date(LocalDate.of(2020, 1, 2)).subject(expectedSubject2)
                .id(2).timeslot(expectedTimeslot2).groups(expectedGroups2)
                .teacher(expectedTeacher2).classroom(expectedClassroom2).build();

        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1, expectedLecture2));
    }
}