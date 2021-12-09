package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.GroupRestControllerTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.api.GroupRestControllerTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.api.StudentRestControllerTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.api.StudentRestControllerTest.TestData.expectedStudents;

@DataJpaTest
public class StudentRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();

    String expectedStudentJson;
    String expectedStudentsJson;

    @Mock
    private StudentService studentService;
    @InjectMocks
    private StudentRestController studentRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(studentRestController)
      //          .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        expectedStudentJson = mapper.writeValueAsString(expectedStudent1);
        expectedStudentsJson = mapper.writeValueAsString(expectedStudents);
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(studentService.findAll()).thenReturn(expectedStudents);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedStudentsJson));
    }

    @Test
    void givenId_onGetStudent_shouldReturnCorrectJson() throws Exception {
        when(studentService.getById(1)).thenReturn(expectedStudent1);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedStudentJson));

        verify(studentService).getById(1);
    }

    @Test
    void givenStudent_onSave_shouldCallServiceCreate() throws Exception {

        System.out.println(expectedStudentJson);

        mockMvc.perform(post("/api/students")
                        .content(expectedStudentJson)
                        .contentType(MediaType.APPLICATION_JSON))
         //       .andExpect(status().isCreated());
                 .andDo(print());
        verify(studentService).create(expectedStudent1);
    }

    @Test
    void givenStudent_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(put("/api/students/{id}", 1)
                        .content(expectedStudentJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(studentService).update(expectedStudent1);
    }

    @Test
    void givenStudent_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 1))
                .andExpect(status().isOk());

        verify(studentService).delete(1);
    }

    interface TestData {
        Address expectedAddress3 = Address.builder().country("Russia").id(3).postalCode("450080").region("Permskiy kray")
                .city("Perm").streetAddress("Lenina 5").build();
        Address expectedAddress4 = Address.builder().country("USA").id(4).postalCode("90210").region("California")
                .city("LA").streetAddress("Grove St. 15").build();
        Address expectedAddress5 = Address.builder().country("France").id(5).postalCode("21012").region("Central")
                .city("Paris").streetAddress("Rue 15").build();
        Address expectedAddress6 = Address.builder().country("China").id(6).postalCode("20121").region("Guangdung")
                .city("Beijin").streetAddress("Main St. 125").build();

        Student studentToCreate = Student.builder().firstName("Ivan").lastName("Petrov")
                .id(0).gender(Gender.MALE).birthDate(LocalDate.of(1980, 11, 1))
                .email("qwe@rty.com").phone("123123123").address(expectedAddress3)
                .group(expectedGroup1).build();

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
    }
}
