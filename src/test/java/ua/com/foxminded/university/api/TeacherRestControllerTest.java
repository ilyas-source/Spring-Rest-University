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
import ua.com.foxminded.university.api.dto.AddressDto;
import ua.com.foxminded.university.api.dto.TeacherDto;
import ua.com.foxminded.university.api.mapper.TeacherMapper;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.service.TeacherService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.SubjectRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TeacherRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToList;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;
import static ua.com.foxminded.university.api.VacationRestControllerTest.TestData.expectedVacations1;
import static ua.com.foxminded.university.api.VacationRestControllerTest.TestData.expectedVacations2;

@ExtendWith(MockitoExtension.class)
public class TeacherRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper mapper;
    @InjectMocks
    private TeacherRestController teacherRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherRestController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(teacherService.findAll()).thenReturn(expectedTeachers);

        MvcResult mvcResult = mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andReturn();
        List<Teacher> actual = mapToList(mvcResult, Teacher.class);

        assertEquals(expectedTeachers, actual);
    }

    @Test
    void givenId_onGetTeacher_shouldReturnCorrectJson() throws Exception {
        when(teacherService.getById(teacherId)).thenReturn(expectedTeacher1);

        MvcResult mvcResult = mockMvc.perform(get("/api/teachers/{id}", teacherId))
                .andExpect(status().isOk()).andReturn();

        var actual = mapToObject(mvcResult, Teacher.class);

        verify(teacherService).getById(teacherId);
        assertEquals(expectedTeacher1, actual);
    }

    @Test
    void givenTeacherDto_onSave_shouldCallServiceCreate() throws Exception {
        when(mapper.teacherDtoToTeacher(teacherDto)).thenReturn(expectedTeacher1);
        mockMvc.perform(post("/api/teachers")
                        .content(objectMapper.writeValueAsString(teacherDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/teachers/1"))
                .andReturn();
        verify(teacherService).create(expectedTeacher1);
    }

    @Test
    void givenTeacherDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.teacherDtoToTeacher(teacherDto)).thenReturn(expectedTeacher1);
        MvcResult mvcResult = mockMvc.perform(put("/api/teachers/{id}", teacherId)
                        .content(objectMapper.writeValueAsString(teacherDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Teacher.class);

        verify(teacherService).update(expectedTeacher1);
        assertEquals(expectedTeacher1, actual);
    }

    @Test
    void givenTeacher_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", teacherId))
                .andExpect(status().isNoContent());

        verify(teacherService).delete(teacherId);
    }

    interface TestData {
        int teacherId = 1;

        Set<Subject> expectedSubjects1 = new HashSet<>(Arrays.asList(expectedSubject1, expectedSubject2));
        Set<Subject> expectedSubjects2 = new HashSet<>(Arrays.asList(expectedSubject3, expectedSubject4));

        Address expectedAddress1 = Address.builder().country("UK").id(1).postalCode("12345").region("City-Of-Edinburgh")
                .city("Edinburgh").streetAddress("Panmure House").build();
        Address expectedAddress2 = Address.builder().country("Poland").id(2).postalCode("54321").region(
                        "Central region")
                .city("Warsaw").streetAddress("Urszuli Ledochowskiej 3").build();
        AddressDto addressDto = AddressDto.builder().country("UK").postalCode("12345").region("City-Of-Edinburgh")
                .city("Edinburgh").streetAddress("Panmure House").build();

        Teacher expectedTeacher1 = Teacher.builder().firstName("Adam").lastName("Smith").id(1)
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(expectedSubjects1)
                .email("adam@smith.com").phoneNumber("+223322").address(expectedAddress1)
                .vacations(expectedVacations1).build();
        Teacher expectedTeacher2 = Teacher.builder().firstName("Marie").lastName("Curie").id(2)
                .gender(Gender.FEMALE).degree(Degree.MASTER).subjects(expectedSubjects2)
                .email("marie@curie.com").phoneNumber("+322223").address(expectedAddress2)
                .vacations(expectedVacations2).build();
        List<Teacher> expectedTeachers = new ArrayList<>(Arrays.asList(expectedTeacher1, expectedTeacher2));

        TeacherDto teacherDto = TeacherDto.builder().firstName("Adam").lastName("Smith")
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(subjectDtos)
                .email("adam@smith.com").phoneNumber("+223322").address(addressDto).build();
    }
}
