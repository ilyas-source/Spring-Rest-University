package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Subject;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.repository.SubjectRepositoryTest.TestData.expectedSubject1;

@DataJpaTest
public class SubjectRepositoryTest {

    @Autowired
    SubjectRepository subjectRepository;

    @Test
    void givenString_onFindByName_shouldReturnOptionalWithCorrectSubject() {
        var actual = subjectRepository.findByName("Economics");

        assertEquals(Optional.of(expectedSubject1), actual);
    }

    interface TestData {
        Subject expectedSubject1 = new Subject(1, "Economics", "Base economics");
        Subject expectedSubject2 = new Subject(2, "Philosophy", "Base philosophy");
        Subject expectedSubject3 = new Subject(3, "Chemistry", "Base chemistry");
        Subject expectedSubject4 = new Subject(4, "Radiology", "Explore radiation");
    }
}