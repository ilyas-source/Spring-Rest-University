package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.service.TeacherService;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

//    @Test
//    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllTeachers() throws Exception {
//        when(teacherService.findAll()).thenReturn(expectedTeachers);
//
//        mockMvc.perform(get("/teachers"))
//                .andExpect(view().name("teachersView"))
//                .andExpect(model().attribute("teachers", expectedTeachers));
//    }
}