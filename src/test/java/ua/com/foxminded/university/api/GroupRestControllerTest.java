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
import ua.com.foxminded.university.api.dto.GroupDto;
import ua.com.foxminded.university.api.mapper.GroupMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.GroupRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToList;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;

@ExtendWith(MockitoExtension.class)
public class GroupRestControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private GroupService groupService;
    @Mock
    private GroupMapper mapper;
    @InjectMocks
    private GroupRestController groupRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(groupRestController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(groupService.findAll()).thenReturn(expectedGroups);

        MvcResult mvcResult = mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andReturn();
        List<Group> actual = mapToList(mvcResult, Group.class);

        assertEquals(expectedGroups, actual);
    }

    @Test
    void givenId_onGetGroup_shouldReturnCorrectJson() throws Exception {
        when(groupService.getById(groupId)).thenReturn(expectedGroup1);

        MvcResult mvcResult = mockMvc.perform(get("/api/groups/{id}", groupId))
                .andExpect(status().isOk()).andReturn();

        var actual = mapToObject(mvcResult, Group.class);

        verify(groupService).getById(groupId);
        assertEquals(expectedGroup1, actual);
    }




    @Test
    void givenGroupDto_onSave_shouldCallServiceCreate() throws Exception {
        when(mapper.groupDtoToGroup(groupDto)).thenReturn(expectedGroup1);
        mockMvc.perform(post("/api/groups")
                        .content(objectMapper.writeValueAsString(groupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/groups/1"))
                .andReturn();
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

        verify(groupService).delete(groupId);
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
