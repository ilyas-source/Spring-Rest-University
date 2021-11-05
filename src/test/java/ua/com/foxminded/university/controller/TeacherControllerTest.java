package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.StudentControllerTest.TestData.expectedAddress1;
import static ua.com.foxminded.university.controller.StudentControllerTest.TestData.expectedAddress2;
import static ua.com.foxminded.university.controller.SubjectControllerTest.TestData.*;
import static ua.com.foxminded.university.controller.TeacherControllerTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.controller.TeacherControllerTest.TestData.expectedTeachers;
import static ua.com.foxminded.university.controller.VacationControllerTest.TestData.expectedVacations1;
import static ua.com.foxminded.university.controller.VacationControllerTest.TestData.expectedVacations2;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(teacherController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnViewWithPageOfTeachers() throws Exception {
        var pageable = PageRequest.of(2, 5);
        Page<Teacher> teacherPage = new PageImpl<>(expectedTeachers, pageable, expectedTeachers.size());

        when(teacherService.findAll(pageable)).thenReturn(teacherPage);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", "2");
        requestParams.add("size", "5");

        mockMvc.perform(get("/teachers").params(requestParams))
                .andExpect(view().name("teacher/all"))
                .andExpect(model().attribute("teacherPage", teacherPage));

        verify(teacherService).findAll(pageable);
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithTeacher() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);

        mockMvc.perform(get("/teachers/{id}", 1))
                .andExpect(view().name("teacher/details"))
                .andExpect(model().attribute("teacher", expectedTeacher1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        when(teacherService.getById(1)).thenThrow(new EntityNotFoundException("Can't find teacher by id 1"));

        mockMvc.perform(get("/teachers/{id}", 1))
                .andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "EntityNotFoundException"))
                .andExpect(model().attribute("message", "Can't find teacher by id 1"));

        verify(teacherService).getById(1);
    }

    @Test
    void givenTeacher_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(post("/teachers/update").flashAttr("teacher", expectedTeacher1))
                .andExpect(status().is3xxRedirection());

        verify(teacherService).update(expectedTeacher1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyTeacher() throws Exception {
        mockMvc.perform(get("/teachers/new"))
                .andExpect(view().name("teacher/create"))
                .andExpect(model().attribute("teacher", new Teacher()));
    }

    @Test
    void givenTeacher_onCreate_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/teachers/create").flashAttr("teacher", expectedTeacher1))
                .andExpect(status().is3xxRedirection());

        verify(teacherService).create(expectedTeacher1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/teachers/delete/{id}", 1)).andExpect(status().is3xxRedirection());

        verify(teacherService).delete(1);
    }

    interface TestData {
        List<Subject> expectedSubjects1 = new ArrayList<>(Arrays.asList(expectedSubject1, expectedSubject2));
        List<Subject> expectedSubjects2 = new ArrayList<>(Arrays.asList(expectedSubject3, expectedSubject4));
        Teacher expectedTeacher1 = Teacher.builder().firstName("Adam").lastName("Smith").id(1)
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(expectedSubjects1)
                .email("adam@smith.com").phoneNumber("+223322").address(expectedAddress1)
                .vacations(expectedVacations1).build();
        Teacher expectedTeacher2 = Teacher.builder().firstName("Marie").lastName("Curie").id(2)
                .gender(Gender.FEMALE).degree(Degree.MASTER).subjects(expectedSubjects2)
                .email("marie@curie.com").phoneNumber("+322223").address(expectedAddress2)
                .vacations(expectedVacations2).build();
        List<Teacher> expectedTeachers = new ArrayList<>(Arrays.asList(expectedTeacher1, expectedTeacher2));
    }
}