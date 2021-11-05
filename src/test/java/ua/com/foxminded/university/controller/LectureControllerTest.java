package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Timeslot;
import ua.com.foxminded.university.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.ClassroomControllerTest.TestData.*;
import static ua.com.foxminded.university.controller.GroupControllerTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.controller.GroupControllerTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.controller.LectureControllerTest.TestData.*;
import static ua.com.foxminded.university.controller.StudentControllerTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.controller.StudentControllerTest.TestData.expectedStudents;
import static ua.com.foxminded.university.controller.SubjectControllerTest.TestData.*;
import static ua.com.foxminded.university.controller.TeacherControllerTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class LectureControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LectureService lectureService;
    @Mock
    private GroupService groupService;
    @Mock
    private TimeslotService timeslotService;
    @Mock
    private SubjectService subjectService;
    @Mock
    private ClassroomService classroomService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private StudentService studentService;
    @InjectMocks
    private LectureController lectureController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(lectureController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllLectures() throws Exception {
        when(lectureService.findAll()).thenReturn(expectedLectures);

        mockMvc.perform(get("/lectures"))
                .andExpect(view().name("lecture/all"))
                .andExpect(model().attribute("lectures", expectedLectures));

        verify(lectureService).findAll();
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithLecture() throws Exception {
        when(lectureService.getById(1)).thenReturn(expectedLecture1);
        when(groupService.findAll()).thenReturn(expectedGroups1);
        when(timeslotService.findAll()).thenReturn(expectedTimeslots);
        when(subjectService.findAll()).thenReturn(expectedSubjects);
        when(classroomService.findAll()).thenReturn(expectedClassrooms);
        when(teacherService.findAll()).thenReturn(expectedTeachers);

        mockMvc.perform(get("/lectures/{id}", 1))
                .andExpect(view().name("lecture/details"))
                .andExpect(model().attribute("lecture", expectedLecture1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        when(lectureService.getById(1)).thenThrow(new EntityNotFoundException("Can't find lecture by id 1"));

        mockMvc.perform(get("/lectures/{id}", 1))
                .andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "EntityNotFoundException"))
                .andExpect(model().attribute("message", "Can't find lecture by id 1"));
    }

    @Test
    void givenLecture_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);
        when(timeslotService.getById(1)).thenReturn(expectedTimeslot1);
        when(subjectService.getById(1)).thenReturn(expectedSubject1);
        when(classroomService.getById(1)).thenReturn(expectedClassroom1);
        when(groupService.getById(1)).thenReturn(expectedGroup1);
        when(groupService.getById(2)).thenReturn(expectedGroup2);

        mockMvc.perform(post("/lectures/update")
                        .flashAttr("lecture", expectedLecture1))
                .andExpect(status().is3xxRedirection());

        verify(lectureService).update(expectedLecture1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyLecture() throws Exception {
        mockMvc.perform(get("/lectures/new"))
                .andExpect(view().name("lecture/create"))
                .andExpect(model().attribute("lecture", new Lecture()));
    }

    @Test
    void givenLecture_onCreate_shouldCallServiceCreate() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);
        when(timeslotService.getById(1)).thenReturn(expectedTimeslot1);
        when(subjectService.getById(1)).thenReturn(expectedSubject1);
        when(classroomService.getById(1)).thenReturn(expectedClassroom1);
        when(groupService.getById(1)).thenReturn(expectedGroup1);
        when(groupService.getById(2)).thenReturn(expectedGroup2);

        mockMvc.perform(post("/lectures/create")
                        .flashAttr("lecture", expectedLecture1))
                .andExpect(status().is3xxRedirection());

        verify(lectureService).create(expectedLecture1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/lectures/delete/{id}", 1)).andExpect(status().is3xxRedirection());

        verify(lectureService).delete(1);
    }


    @Test
    void givenStudentEntityNameAndSubstring_onSearchPerson_shouldCallStudentServiceFindBySubstring() throws Exception {
        when(studentService.findBySubstring("test")).thenReturn(expectedStudents);
        mockMvc.perform(get("/lectures/search")
                        .param("entity", "student")
                        .param("substring", "test"))
                .andExpect(view().name("universityView"))
                .andExpect(model().attribute("students", expectedStudents));

        verify(studentService).findBySubstring("test");
    }

    @Test
    void givenTeacherEntityNameAndSubstring_onSearchPerson_shouldCallTeacherServiceFindBySubstring() throws Exception {
        when(teacherService.findBySubstring("test")).thenReturn(expectedTeachers);
        mockMvc.perform(get("/lectures/search")
                        .param("entity", "teacher")
                        .param("substring", "test"))
                .andExpect(view().name("universityView"))
                .andExpect(model().attribute("teachers", expectedTeachers));

        verify(teacherService).findBySubstring("test");
    }

    @Test
    void givenTeacherIdAndDates_onShowScheduleView_shouldOpenViewWithCorrectAttributes() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);

        var request = get("/lectures/schedule")
                .param("entity", "teacher")
                .param("date", "01.10.2021")
                .param("id", "1")
                .param("periodType", "day");


        mockMvc.perform(request)
                .andExpect(view().name("calendar"))
                .andExpect(model().attribute("entity", "teacher"))
                .andExpect(model().attribute("periodType", "day"))
                .andExpect(model().attribute("date", LocalDate.of(2021, 10, 01)))
                .andExpect(model().attribute("id", 1))
                .andExpect(model().attribute("personName", "teacher Adam Smith"));
    }

    @Test
    void givenStudentIdAndDates_onShowScheduleView_shouldOpenViewWithCorrectAttributes() throws Exception {
        when(studentService.getById(1)).thenReturn(expectedStudent1);

        var request = get("/lectures/schedule")
                .param("entity", "student")
                .param("date", "01.10.2021")
                .param("id", "1")
                .param("periodType", "day");


        mockMvc.perform(request)
                .andExpect(view().name("calendar"))
                .andExpect(model().attribute("entity", "student"))
                .andExpect(model().attribute("periodType", "day"))
                .andExpect(model().attribute("date", LocalDate.of(2021, 10, 01)))
                .andExpect(model().attribute("id", 1))
                .andExpect(model().attribute("personName", "student Ivan Petrov"));
    }

    @Test
    void givenTeacherAndDates_onRetrieveLecturesForCalendar_shouldReturnCorrectLecturesLise() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);
        when(lectureService.findByTeacherAndPeriod(expectedTeacher1, startDate, endDate)).thenReturn(expectedLectures);

        var request = get("/lectures/schedule/calendar")
                .param("id", "1")
                .param("entity", "teacher")
                .param("start", "2000-01-01T00:00%2B03:00")
                .param("end", "2000-02-01T00:00%2B03:00");

        var mvcResult = mockMvc.perform(request);

        var status = mvcResult.andExpect(status().is2xxSuccessful());
        assertEquals(true, status);
    }

    @Test
    void onShowReplacementView_shouldShowReplacementView() throws Exception {
        mockMvc.perform(get("/lectures/replacement"))
                .andExpect(view().name("lecture/replacement"));
    }

    @Test
    void givenTeacherIdAndDates_onReplaceTeacher_shouldCallServiceReplaceTeacher() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);

        var request = post("/lectures/replacement")
                .param("teacher", "1")
                .param("start", "01.01.2000")
                .param("end", "01.02.2000");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lectures"));

        verify(lectureService).replaceTeacher(expectedTeacher1, startDate, endDate);
    }

    public interface TestData {

        LocalDate startDate = LocalDate.of(2000, 01, 01);
        LocalDate endDate = LocalDate.of(2000, 02, 01);

        Timeslot expectedTimeslot1 = new Timeslot(1, LocalTime.of(9, 00), LocalTime.of(9, 45));
        Timeslot expectedTimeslot2 = new Timeslot(2, LocalTime.of(10, 00), LocalTime.of(10, 45));
        Timeslot expectedTimeslot3 = new Timeslot(3, LocalTime.of(11, 00), LocalTime.of(11, 45));
        List<Timeslot> expectedTimeslots = new ArrayList<>(
                Arrays.asList(expectedTimeslot1, expectedTimeslot2, expectedTimeslot3));

        List<Group> expectedGroups1 = new ArrayList<>(Arrays.asList(expectedGroup1, expectedGroup2));
        List<Group> expectedGroups2 = new ArrayList<>(Arrays.asList(expectedGroup1));

        Lecture expectedLecture1 = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture2 = Lecture.builder().date(LocalDate.of(2020, 1, 2)).subject(expectedSubject2)
                .id(2).timeslot(expectedTimeslot2).groups(expectedGroups2)
                .teacher(expectedTeacher2).classroom(expectedClassroom2).build();

        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1, expectedLecture2));
    }
}

