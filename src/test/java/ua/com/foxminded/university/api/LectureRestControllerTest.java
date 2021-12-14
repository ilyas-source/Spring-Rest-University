package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.dto.LectureDto;
import ua.com.foxminded.university.api.mapper.LectureMapper;
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
import static ua.com.foxminded.university.api.ClassroomRestControllerTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.api.ClassroomRestControllerTest.TestData.expectedClassroom2;
import static ua.com.foxminded.university.api.GroupRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.LectureRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.SubjectRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TeacherRestControllerTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.api.TeacherRestControllerTest.TestData.expectedTeacher2;
import static ua.com.foxminded.university.api.TimeslotRestControllerTest.TestData.*;

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
    @Mock
    private LectureMapper mapper;
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

        mockMvc.perform(get("/api/lectures/{id}", lectureId))
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
    void givenLectureDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.lectureDtoToLecture(lectureDto)).thenReturn(expectedLecture1);
        MvcResult mvcResult = mockMvc.perform(put("/api/lectures/{id}", lectureId)
                        .content(objectMapper.writeValueAsString(lectureDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Lecture.class);

        verify(lectureService).update(expectedLecture1);
        assertEquals(expectedLecture1, actual);
    }

    @Test
    void givenLecture_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/lectures/{id}", lectureId))
                .andExpect(status().isNoContent());

        verify(lectureService).delete(lectureId);
    }

    @Test
    void givenTeacherIdAndDates_onReplaceTeacher_shouldCallServiceReplaceTeacher() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);

        var request = post("/api/lectures/replacement")
                .param("teacher", "1")
                .param("start", "01.01.2000")
                .param("end", "01.02.2000");

        mockMvc.perform(request).andExpect(status().isOk());

        verify(lectureService).replaceTeacher(expectedTeacher1, startDate, endDate);
    }

    interface TestData {
        int lectureId = 1;

        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2000, 2, 1);

        Set<Group> expectedGroups1 = new HashSet<>(Arrays.asList(expectedGroup1, expectedGroup2));
        Set<Group> expectedGroups2 = new HashSet<>(List.of(expectedGroup1));

        Set<Group> expectedGroupsDto = new HashSet<>(Arrays.asList(expectedGroup1, expectedGroup2));

        Lecture expectedLecture1 = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture2 = Lecture.builder().date(LocalDate.of(2020, 1, 2)).subject(expectedSubject2)
                .id(2).timeslot(expectedTimeslot2).groups(expectedGroups2)
                .teacher(expectedTeacher2).classroom(expectedClassroom2).build();

        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1, expectedLecture2));

        LectureDto lectureDto = LectureDto.builder().date(LocalDate.of(2020, 1, 1)).subject(subjectDto)
                .timeslot(timeslotDto).groups(expectedGroupDtos)
                .teacher(teacherDto).classroom(expectedClassroom1).build();
    }
}