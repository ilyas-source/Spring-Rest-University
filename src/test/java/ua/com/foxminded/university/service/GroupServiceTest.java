package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void onFindAll_shouldCallDaoFindAll() {
	groupService.findAll();

	verify(groupDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	groupService.findById(1);

	verify(groupDao).findById(1);
    }

    @Test
    void givenNewGroup_onCreate_shouldCallDaoCreate() throws Exception {
	when(groupDao.findByName(expectedGroup1.getName())).thenReturn(Optional.empty());
	groupService.create(expectedGroup1);

	verify(groupDao).create(expectedGroup1);
    }

    @Test
    void givenNewGroup_onUpdate_shouldCallUpdate() throws Exception {
	when(groupDao.findByName(expectedGroup1.getName())).thenReturn(Optional.empty());
	groupService.update(expectedGroup1);

	verify(groupDao).update(expectedGroup1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallDelete() throws Exception {
	when(groupDao.findById(1)).thenReturn(Optional.of(expectedGroup1));
	groupService.delete(1);

	verify(groupDao).delete(1);
    }

    @Test
    void givenExistingGroup_onCreate_shouldThrowException() {
	when(groupDao.findByName(expectedGroup1.getName())).thenReturn(Optional.of(expectedGroup1));
	String expected = String.format("Group with name %s already exists, can't create/update", expectedGroup1.getName());

	Throwable thrown = assertThrows(Exception.class, () -> {
	    groupService.create(expectedGroup1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenExistingGroup_onUpdate_shouldThrowException() {
	when(groupDao.findByName(expectedGroup1.getName())).thenReturn(Optional.of(expectedGroup1));
	String expected = String.format("Group with name %s already exists, can't create/update", expectedGroup1.getName());

	Throwable thrown = assertThrows(Exception.class, () -> {
	    groupService.update(expectedGroup1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenWrongId_onDelete_shouldThrowException() throws Exception {
	when(groupDao.findById(1)).thenReturn(Optional.empty());
	String expected = String.format("Group with id %s does not exist, nothing to delete", expectedGroup1.getId());

	Throwable thrown = assertThrows(Exception.class, () -> {
	    groupService.delete(1);
	});

	assertEquals(expected, thrown.getMessage());
    }
}