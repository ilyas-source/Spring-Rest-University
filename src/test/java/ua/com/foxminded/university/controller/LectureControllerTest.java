package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.service.LectureService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture1;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;

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

        verify(lectureService).findAll();
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithLecture() throws Exception {
        when(lectureService.findById(1)).thenReturn(Optional.of(expectedLecture1));

        mockMvc.perform(get("/lectures/1"))
                .andExpect(view().name("/details/lecture"))
                .andExpect(model().attribute("lecture", expectedLecture1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        String expected = "Can't find lecture by id 1";
        when(lectureService.findById(1)).thenReturn(Optional.empty());
        Throwable thrown = assertThrows(org.springframework.web.util.NestedServletException.class,
                                        () -> mockMvc.perform(get("/lectures/1")));
        Throwable cause = thrown.getCause();

        assertEquals(cause.getClass(), EntityNotFoundException.class);
        assertEquals(expected, cause.getMessage());
    }

    @Test
    void givenLecture_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(post("/lectures/update").flashAttr("lecture", expectedLecture1))
                .andExpect(status().is3xxRedirection());

        verify(lectureService).update(expectedLecture1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyLecture() throws Exception {
        mockMvc.perform(get("/lectures/new"))
                .andExpect(view().name("/create/lecture"))
                .andExpect(model().attribute("lecture", new Lecture()));
    }

    @Test
    void givenLecture_onCreate_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/lectures/create").flashAttr("lecture", expectedLecture1))
                .andExpect(status().is3xxRedirection());

        verify(lectureService).create(expectedLecture1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/lectures/delete/1")).andExpect(status().is3xxRedirection());

        verify(lectureService).delete(1);
    }
}
