package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

//    @Test
//    void givenCorrectDefaultGetRequest_onFindAll_shouldReturnHtmlPageWithStudents() throws Exception {
//        when(studentService.findAll(any())).thenReturn(expectedStudents);
//
//        mockMvc.perform(get("/students"))
//                .andExpect(view().name("studentsView"))
//                .andExpect(model().attribute("student", expectedStudents));
//    }
}