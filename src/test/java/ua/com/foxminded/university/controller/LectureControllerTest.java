package ua.com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.service.LectureService;

@ExtendWith(MockitoExtension.class)
class LectureControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LectureService lectureService;
    @InjectMocks
    private LectureController lectureController;

    @BeforeEach
    public void setMocks() {
	mockMvc = MockMvcBuilders.standaloneSetup(lectureController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllLectures() throws Exception {
	when(lectureService.findAll()).thenReturn(expectedLectures);

	mockMvc.perform(get("/lectures"))
		.andExpect(view().name("lecturesView"))
		.andExpect(model().attribute("lectures", expectedLectures));
    }
}
