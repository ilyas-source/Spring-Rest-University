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
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.service.ClassroomService;
import ua.com.foxminded.university.service.LocationService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassrooms;
import static ua.com.foxminded.university.dao.LocationDaoTest.TestData.expectedLocation1;
import static ua.com.foxminded.university.dao.LocationDaoTest.TestData.expectedLocations;

@ExtendWith(MockitoExtension.class)
class ClassroomControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClassroomService classroomService;
    @Mock
    private LocationService locationService;
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

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithClassroom() throws Exception {
        when(classroomService.findById(1)).thenReturn(java.util.Optional.of(expectedClassroom1));
        when(locationService.findAll()).thenReturn(expectedLocations);

        mockMvc.perform(get("/classrooms/1"))
                .andExpect(view().name("/details/classroom"))
                .andExpect(model().attribute("classroom", expectedClassroom1))
                .andExpect(model().attribute("locations", expectedLocations))
                .andExpect(model().attribute("location", new Location()));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        String expected = "Can't find classroom by id 1";
        when(classroomService.findById(1)).thenReturn(Optional.empty());
        Throwable thrown = assertThrows(org.springframework.web.util.NestedServletException.class,
                                        () -> mockMvc.perform(get("/classrooms/1")));
        Throwable cause = thrown.getCause();

        assertEquals(cause.getClass(), EntityNotFoundException.class);
        assertEquals(expected, cause.getMessage());
    }

    @Test
    void givenClassroom_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(post("/classrooms/update").flashAttr("classroom", expectedClassroom1))
                .andExpect(status().is3xxRedirection());

        verify(classroomService).update(expectedClassroom1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyClassroom() throws Exception {
        when(locationService.findAll()).thenReturn(expectedLocations);

        mockMvc.perform(get("/classrooms/new"))
                .andExpect(view().name("/create/classroom"))
                .andExpect(model().attribute("classroom", new Classroom()))
                .andExpect(model().attribute("locations", expectedLocations))
                .andExpect(model().attribute("location", new Location()));
    }

    @Test
    void givenClassroom_onCreate_shouldCallServiceCreate() throws Exception {
        when(locationService.findById(1)).thenReturn(Optional.of(expectedLocation1));
        mockMvc.perform(post("/classrooms/create")
                                .flashAttr("classroom", expectedClassroom1))
                .andExpect(status().is3xxRedirection());

        verify(classroomService).create(expectedClassroom1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/classrooms/delete/1")).andExpect(status().is3xxRedirection());

        verify(classroomService).delete(1);
    }

    @Test
    void givenClassroomWithNonExistingLocation_onCreate_shouldThrowException() throws Exception {
        when(locationService.findById(1)).thenReturn(Optional.empty());
        String expected="Location not found by id=1";
        Throwable thrown = assertThrows(org.springframework.web.util.NestedServletException.class,
                                        () -> mockMvc.perform(post("/classrooms/create")
                                                .flashAttr("classroom", expectedClassroom1))
                                                .andExpect(status().is3xxRedirection()));
        Throwable cause = thrown.getCause();

        assertEquals(cause.getClass(), EntityNotFoundException.class);
        assertEquals(expected, cause.getMessage());
    }
}
