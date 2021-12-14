package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.dto.SubjectDto;
import ua.com.foxminded.university.api.mapper.SubjectMapper;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.SubjectService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.SubjectRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToList;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;

@ExtendWith(MockitoExtension.class)
public class SubjectRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private SubjectService subjectService;
    @Mock
    private SubjectMapper mapper;
    @InjectMocks
    private SubjectRestController subjectRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectRestController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(subjectService.findAll()).thenReturn(expectedSubjects);

        MvcResult mvcResult = mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andReturn();
        List<Subject> actual = mapToList(mvcResult, Subject.class);

        assertEquals(expectedSubjects, actual);
    }

    @Test
    void givenId_onGetSubject_shouldReturnCorrectJson() throws Exception {
        when(subjectService.getById(subjectId)).thenReturn(expectedSubject1);

        MvcResult mvcResult = mockMvc.perform(get("/api/subjects/{id}", subjectId))
                .andExpect(status().isOk()).andReturn();

        var actual = mapToObject(mvcResult, Subject.class);

        verify(subjectService).getById(subjectId);
        assertEquals(expectedSubject1, actual);
    }

    @Test
    void givenSubjectDto_onSave_shouldCallServiceCreate() throws Exception {
        when(mapper.subjectDtoToSubject(subjectDto)).thenReturn(expectedSubject1);
        mockMvc.perform(post("/api/subjects")
                        .content(objectMapper.writeValueAsString(subjectDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/subjects/1"))
                .andReturn();
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
        SubjectDto subjectDto2 = new SubjectDto("Test Philosophy", "Base philosophy");

        Set<SubjectDto> subjectDtos = new HashSet<>(Arrays.asList(subjectDto, subjectDto2));

        Subject expectedSubject1 = new Subject(1, "Test Economics", "Base economics");
        Subject expectedSubject2 = new Subject(2, "Test Philosophy", "Base philosophy");
        Subject expectedSubject3 = new Subject(3, "Test Chemistry", "Base chemistry");
        Subject expectedSubject4 = new Subject(4, "Test Radiology", "Explore radiation");

        List<Subject> expectedSubjects = new ArrayList<>(
                Arrays.asList(expectedSubject1, expectedSubject2, expectedSubject3, expectedSubject4));
    }
}
