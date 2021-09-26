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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.expectedGroups;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;
    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllGroups() throws Exception {
        when(groupService.findAll()).thenReturn(expectedGroups);

        mockMvc.perform(get("/groups"))
                .andExpect(view().name("groupsView"))
                .andExpect(model().attribute("groups", expectedGroups));

        verify(groupService).findAll();
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithGroup() throws Exception {
        when(groupService.findById(1)).thenReturn(Optional.of(expectedGroup1));

        mockMvc.perform(get("/groups/1"))
                .andExpect(view().name("/details/group"))
                .andExpect(model().attribute("group", expectedGroup1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        String expected = "Can't find group by id 1";
        when(groupService.findById(1)).thenReturn(Optional.empty());
        Throwable thrown = assertThrows(org.springframework.web.util.NestedServletException.class,
                                        () -> mockMvc.perform(get("/groups/1")));
        Throwable cause = thrown.getCause();

        assertEquals(cause.getClass(), EntityNotFoundException.class);
        assertEquals(expected, cause.getMessage());
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
                .andExpect(view().name("/create/group"))
                .andExpect(model().attribute("group", new Group()));
    }

    @Test
    void givenGroup_onCreate_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/groups/create").flashAttr("group", expectedGroup1))
                .andExpect(status().is3xxRedirection());

        verify(groupService).create(expectedGroup1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/groups/delete/1")).andExpect(status().is3xxRedirection());

        verify(groupService).delete(1);
    }
}
