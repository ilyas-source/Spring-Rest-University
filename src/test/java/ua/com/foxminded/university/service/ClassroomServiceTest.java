package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture1;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture2;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LectureDao;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private ClassroomDao classroomDao;
    @Mock
    private LectureDao lectureDao;
    @Mock
    private GroupDao groupDao;
    @InjectMocks
    private ClassroomService classroomService;

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	classroomService.findAll();

	verify(classroomDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	classroomService.findById(1);

	verify(classroomDao).findById(1);
    }

    @Test
    void givenGoodClassroom_onCreate_shouldCallDaoCreate() {
	classroomService.create(expectedClassroom1);

	verify(classroomDao).create(expectedClassroom1);
    }

    @Test
    void givenGoodClassroom_onUpdate_shouldCallDaoUpdate() {
	classroomService.update(expectedClassroom1);

	verify(classroomDao).update(expectedClassroom1);
    }

    @Test
    void givenSmallClassroom_onUpdate_shouldNotCallDaoUpdate() {
	when(lectureDao.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);
	when(lectureDao.countStudentsInLecture(expectedLecture1)).thenReturn(1000);
	when(lectureDao.countStudentsInLecture(expectedLecture2)).thenReturn(200);

	classroomService.update(expectedClassroom1);

	verify(classroomDao, never()).update(expectedClassroom1);
    }

    @Test
    void givenExistingClassroom_onDelete_shouldCallDaoDelete() {
	when(classroomDao.findById(1)).thenReturn(Optional.of(expectedClassroom1));
	classroomService.delete(1);

	verify(classroomDao).delete(1);
    }

    @Test
    void givenOccupiedClassroom_onDelete_shouldNotCallDaoDelete() {
	when(classroomDao.findById(1)).thenReturn(Optional.of(expectedClassroom1));
	when(lectureDao.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);

	classroomService.delete(1);

	verify(classroomDao, never()).delete(1);
    }

    @Test
    void givenClassroomWithExistingName_onCreate_shouldNotCallDaoCreate() {
	when(classroomDao.findByName(expectedClassroom1.getName())).thenReturn(Optional.of(expectedClassroom1));

	classroomService.create(expectedClassroom1);

	verify(classroomDao, never()).create(expectedClassroom1);
    }

    @Test
    void givenClassroomWithInvalidCapacity_onCreate_shouldNotCallDaoCreate() {
	int capacityBackup = expectedClassroom1.getCapacity();
	expectedClassroom1.setCapacity(-5);

	classroomService.create(expectedClassroom1);

	expectedClassroom1.setCapacity(capacityBackup);
	verify(classroomDao, never()).create(expectedClassroom1);
    }

    @Test
    void givenIncorrectClassroomId_onDelete_shouldNotCallDaoDelete() {
	classroomService.delete(1);

	verify(classroomDao, never()).delete(1);
    }
}