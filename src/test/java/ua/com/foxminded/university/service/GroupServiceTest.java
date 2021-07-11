package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.expectedGroup1;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.StudentDao;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao groupDao;
    @Mock
    private StudentDao studentDao;
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
    void givenCorrectId_onDelete_shouldCallDaoDelete() {
	when(groupDao.findById(1)).thenReturn(Optional.of(expectedGroup1));
	groupService.delete(1);

	verify(groupDao).delete(1);
    }

    @Test
    void givenExistingGroup_onCreate_shouldNotCallDaoCreate() {
	when(groupDao.findByName(expectedGroup1.getName())).thenReturn(Optional.of(expectedGroup1));

	groupService.create(expectedGroup1);

	verify(groupDao, never()).create(expectedGroup1);
    }

    @Test
    void givenExistingGroup_onUpdate_shouldNotCallDaoUpdate() {
	when(groupDao.findByName(expectedGroup1.getName())).thenReturn(Optional.of(expectedGroup1));
	groupService.update(expectedGroup1);

	verify(groupDao, never()).update(expectedGroup1);
    }

    @Test
    void givenWrongId_onDelete_shouldNotCallDaoDelete() throws Exception {
	when(groupDao.findById(1)).thenReturn(Optional.empty());

	groupService.delete(1);

	verify(groupDao, never()).delete(1);
    }
}