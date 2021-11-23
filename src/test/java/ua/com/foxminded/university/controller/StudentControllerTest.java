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
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.GroupControllerTest.TestData.*;
import static ua.com.foxminded.university.controller.StudentControllerTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.controller.StudentControllerTest.TestData.expectedStudents;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;
    @Mock
    private GroupService groupService;
    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(studentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnViewWithPageOfStudents() throws Exception {
        var pageable = PageRequest.of(2, 5);
        Page<Student> studentPage = new PageImpl<>(expectedStudents, pageable, expectedStudents.size());

        when(studentService.findAll(pageable)).thenReturn(studentPage);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", "2");
        requestParams.add("size", "5");

        mockMvc.perform(get("/students").params(requestParams))
                .andExpect(view().name("student/all"))
                .andExpect(model().attribute("studentPage", studentPage));

        verify(studentService).findAll(pageable);
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithStudent() throws Exception {
        when(studentService.getById(1)).thenReturn(expectedStudent1);
        when(groupService.findAll()).thenReturn(expectedGroups);

        mockMvc.perform(get("/students/{id}", 1))
                .andExpect(view().name("student/details"))
                .andExpect(model().attribute("student", expectedStudent1))
                .andExpect(model().attribute("groups", expectedGroups));

        verify(studentService).getById(1);
        verify(groupService).findAll();
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        when(studentService.getById(1)).thenThrow(new EntityNotFoundException("Can't find student by id 1"));

        mockMvc.perform(get("/students/{id}", 1))
                .andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "EntityNotFoundException"))
                .andExpect(model().attribute("message", "Can't find student by id 1"));
    }

    @Test
    void givenStudent_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(post("/students/update").flashAttr("student", expectedStudent1))
                .andExpect(status().is3xxRedirection());

        verify(studentService).update(expectedStudent1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyStudent() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(view().name("student/create"))
                .andExpect(model().attribute("student", new Student()));
    }

    @Test
    void givenStudent_onCreate_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/students/create").flashAttr("student", expectedStudent1))
                .andExpect(status().is3xxRedirection());

        verify(studentService).create(expectedStudent1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/students/delete/{id}", 1)).andExpect(status().is3xxRedirection());

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