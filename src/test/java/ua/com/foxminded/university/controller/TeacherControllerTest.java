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
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeachers;

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
                .build();
    }

   @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnViewWithPageOfTeachers() throws Exception {
        var pageable= PageRequest.of(2,5);
        Page<Teacher> teacherPage =new PageImpl<>(expectedTeachers, pageable, expectedTeachers.size());

        when(teacherService.findAll(pageable)).thenReturn(teacherPage);

        mockMvc.perform(get("/teachers?page=2&size=5"))
                .andExpect(view().name("teachersView"))
                .andExpect(model().attribute("teacherPage", teacherPage));
    }
}