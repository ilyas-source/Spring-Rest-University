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
import ua.com.foxminded.university.api.dto.ClassroomDto;
import ua.com.foxminded.university.api.dto.LocationDto;
import ua.com.foxminded.university.api.mapper.ClassroomMapper;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.service.ClassroomService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.ClassroomRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;

@ExtendWith(MockitoExtension.class)
public class ClassroomRestControllerTest {

    private MockMvc mockMvc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private int classroomId = 1;
    @Mock
    private ClassroomService classroomService;
    @Mock
    private ClassroomMapper mapper;
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

    static <T> List<T> mapToList(MvcResult mvcResult,
                                 Class<T> targetClass)
            throws IOException, ClassNotFoundException {
        var json = mvcResult.getResponse().getContentAsString();
        Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + targetClass.getName() + ";");
        T[] objects = objectMapper.readValue(json, arrayClass);
        return Arrays.asList(objects);
    }

    @Test
    void givenId_onClassroom_shouldReturnCorrectJson() throws Exception {
        when(classroomService.getById(classroomId)).thenReturn(expectedClassroom1);

        MvcResult mvcResult = mockMvc.perform(get("/api/classrooms/{id}", classroomId))
                .andExpect(status().isOk()).andReturn();

        var actual=mapToObject(mvcResult, Classroom.class);

        verify(classroomService).getById(classroomId);
        assertEquals(expectedClassroom1, actual);
    }

    @Test
    void givenWrongId_onGetClassroom_shouldReturn404() throws Exception {
        when(classroomService.getById(classroomId)).thenThrow(new EntityNotFoundException("Can't find classroom by id 1"));

        mockMvc.perform(get("/api/classrooms/{id}", classroomId))
                .andExpect(status().isNotFound());

        verify(classroomService).getById(classroomId);
    }

    @Test
    void givenClassroomDto_onSave_shouldCallServiceCreate() throws Exception {
        when(mapper.classroomDtoToClassroom(classroomDto)).thenReturn(expectedClassroom1);
        mockMvc.perform(post("/api/classrooms")
                        .content(objectMapper.writeValueAsString(classroomDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/classrooms/1"))
                .andReturn();
        verify(classroomService).create(expectedClassroom1);
    }

    @Test
    void givenClassroomDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.classroomDtoToClassroom(classroomDto)).thenReturn(expectedClassroom1);
        MvcResult mvcResult = mockMvc.perform(put("/api/classrooms/{id}", classroomId)
                        .content(objectMapper.writeValueAsString(classroomDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Classroom.class);

        verify(classroomService).update(expectedClassroom1);
        assertEquals(expectedClassroom1, actual);
    }

    @Test
    void givenClassroom_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/classrooms/{id}", classroomId))
                .andExpect(status().isNoContent());

        verify(classroomService).delete(classroomId);
    }

    interface TestData {
        LocationDto locationDto = new LocationDto("Phys building", 2, 22);
        ClassroomDto classroomDto = new ClassroomDto("Big physics auditory", 500, locationDto);

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


