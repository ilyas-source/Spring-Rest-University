package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.repository.ClassroomRepositoryTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.repository.ClassroomRepositoryTest.TestData.testLocation;

@DataJpaTest
public class ClassroomRepositoryTest {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Test
    void givenName_onFindByName_shouldReturnOptionalWithCorrectClassroom() {
        Optional<Classroom> expected = Optional.of(expectedClassroom1);

        Optional<Classroom> actual = classroomRepository.findByName(expectedClassroom1.getName());

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongName_onFindByName_shouldReturnOptionalEmpty() {
        Optional<Classroom> actual = classroomRepository.findByName("wrong name");

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void givenLocation_onFindByLocation_shouldReturnOptionalWithCorrectClassroom() {
        var expected = Optional.of(expectedClassroom1);
        var actual = classroomRepository.findByLocation(expectedClassroom1.getLocation());

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongLocation_onFindByLocation_shouldReturnOptionalEmpty() {
        var actual = classroomRepository.findByLocation(testLocation);

        assertEquals(Optional.empty(), actual);
    }

    interface TestData {
        Location testLocation = new Location(4, "Test location", 1, 1);

        Location location1 = new Location(1, "Phys building", 2, 22);
        Classroom expectedClassroom1 = new Classroom(1, location1, "Big physics auditory", 500);

        Location location2 = new Location(2, "Chem building", 1, 12);
        Classroom expectedClassroom2 = new Classroom(2, location2, "Small chemistry auditory", 30);
    }
}
