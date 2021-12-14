package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.dto.LectureDto;
import ua.com.foxminded.university.api.mapper.LectureMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.ClassroomRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.GroupRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.LectureRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.SubjectRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TeacherRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToList;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;
import static ua.com.foxminded.university.api.TimeslotRestControllerTest.TestData.*;

@ExtendWith(MockitoExtension.class)
public class LectureRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
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
        mockMvc = MockMvcBuilders.standaloneSetup(lectureRestController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(lectureService.findAll()).thenReturn(expectedLectures);

        MvcResult mvcResult = mockMvc.perform(get("/api/lectures"))
                .andExpect(status().isOk())
                .andReturn();
        List<Lecture> actual = mapToList(mvcResult, Lecture.class);

        assertEquals(expectedLectures, actual);
    }

    @Test
    void givenId_onGetLecture_shouldReturnCorrectJson() throws Exception {
        when(lectureService.getById(lectureId)).thenReturn(expectedLecture1);

        MvcResult mvcResult = mockMvc.perform(get("/api/lectures/{id}", lectureId))
                .andExpect(status().isOk()).andReturn();

        var actual = mapToObject(mvcResult, Lecture.class);

        verify(lectureService).getById(lectureId);
        assertEquals(expectedLecture1, actual);
    }

    @Test
    void givenLectureDto_onSave_shouldCallServiceCreate() throws Exception {
        when(mapper.lectureDtoToLecture(lectureDto)).thenReturn(expectedLecture1);
        mockMvc.perform(post("/api/lectures")
                        .content(objectMapper.writeValueAsString(lectureDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/lectures/1"))
                .andReturn();
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
        when(teacherService.getById(lectureId)).thenReturn(expectedTeacher1);

        var request = post("/api/lectures/replacement")
                .param("teacher", "1")
                .param("start", "2000-01-01")
                .param("end", "2000-02-01");

        var result = mockMvc.perform(request).andExpect(status().isOk());

        verify(lectureService).replaceTeacher(expectedTeacher1, startDate, endDate);
    }

    interface TestData {
        int lectureId = 1;

        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2000, 2, 1);

        Set<Group> expectedGroups1 = new HashSet<>(Arrays.asList(expectedGroup1, expectedGroup2));
        Set<Group> expectedGroups2 = new HashSet<>(List.of(expectedGroup1));

        Lecture expectedLecture1 = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture2 = Lecture.builder().date(LocalDate.of(2020, 1, 2)).subject(expectedSubject2)
                .id(2).timeslot(expectedTimeslot2).groups(expectedGroups2)
                .teacher(expectedTeacher2).classroom(expectedClassroom2).build();

        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1, expectedLecture2));

        LectureDto lectureDto = LectureDto.builder().date(LocalDate.of(2020, 1, 1)).subject(subjectDto)
                .timeslot(timeslotDto).groups(groupDtos)
                .teacher(teacherDto).classroom(classroomDto).build();
    }
}