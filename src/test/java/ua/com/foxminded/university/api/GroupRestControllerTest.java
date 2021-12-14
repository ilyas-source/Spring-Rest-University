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
import ua.com.foxminded.university.api.dto.GroupDto;
import ua.com.foxminded.university.api.mapper.GroupMapper;
import ua.com.foxminded.university.controller.ControllerExceptionHandler;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.GroupRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;

@DataJpaTest
public class GroupRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedGroupJson;
    String expectedGroupsJson;

    @Mock
    private GroupService groupService;
    @Mock
    private GroupMapper mapper;
    @InjectMocks
    private GroupRestController groupRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(groupRestController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        expectedGroupJson = objectMapper.writeValueAsString(expectedGroup1);
        expectedGroupsJson = objectMapper.writeValueAsString(expectedGroups);
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

        mockMvc.perform(get("/api/groups/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedGroupJson));

        verify(groupService).getById(groupId);
    }

    @Test
    void givenGroup_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/groups")
                        .content(expectedGroupJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(groupService).create(expectedGroup1);
    }

    @Test
    void givenGroupDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.groupDtoToGroup(groupDto)).thenReturn(expectedGroup1);
        MvcResult mvcResult = mockMvc.perform(put("/api/groups/{id}", groupId)
                        .content(objectMapper.writeValueAsString(groupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Group.class);

        verify(groupService).update(expectedGroup1);
        assertEquals(expectedGroup1, actual);
    }

    @Test
    void givenGroup_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/groups/{id}", groupId))
                .andExpect(status().isNoContent());

        verify(groupService).delete(1);
    }

    interface TestData {
        int groupId = 1;

        GroupDto groupDto = new GroupDto("AB-11");
        GroupDto groupDto2 = new GroupDto("ZI-08");

        Set<GroupDto> groupDtos =new HashSet<>(Arrays.asList(groupDto, groupDto2));

        Group expectedGroup1 = new Group(1, "AB-11");
        Group expectedGroup2 = new Group(2, "ZI-08");

        List<Group> expectedGroups = new ArrayList<>(Arrays.asList(expectedGroup1, expectedGroup2));
    }
}
