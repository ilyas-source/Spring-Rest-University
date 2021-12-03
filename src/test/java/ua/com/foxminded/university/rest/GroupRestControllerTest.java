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
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.rest.GroupRestControllerTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.rest.GroupRestControllerTest.TestData.expectedGroups;

@DataJpaTest
public class GroupRestControllerTest {

    private MockMvc mockMvc;
    String expectedGroupJson;
    String expectedGroupsJson;

    @Mock
    private GroupService groupService;
    @InjectMocks
    private GroupRestController groupRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(groupRestController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        expectedGroupJson = new ObjectMapper().writeValueAsString(expectedGroup1);
        expectedGroupsJson = new ObjectMapper().writeValueAsString(expectedGroups);
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(groupService.findAll()).thenReturn(expectedGroups);

        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedGroupsJson));
    }

    @Test
    void givenId_onGetGroup_shouldReturnCorrectJson() throws Exception {
        when(groupService.getById(1)).thenReturn(expectedGroup1);

        mockMvc.perform(get("/api/groups/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedGroupJson));

        verify(groupService).getById(1);
    }

    @Test
    void givenGroup_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/groups")
                        .content(expectedGroupJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(groupService).create(new Group(0, "AB-11"));
    }

    @Test
    void givenGroup_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(put("/api/groups/{id}", 1)
                        .content(expectedGroupJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(groupService).update(expectedGroup1);
    }

    @Test
    void givenGroup_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/groups/{id}", 1))
                .andExpect(status().isOk());

        verify(groupService).delete(1);
    }

    interface TestData {
        Group expectedGroup1 = new Group(1, "AB-11");
        Group expectedGroup2 = new Group(2, "ZI-08");

        List<Group> expectedGroups = new ArrayList<>(
                Arrays.asList(expectedGroup1, expectedGroup2));
    }
}
