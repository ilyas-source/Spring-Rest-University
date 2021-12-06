package ua.com.foxminded.university.rest;

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
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.service.ClassroomService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.rest.ClassroomRestControllerTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.rest.ClassroomRestControllerTest.TestData.expectedClassrooms;

@ExtendWith(MockitoExtension.class)
public class ClassroomRestControllerTest {

    private MockMvc mockMvc;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ClassroomService classroomService;
    @InjectMocks
    private ClassroomRestController classroomRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(classroomRestController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(classroomService.findAll()).thenReturn(expectedClassrooms);

        MvcResult mvcResult = mockMvc.perform(get("/api/classrooms"))
                .andExpect(status().isOk())
                .andReturn();
        var actual = mapToList(mvcResult, Classroom.class);

        assertEquals(expectedClassrooms, actual);
    }

    static Object mapToObject(MvcResult mvcResult, Class targetClass)
            throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), targetClass);
    }

    static <T> List<T> mapToList(MvcResult mvcResult,
                                        Class<T> targetClass)
            throws IOException, ClassNotFoundException {
        var json = mvcResult.getResponse().getContentAsString();
        Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + targetClass.getName() + ";");
        T[] objects = objectMapper.readValue(json, arrayClass);
        return Arrays.asList(objects);
    }

    @Test
    void givenId_onGetClassroom_shouldReturnCorrectJson() throws Exception {
        when(classroomService.getById(1)).thenReturn(expectedClassroom1);

        MvcResult mvcResult = mockMvc.perform(get("/api/classrooms/{id}", 1))
                .andExpect(status().isOk()).andReturn();

        var actual = mapToObject(mvcResult, Classroom.class);

        verify(classroomService).getById(1);
        assertEquals(expectedClassroom1, actual);
    }

    @Test
    void givenWrongId_onGetClassroom_shouldReturn404() throws Exception {
        when(classroomService.getById(1)).thenThrow(new EntityNotFoundException("Can't find classroom by id 1"));

        mockMvc.perform(get("/api/classrooms/{id}", 1))
                .andExpect(status().is4xxClientError());

        verify(classroomService).getById(1);
    }

    @Test
    void givenClassroom_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/classrooms")
                        .content(objectMapper.writeValueAsString(expectedClassroom1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        verify(classroomService).create(expectedClassroom1);
    }

    @Test
    void givenClassroom_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(put("/api/classrooms/{id}", 1)
                        .content(objectMapper.writeValueAsString(expectedClassroom1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(classroomService).update(expectedClassroom1);
    }

    @Test
    void givenClassroom_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/classrooms/{id}", 1))
                .andExpect(status().isNoContent());

        verify(classroomService).delete(1);
    }

    interface TestData {
        Location location1 = new Location(1, "Phys building", 2, 22);
        Classroom expectedClassroom1 = new Classroom(1, location1, "Big physics auditory", 500);

        Location location2 = new Location(2, "Chem building", 1, 12);
        Classroom expectedClassroom2 = new Classroom(2, location2, "Small chemistry auditory", 30);

        Location location3 = new Location(3, "Chem building", 2, 12);
        Classroom expectedClassroom3 = new Classroom(3, location3, "Chemistry laboratory", 15);

        List<Classroom> expectedClassrooms = new ArrayList<>(
                Arrays.asList(expectedClassroom1, expectedClassroom2, expectedClassroom3));
    }
}


