package ua.com.foxminded.university.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.hibernate.HibernateGroupDao;
import ua.com.foxminded.university.model.Group;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ua.com.foxminded.university.dao.HibernateGroupDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateGroupDaoTest {

    @Autowired
    private HibernateGroupDao groupDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    void givenNewGroup_onCreate_shouldCreateGroup() {
        var actual = hibernateTemplate.get(Group.class, 3);
        assertNull(actual);

        groupDao.create(groupToCreate);

        actual = hibernateTemplate.get(Group.class, 3);
        assertEquals(groupToCreate, actual);
    }

    @Test
    void givenCorrectGroupId_onFindById_shouldReturnOptionalWithCorrectGroup() {
        var expected = Optional.of(expectedGroup2);

        var actual = groupDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectGroupId_onFindById_shouldReturnEmptyOptional() {
        Optional<Group> expected = Optional.empty();

        var actual = groupDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasGroups_onFindAll_shouldReturnCorrectListOfGroups() {
        assertEquals(expectedGroups, groupDao.findAll());
    }

    @Test
    void ifDatabaseHasNoGroups_onFindAll_shouldReturnEmptyListOfGroups() {
        hibernateTemplate.deleteAll(expectedGroups);

        var groups = groupDao.findAll();

        assertThat(groups).isEmpty();
    }

    @Test
    void givenGroup_onUpdate_shouldUpdateCorrectly() {
        groupDao.update(groupToUpdate);

        var expected = hibernateTemplate.get(Group.class, 2);

        assertEquals(groupToUpdate, expected);
    }

    @Test
    void givenCorrectGroupId_onDelete_shouldDeleteCorrectly() {
        groupDao.delete(expectedGroup2);

        var expected = hibernateTemplate.get(Group.class, 2);
        assertNull(expected);
    }

    @Test
    void givenName_onFindByName_shouldReturnOptionalWithCorrectGroup() {
        var expected = Optional.of(expectedGroup1);

        var actual = groupDao.findByName(expectedGroup1.getName());

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongName_onFindByName_shouldReturnOptionalEmpty() {
        var actual = groupDao.findByName("Wrong name");

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void givenLectureId_onFindByLectureId_shouldReturnListOfGroups() {
        var actual = groupDao.findByLectureId(1);

        assertEquals(expectedGroups, actual);
    }

    public interface TestData {
        Group groupToCreate = new Group(3, "test");
        Group groupToUpdate = new Group(2, "test");

        Group expectedGroup1 = new Group(1, "AB-11");
        Group expectedGroup2 = new Group(2, "ZI-08");

        List<Group> expectedGroups = new ArrayList<>(
                Arrays.asList(expectedGroup1, expectedGroup2));
    }
}
