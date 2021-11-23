package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Group;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void givenName_onFindByName_shouldReturnOptionalWithCorrectGroup() {
        var actual= groupRepository.findByName("AB-11");

        var expected=new Group(1, "AB-11");

        assertEquals(expected, actual);
    }

    interface TestData {
        Group expectedGroup1 = new Group(1, "AB-11");
        Group expectedGroup2 = new Group(2, "ZI-08");
    }
}
