package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.dto.SubjectDto;
import ua.com.foxminded.university.api.mapper.SubjectMapper;
import ua.com.foxminded.university.controller.ControllerExceptionHandler;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.SubjectService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.SubjectRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;

@DataJpaTest
public class SubjectRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedSubjectJson;
    String expectedSubjectsJson;

    @Mock
    private SubjectService subjectService;
    @Mock
    private SubjectMapper mapper;
    @InjectMocks
    private SubjectRestController subjectRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectRestController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        expectedSubjectJson = objectMapper.writeValueAsString(expectedSubject1);
        expectedSubjectsJson = objectMapper.writeValueAsString(expectedSubjects);
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(subjectService.findAll()).thenReturn(expectedSubjects);

        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedSubjectsJson));
    }

    @Test
    void givenId_onGetSubject_shouldReturnCorrectJson() throws Exception {
        when(subjectService.getById(1)).thenReturn(expectedSubject1);

        mockMvc.perform(get("/api/subjects/{id}", subjectId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedSubjectJson));

        verify(subjectService).getById(subjectId);
    }

    @Test
    void givenSubject_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/subjects")
                        .content(expectedSubjectJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(subjectService).create(expectedSubject1);
    }

    @Test
    void givenSubjectDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.subjectDtoToSubject(subjectDto)).thenReturn(expectedSubject1);
        MvcResult mvcResult = mockMvc.perform(put("/api/subjects/{id}", subjectId)
                        .content(objectMapper.writeValueAsString(subjectDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Subject.class);

        verify(subjectService).update(expectedSubject1);
        assertEquals(expectedSubject1, actual);
    }

    @Test
    void givenSubject_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/subjects/{id}", subjectId))
                .andExpect(status().isNoContent());

        verify(subjectService).delete(1);
    }

    interface TestData {
        int subjectId = 1;

        SubjectDto subjectDto = new SubjectDto("Test Economics", "Base economics");

        Subject expectedSubject1 = new Subject(1, "Test Economics", "Base economics");
        Subject expectedSubject2 = new Subject(2, "Test Philosophy", "Base philosophy");
        Subject expectedSubject3 = new Subject(3, "Test Chemistry", "Base chemistry");
        Subject expectedSubject4 = new Subject(4, "Test Radiology", "Explore radiation");

        List<Subject> expectedSubjects = new ArrayList<>(
                Arrays.asList(expectedSubject1, expectedSubject2, expectedSubject3, expectedSubject4));
    }
}
