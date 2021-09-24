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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubjects;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllSubjects() throws Exception {
        when(subjectService.findAll()).thenReturn(expectedSubjects);

        mockMvc.perform(get("/subjects"))
                .andExpect(view().name("subjectsView"))
                .andExpect(model().attribute("subjects", expectedSubjects));
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithSubject() throws Exception {
        when(subjectService.findById(1)).thenReturn(java.util.Optional.of(expectedSubject1));

        mockMvc.perform(get("/subjects/1"))
                .andExpect(view().name("/details/subject"))
                .andExpect(model().attribute("subject", expectedSubject1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        String expected = "Can't find subject by id 1";
        when(subjectService.findById(1)).thenReturn(Optional.empty());
        Throwable thrown = assertThrows(org.springframework.web.util.NestedServletException.class,
                                        () -> mockMvc.perform(get("/subjects/1")));
        Throwable cause = thrown.getCause();

        assertEquals(cause.getClass(), EntityNotFoundException.class);
        assertEquals(expected, cause.getMessage());
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
                .andExpect(view().name("/create/subject"))
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
}