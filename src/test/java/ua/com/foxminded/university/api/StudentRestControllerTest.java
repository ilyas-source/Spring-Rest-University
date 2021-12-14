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
import ua.com.foxminded.university.api.dto.StudentDto;
import ua.com.foxminded.university.api.mapper.StudentMapper;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.GroupRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.StudentRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToList;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;

@ExtendWith(MockitoExtension.class)
public class StudentRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private StudentService studentService;
    @Mock
    private StudentMapper mapper;
    @InjectMocks
    private StudentRestController studentRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(studentRestController)
                .build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(studentService.findAll()).thenReturn(expectedStudents);

        MvcResult mvcResult = mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andReturn();
        List<Student> actual = mapToList(mvcResult, Student.class);

        assertEquals(expectedStudents, actual);
    }

    @Test
    void givenId_onGetStudent_shouldReturnCorrectJson() throws Exception {
        when(studentService.getById(studentId)).thenReturn(expectedStudent1);

        MvcResult mvcResult = mockMvc.perform(get("/api/students/{id}", studentId))
                .andExpect(status().isOk()).andReturn();

        var actual = mapToObject(mvcResult, Student.class);

        verify(studentService).getById(studentId);
        assertEquals(expectedStudent1, actual);
    }

    @Test
    void givenStudentDto_onSave_shouldCallServiceCreate() throws Exception {
        when(mapper.studentDtoToStudent(studentDto)).thenReturn(expectedStudent1);
        mockMvc.perform(post("/api/students")
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/students/1"))
                .andReturn();
        verify(studentService).create(expectedStudent1);
    }

    @Test
    void givenStudentDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.studentDtoToStudent(studentDto)).thenReturn(expectedStudent1);
        MvcResult mvcResult = mockMvc.perform(put("/api/students/{id}", studentId)
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Student.class);

        verify(studentService).update(expectedStudent1);
        assertEquals(expectedStudent1, actual);
    }

    @Test
    void givenStudent_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", studentId))
                .andExpect(status().isNoContent());

        verify(studentService).delete(studentId);
    }

    interface TestData {
        int studentId = 1;

        Address expectedAddress3 = Address.builder().country("Russia").id(3).postalCode("450080").region("Permskiy kray")
                .city("Perm").streetAddress("Lenina 5").build();
        Address expectedAddress4 = Address.builder().country("USA").id(4).postalCode("90210").region("California")
                .city("LA").streetAddress("Grove St. 15").build();
        Address expectedAddress5 = Address.builder().country("France").id(5).postalCode("21012").region("Central")
                .city("Paris").streetAddress("Rue 15").build();
        Address expectedAddress6 = Address.builder().country("China").id(6).postalCode("20121").region("Guangdung")
                .city("Beijin").streetAddress("Main St. 125").build();

        Student expectedStudent1 = Student.builder().firstName("Ivan").lastName("Petrov")
                .id(1).gender(Gender.MALE).birthDate(LocalDate.of(1980, 11, 1))
                .email("qwe@rty.com").phone("123123123").address(expectedAddress3)
                .group(expectedGroup1).build();
        Student expectedStudent2 = Student.builder().firstName("John").lastName("Doe")
                .id(2).gender(Gender.MALE).birthDate(LocalDate.of(1981, 11, 1))
                .email("qwe@qwe.com").phone("1231223").address(expectedAddress4)
                .group(expectedGroup2).build();
        Student expectedStudent3 = Student.builder().firstName("Janna").lastName("DArk")
                .id(3).gender(Gender.FEMALE).birthDate(LocalDate.of(1881, 11, 1))
                .email("qwe@no.fr").phone("1231223").address(expectedAddress5)
                .group(expectedGroup1).build();
        Student expectedStudent4 = Student.builder().firstName("Mao").lastName("Zedun")
                .id(4).gender(Gender.MALE).birthDate(LocalDate.of(1921, 9, 14))
                .email("qwe@no.cn").phone("1145223").address(expectedAddress6)
                .group(expectedGroup2).build();

        List<Student> expectedStudents = new ArrayList<>(
                Arrays.asList(expectedStudent1, expectedStudent2, expectedStudent3, expectedStudent4));

        AddressDto addressDto3=AddressDto.builder().country("Russia").postalCode("450080").region("Permskiy kray")
                .city("Perm").streetAddress("Lenina 5").build();

        StudentDto studentDto = StudentDto.builder().firstName("Ivan").lastName("Petrov")
                .gender(Gender.MALE).birthDate(LocalDate.of(1980, 11, 1))
                .email("qwe@rty.com").phone("123123123").address(addressDto3)
                .group(groupDto).build();
    }
}
