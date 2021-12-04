package ua.com.foxminded.university.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.controller.ControllerExceptionHandler;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.service.ClassroomService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.rest.ClassroomRestControllerTest.TestData.*;

@DataJpaTest
public class ClassroomRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedClassroomJson;
    String expectedClassroomsJson;

    @Mock
    private ClassroomService classroomService;
    @InjectMocks
    private ClassroomRestController classroomRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(classroomRestController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        expectedClassroomJson = objectMapper.writeValueAsString(expectedClassroom1);
        expectedClassroomsJson = objectMapper.writeValueAsString(expectedClassrooms);
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(classroomService.findAll()).thenReturn(expectedClassrooms);

        mockMvc.perform(get("/api/classrooms"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedClassroomsJson));
    }

    @Test
    void givenId_onGetClassroom_shouldReturnCorrectJson() throws Exception {
        when(classroomService.getById(1)).thenReturn(expectedClassroom1);

        mockMvc.perform(get("/api/classrooms/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedClassroomJson));

        verify(classroomService).getById(1);
    }

    @Test
    void givenClassroom_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/classrooms")
                        .content(expectedClassroomJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(classroomService).create(expectedClassroom1);
    }

    @Test
    void givenClassroom_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(put("/api/classrooms/{id}", 1)
                        .content(expectedClassroomJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(classroomService).update(expectedClassroom1);
    }

    @Test
    void givenClassroom_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/classrooms/{id}", 1))
                .andExpect(status().isOk());

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


