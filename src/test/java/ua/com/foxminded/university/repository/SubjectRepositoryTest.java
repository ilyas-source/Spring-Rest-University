package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Subject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SubjectRepositoryTest {

    @Autowired
    SubjectRepository SubjectRepository;

    @Test
    void givenSubjectId_onCountAssignments_shouldReturnNumberOfAssignments() {
        var actual = SubjectRepository.countAssignments(1);

        assertEquals(1, actual);
    }

    @Test
    void givenUnassignedSubjectId_onCountAssignments_shouldReturnZero() {
        var actual = SubjectRepository.countAssignments(5);

        assertEquals(0, actual);
    }

    interface TestData {
        Subject expectedSubject1 = new Subject(1, "Economics", "Base economics");
        Subject expectedSubject2 = new Subject(2, "Philosophy", "Base philosophy");
        Subject expectedSubject3 = new Subject(3, "Chemistry", "Base chemistry");
        Subject expectedSubject4 = new Subject(4, "Radiology", "Explore radiation");
    }
}