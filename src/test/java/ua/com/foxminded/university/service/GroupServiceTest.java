package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private JdbcGroupDao groupDao;
    @InjectMocks
    private GroupService groupService;

    @Test
    void onFindAll_shouldReturnAllGroups() {
	when(groupDao.findAll()).thenReturn(expectedGroups);

	assertEquals(expectedGroups, groupService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectGroup() {
	when(groupDao.findById(1)).thenReturn(Optional.of(expectedGroup1));

	assertEquals(Optional.of(expectedGroup1), groupService.findById(1));
    }

    @Test
    void givenGroup_onCreate_shouldCallCreate() {
	groupService.create(expectedGroup1);

	verify(groupDao).create(expectedGroup1);
    }

    @Test
    void givenGroup_onUpdate_shouldCallUpdate() {
	groupService.update(expectedGroup1);

	verify(groupDao).update(expectedGroup1);
    }

    @Test
    void givenGroup_onDelete_shouldCallDelete() {
	groupService.delete(1);

	verify(groupDao).delete(1);
    }
}