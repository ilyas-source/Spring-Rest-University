package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.GroupNotEmptyException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.GroupServiceTest.TestData.*;
import static ua.com.foxminded.university.service.StudentServiceTest.TestData.expectedStudents;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private GroupService groupService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(groupRepository.findAll()).thenReturn(expectedGroups);

        assertEquals(expectedGroups, groupService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectGroup() {
        when(groupRepository.findById(1)).thenReturn(Optional.of(expectedGroup1));
        Optional<Group> expected = Optional.of(expectedGroup1);

        Optional<Group> actual = groupService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNewGroup_onCreate_shouldCallRepositoryCreate() {
        when(groupRepository.findByName(expectedGroup1.getName())).thenReturn(Optional.empty());

        groupService.create(expectedGroup1);

        verify(groupRepository).save(expectedGroup1);
    }

    @Test
    void givenNewGroup_onUpdate_shouldCallRepositoryUpdate() {
        when(groupRepository.findByName(expectedGroup1.getName())).thenReturn(Optional.empty());

        groupService.update(expectedGroup1);

        verify(groupRepository).save(expectedGroup1);
    }

    @Test
    void givenGroupWithOldNameAndNewId_onUpdate_shouldThrowException() {
        String expected = "Group ZI-08 already exists";
        Group group = new Group(5, "ZI-08");
        when(groupRepository.findByName("ZI-08")).thenReturn(Optional.of(expectedGroup2));

        Throwable thrown = assertThrows(EntityNotUniqueException.class,
                () -> groupService.update(group));

        assertEquals(expected, thrown.getMessage());
        verify(groupRepository, never()).save(group);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallRepositoryDelete() {
        when(groupRepository.findById(1)).thenReturn(Optional.of(expectedGroup1));

        groupService.delete(1);

        verify(groupRepository).delete(expectedGroup1);
    }

    @Test
    void givenIncorrectGroupId_onDelete_shouldThrowException() {
        String expected = "Can't find group by id 1";
        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> groupService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(groupRepository, never()).delete(any());
    }

    @Test
    void givenNonEmptyGroup_onDelete_shouldThrowException() {
        String expected = "Group AB-11 has assigned students, can't delete";
        when(groupRepository.findById(1)).thenReturn(Optional.of(expectedGroup1));
        when(studentRepository.findByGroup(expectedGroup1)).thenReturn(expectedStudents);

        Throwable thrown = assertThrows(GroupNotEmptyException.class,
                () -> groupService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(groupRepository, never()).delete(expectedGroup1);
    }

    public interface TestData {
        Group expectedGroup1 = new Group(1, "AB-11");
        Group expectedGroup2 = new Group(2, "ZI-08");

        List<Group> expectedGroups = new ArrayList<>(
                Arrays.asList(expectedGroup1, expectedGroup2));

        Set<Group> expectedGroups1 = new HashSet<>(Arrays.asList(expectedGroup1, expectedGroup2));
        Set<Group> expectedGroups2 = new HashSet<>(List.of(expectedGroup1));
        Set<Group> testGroups = new HashSet<>(Arrays.asList(expectedGroup1, expectedGroup2));
        Set<Group> expectedGroupsAfterUpdate = new HashSet<>(List.of(expectedGroup2));
    }
}