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
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.SubjectService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.SubjectControllerTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.controller.SubjectControllerTest.TestData.expectedSubjects;


@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllSubjects() throws Exception {
        when(subjectService.findAll()).thenReturn(expectedSubjects);

        mockMvc.perform(get("/subjects"))
                .andExpect(view().name("subject/all"))
                .andExpect(model().attribute("subjects", expectedSubjects));

        verify(subjectService).findAll();
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithSubject() throws Exception {
        when(subjectService.getById(1)).thenReturn(expectedSubject1);

        mockMvc.perform(get("/subjects/1"))
                .andExpect(view().name("subject/details"))
                .andExpect(model().attribute("subject", expectedSubject1));

        verify(subjectService).getById(1);
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        when(subjectService.getById(1)).thenThrow(new EntityNotFoundException("Can't find subject by id 1"));

        mockMvc.perform(get("/subjects/1"))
                .andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "EntityNotFoundException"))
                .andExpect(model().attribute("message", "Can't find subject by id 1"));
    }

    @Test
    void givenSubject_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(post("/subjects/update").flashAttr("subject", expectedSubject1))
                .andExpect(status().is3xxRedirection());

        verify(subjectService).update(expectedSubject1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptySubject() throws Exception {
        mockMvc.perform(get("/subjects/new"))
                .andExpect(view().name("subject/create"))
                .andExpect(model().attribute("subject", new Subject()));
    }

    @Test
    void givenSubject_onCreate_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/subjects/create").flashAttr("subject", expectedSubject1))
                .andExpect(status().is3xxRedirection());

        verify(subjectService).create(expectedSubject1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/subjects/delete/1")).andExpect(status().is3xxRedirection());

        verify(subjectService).delete(1);
    }

    public interface TestData {

        Subject expectedSubject1 = new Subject(1, "Test Economics", "Base economics");
        Subject expectedSubject2 = new Subject(2, "Test Philosophy", "Base philosophy");
        Subject expectedSubject3 = new Subject(3, "Test Chemistry", "Base chemistry");
        Subject expectedSubject4 = new Subject(4, "Test Radiology", "Explore radiation");

        List<Subject> expectedSubjects = new ArrayList<>(
                Arrays.asList(expectedSubject1, expectedSubject2, expectedSubject3, expectedSubject4));
    }
}