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
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.service.TeacherService;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.rest.SubjectRestControllerTest.TestData.*;
import static ua.com.foxminded.university.rest.TeacherRestControllerTest.TestData.*;
import static ua.com.foxminded.university.rest.VacationRestControllerTest.TestData.expectedVacations1;
import static ua.com.foxminded.university.rest.VacationRestControllerTest.TestData.expectedVacations2;

@DataJpaTest
public class TeacherRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedTeacherJson;
    String expectedTeachersJson;

    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private TeacherRestController teacherRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherRestController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        expectedTeacherJson = objectMapper.writeValueAsString(expectedTeacher1);
        expectedTeachersJson = objectMapper.writeValueAsString(expectedTeachers);
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(teacherService.findAll()).thenReturn(expectedTeachers);

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTeachersJson));
    }

    @Test
    void givenId_onGetTeacher_shouldReturnCorrectJson() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);

        mockMvc.perform(get("/api/teachers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTeacherJson));

        verify(teacherService).getById(1);
    }

    @Test
    void givenTeacher_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/teachers")
                        .content(expectedTeacherJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(teacherService).create(expectedTeacher1);
    }

    @Test
    void givenTeacher_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(put("/api/teachers/{id}", 1)
                        .content(expectedTeacherJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(teacherService).update(expectedTeacher1);
    }

    @Test
    void givenTeacher_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", 1))
                .andExpect(status().isOk());

        verify(teacherService).delete(1);
    }

    interface TestData {
        Set<Subject> expectedSubjects1 = new HashSet<>(Arrays.asList(expectedSubject1, expectedSubject2));
        Set<Subject> expectedSubjects2 = new HashSet<>(Arrays.asList(expectedSubject3, expectedSubject4));

        Address expectedAddress1 = Address.builder().country("UK").id(1).postalCode("12345").region("City-Of-Edinburgh")
                .city("Edinburgh").streetAddress("Panmure House").build();
        Address expectedAddress2 = Address.builder().country("Poland").id(2).postalCode("54321").region(
                        "Central region")
                .city("Warsaw").streetAddress("Urszuli Ledochowskiej 3").build();

        Teacher teacherToCreate = Teacher.builder().firstName("Adam").lastName("Smith").id(0)
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(expectedSubjects1)
                .email("adam@smith.com").phoneNumber("+223322").address(expectedAddress1)
                .vacations(expectedVacations1).build();
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
