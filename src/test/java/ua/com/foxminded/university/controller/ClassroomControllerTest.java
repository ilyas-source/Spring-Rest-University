package ua.com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassrooms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.service.ClassroomService;

@ExtendWith(MockitoExtension.class)
class ClassroomControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClassroomService classroomService;
    @InjectMocks
    private ClassroomController classroomController;

    @BeforeEach
    public void setMocks() {
	mockMvc = MockMvcBuilders.standaloneSetup(classroomController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllClassrooms() throws Exception {
	when(classroomService.findAll()).thenReturn(expectedClassrooms);

	mockMvc.perform(get("/classrooms"))

		.andExpect(view().name("classroomsView"))
		.andExpect(model().attribute("classrooms", expectedClassrooms));
    }
}
