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
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.GroupControllerTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;
    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllGroups() throws Exception {
        when(groupService.findAll()).thenReturn(expectedGroups);

        mockMvc.perform(get("/groups"))
                .andExpect(view().name("group/all"))
                .andExpect(model().attribute("groups", expectedGroups));

        verify(groupService).findAll();
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithGroup() throws Exception {
        when(groupService.getById(1)).thenReturn(expectedGroup1);

        mockMvc.perform(get("/groups/{id}", 1))
                .andExpect(view().name("group/details"))
                .andExpect(model().attribute("group", expectedGroup1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        when(groupService.getById(1)).thenThrow(new EntityNotFoundException("Can't find group by id 1"));

        mockMvc.perform(get("/groups/{id}", 1))
                .andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "EntityNotFoundException"))
                .andExpect(model().attribute("message", "Can't find group by id 1"));
    }

    @Test
    void givenGroup_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(post("/groups/update").flashAttr("group", expectedGroup1))
                .andExpect(status().is3xxRedirection());

        verify(groupService).update(expectedGroup1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyGroup() throws Exception {
        mockMvc.perform(get("/groups/new"))
                .andExpect(view().name("group/create"))
                .andExpect(model().attribute("group", new Group()));
    }

    @Test
    void givenGroup_onCreate_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/groups/create").flashAttr("group", expectedGroup1))
                .andExpect(status().is3xxRedirection());

        verify(groupService).create(expectedGroup1);
    }

    @Test
    void givenInvalidGroup_onCreate_shouldThrowValidationException() throws Exception {
        mockMvc.perform(post("/groups/create")
                        .flashAttr("group", invalidGroup))
                .andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "ValidationException"))
                .andExpect(model().attribute("message", "{length.between}"));;

        verify(groupService, never()).create(invalidGroup);
    }

    @Test
    void givenInvalidGroup_onUpdate_shouldThrowValidationException() throws Exception {
        mockMvc.perform(post("/groups/update")
                        .flashAttr("group", invalidGroup))
                .andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "ValidationException"))
                .andExpect(model().attribute("message", "{length.between}"));;

        verify(groupService, never()).create(invalidGroup);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/groups/delete/{id}", 1)).andExpect(status().is3xxRedirection());

        verify(groupService).delete(1);
    }

    interface TestData {
        Group expectedGroup1 = new Group(1, "AB-11");
        Group expectedGroup2 = new Group(2, "ZI-08");

        Group invalidGroup = new Group("Z");

        List<Group> expectedGroups = new ArrayList<>(
                Arrays.asList(expectedGroup1, expectedGroup2));
    }
}
