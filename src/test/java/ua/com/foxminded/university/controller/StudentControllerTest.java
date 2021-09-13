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
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudents;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(studentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnViewWithPageOfStudents() throws Exception {
        var pageable= PageRequest.of(2,5);
        Page<Student> studentPage =new PageImpl<>(expectedStudents, pageable, expectedStudents.size());

        when(studentService.findAll(pageable)).thenReturn(studentPage);

        mockMvc.perform(get("/students?page=2&size=5"))
                .andExpect(view().name("studentsView"))
                .andExpect(model().attribute("studentPage", studentPage));
    }
}