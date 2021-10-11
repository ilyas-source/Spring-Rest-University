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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.ClassroomControllerTest.TestData.*;

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
                .andExpect(view().name("classroom/all"))
                .andExpect(model().attribute("classrooms", expectedClassrooms));

        verify(classroomService).findAll();
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithClassroom() throws Exception {
        when(classroomService.getById(1)).thenReturn(expectedClassroom1);

        mockMvc.perform(get("/classrooms/1"))
                .andExpect(view().name("classroom/details"))
                .andExpect(model().attribute("classroom", expectedClassroom1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        when(classroomService.getById(1)).thenThrow(new EntityNotFoundException("Can't find classroom by id 1"));
        assertThrows(org.springframework.web.util.NestedServletException.class,
                                        () -> mockMvc.perform(get("/classrooms/1"))
                                                .andExpect(view().name("exceptions/error")));
    }

    @Test
    void givenClassroom_onUpdate_shouldCallServiceUpdate() throws Exception {
    //    when(locationService.findById(1)).thenReturn(Optional.of(location1));

        mockMvc.perform(post("/classrooms/update")
                                .flashAttr("classroom", expectedClassroom1))
                                .andExpect(status().is3xxRedirection());

        verify(classroomService).update(expectedClassroom1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyClassroom() throws Exception {
        when(locationService.findAll()).thenReturn(expectedLocations);

        mockMvc.perform(get("/classrooms/new"))
                .andExpect(view().name("classroom/create"))
                .andExpect(model().attribute("classroom", new Classroom()))
                .andExpect(model().attribute("locations", expectedLocations))
                .andExpect(model().attribute("location", new Location()));
    }

    @Test
    void givenClassroom_onCreate_shouldCallServiceCreate() throws Exception {
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

    interface TestData {
        Location location1 = new Location(1, "Phys building", 2, 22);
        Classroom expectedClassroom1 = new Classroom(1, location1, "Big physics auditory", 500);

        Location location2 = new Location(2, "Chem building", 1, 12);
        Classroom expectedClassroom2 = new Classroom(2, location2, "Small chemistry auditory", 30);

        Location location3 = new Location(3, "Chem building", 2, 12);
        Classroom expectedClassroom3 = new Classroom(3, location3, "Chemistry laboratory", 15);

        List<Location> expectedLocations=new ArrayList<>(
                Arrays.asList(location1, location2, location3));

        List<Classroom> expectedClassrooms = new ArrayList<>(
                Arrays.asList(expectedClassroom1, expectedClassroom2, expectedClassroom3));
    }
}
