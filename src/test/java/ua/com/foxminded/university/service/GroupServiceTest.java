package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.GroupNotEmptyException;
import ua.com.foxminded.university.model.Group;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.dao.HibernateGroupDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudents;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao groupDao;
    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private GroupService groupService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(groupDao.findAll()).thenReturn(expectedGroups);

        assertEquals(expectedGroups, groupService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectGroup() {
        when(groupDao.findById(1)).thenReturn(Optional.of(expectedGroup1));
        Optional<Group> expected = Optional.of(expectedGroup1);

        Optional<Group> actual = groupService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNewGroup_onCreate_shouldCallDaoCreate() {
        when(groupDao.findByName(expectedGroup1.getName())).thenReturn(Optional.empty());

        groupService.create(expectedGroup1);

        verify(groupDao).create(expectedGroup1);
    }

    @Test
    void givenNewGroup_onUpdate_shouldCallDaoUpdate() {
        when(groupDao.findByName(expectedGroup1.getName())).thenReturn(Optional.empty());

        groupService.update(expectedGroup1);

        verify(groupDao).update(expectedGroup1);
    }

    @Test
    void givenGroupWithOldNameAndNewId_onUpdate_shouldThrowException() {
        String expected = "Group ZI-08 already exists";
        Group group = new Group(5, "ZI-08");
        when(groupDao.findByName("ZI-08")).thenReturn(Optional.of(expectedGroup2));

        Throwable thrown = assertThrows(EntityNotUniqueException.class,
                () -> groupService.update(group));

        assertEquals(expected, thrown.getMessage());
        verify(groupDao, never()).update(group);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallDaoDelete() {
        when(groupDao.findById(1)).thenReturn(Optional.of(expectedGroup1));

        groupService.delete(1);

     //   verify(groupDao).delete(1);
    }

    @Test
    void givenIncorrectGroupId_onDelete_shouldThrowException() {
        String expected = "Can't find group by id 1";
        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> groupService.delete(1));

        assertEquals(expected, thrown.getMessage());
    //    verify(groupDao, never()).delete(1);
    }

    @Test
    void givenNonEmptyGroup_onDelete_shouldThrowException() {
        String expected = "Group AB-11 has assigned students, can't delete";
        when(groupDao.findById(1)).thenReturn(Optional.of(expectedGroup1));
        when(studentDao.findByGroup(expectedGroup1)).thenReturn(expectedStudents);

        Throwable thrown = assertThrows(GroupNotEmptyException.class,
                () -> groupService.delete(1));

        assertEquals(expected, thrown.getMessage());
     //   verify(groupDao, never()).delete(1);
    }
}