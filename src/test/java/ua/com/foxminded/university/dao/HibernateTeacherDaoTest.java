package ua.com.foxminded.university.dao;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.university.SpringTestConfig;

import javax.transaction.Transactional;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class HibernateTeacherDaoTest {

//    private static final String defaultSortDirection = "ASC";
//    private static final String defaultSortAttribute = "last_name";
//
//    @BeforeEach
//    void init() {
//        ReflectionTestUtils.setField(teacherDao, "defaultSortDirection", defaultSortDirection);
//        ReflectionTestUtils.setField(teacherDao, "defaultSortAttribute", defaultSortAttribute);
//    }
//
//    @Autowired
//    private HibernateTeacherDao teacherDao;
//    @Autowired
//    private HibernateTemplate hibernateTemplate;
//
//    @Test
//    void givenNewTeacher_onCreate_shouldCreateTeacher() {
//        var actual = hibernateTemplate.get(Teacher.class, 3);
//        assertNull(actual);
//
//        System.out.println("Creating " + teacherToCreate);
//        teacherDao.create(teacherToCreate);
//
//
//        actual = hibernateTemplate.get(Teacher.class, 3);
//        assertEquals(teacherToCreate, actual);
//    }
//
//    @Test
//    void givenCorrectTeacherId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
//        var expected = Optional.of(expectedTeacher2);
//
//        var actual = teacherDao.findById(2);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenIncorrectTeacherId_onFindById_shouldReturnEmptyOptional() {
//        Optional<Teacher> expected = Optional.empty();
//
//        var actual = teacherDao.findById(5);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasTeachers_onFindAll_shouldReturnCorrectListOfTeachers() {
//        assertEquals(expectedTeachers, teacherDao.findAll());
//    }
//
//    @Test
//    void ifDatabaseHasNoTeachers_onFindAll_shouldReturnEmptyListOfTeachers() {
//        hibernateTemplate.deleteAll(expectedTeachers);
//
//        var teachers = teacherDao.findAll();
//
//        assertThat(teachers).isEmpty();
//    }
//
//    @Test
//    void givenTeacher_onUpdate_shouldUpdateCorrectly() {
//        teacherDao.update(teacherToUpdate);
//
//        var expected = hibernateTemplate.get(Teacher.class, 2);
//
//        assertEquals(teacherToUpdate, expected);
//    }
//
//    @Test
//    void givenCorrectTeacherId_onDelete_shouldDeleteCorrectly() {
//        teacherDao.delete(expectedTeacher2);
//
//        var expected = hibernateTemplate.get(Teacher.class, 2);
//        assertNull(expected);
//    }
//
//    @Test
//    void givenNameAndEmail_onFindByNameAndEmail_shouldReturnOptionalwithCorrectTeacher() {
//        Optional<Teacher> expected = Optional.of(expectedTeacher1);
//
//        Optional<Teacher> actual = teacherDao.findByNameAndEmail("Adam", "Smith", "adam@smith.com");
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenLecture_onGetReplacementCandidates_shouldReturnCorrectListOfTeachers() {
//        var expected = new ArrayList<>(List.of(expectedTeacher2));
//
//        var actual = teacherDao.getReplacementCandidates(lectureToReplaceTeacher);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenString_onFindBySubstring_shouldReturnCorrectListOfTeachers() {
//        var expected = new ArrayList<>(List.of(expectedTeacher1));
//
//        var actual = teacherDao.findBySubstring("adam");
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenNameAndEmail_onFindByNameAndEmail_shouldReturnOptionalWithCorrectTeacher() {
//        var actual = teacherDao.findByNameAndEmail("Adam", "Smith", "adam@smith.com");
//
//        assertEquals(Optional.of(expectedTeacher1), actual);
//    }
//
//    @Test
//    void givenWrongData_onFindByNameAndEmail_shouldReturnOptionalEmpty() {
//        var actual = teacherDao.findByNameAndEmail("Adam1", "Smith", "adam@smith.com");
//
//        assertEquals(Optional.empty(), actual);
//    }
//
//    @Test
//    void givenPageable_onFindAll_shouldReturnCorrectPageOfTeachers() {
//        Pageable pageable = PageRequest.of(1, 1, Sort.by("id").ascending());
//
//        Page<Teacher> expected = new PageImpl<>(expectedTeachersPage, pageable, 2);
//        var actual = teacherDao.findAll(pageable);
//
//        assertEquals(expected, actual);
//    }
//
//    public interface TestData {
//        Set<Subject> testSubjects = new HashSet<>(List.of(subjectToUpdate));
//        List<Vacation> vacationsToCreate = new ArrayList<>(List.of(vacationToCreate));
//
//        List<Vacation> expectedVacations1 = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));
//        List<Vacation> expectedVacations2 = new ArrayList<>(Arrays.asList(expectedVacation3, expectedVacation4));
//        Set<Subject> expectedSubjects1 = new HashSet<>(Arrays.asList(expectedSubject1, expectedSubject2));
//        Set<Subject> expectedSubjects2 = new HashSet<>(Arrays.asList(expectedSubject3, expectedSubject4));
//
//        Teacher teacherToCreate = Teacher.builder().firstName("Test").lastName("Teacher").id(3)
//                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(testSubjects)
//                .email("test@mail").phoneNumber("phone").address(expectedAddress3)
//                .vacations(vacationsToCreate).build();
//
//        Teacher teacherToUpdate = Teacher.builder().firstName("Test").lastName("Teacher").id(2)
//                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(expectedSubjects1)
//                .email("test@mail").phoneNumber("phone").address(addressToUpdate)
//                .vacations(expectedVacations1).build();
//
//        Teacher expectedTeacher1 = Teacher.builder().firstName("Adam").lastName("Smith").id(1)
//                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(expectedSubjects1)
//                .email("adam@smith.com").phoneNumber("+223322").address(expectedAddress1)
//                .vacations(expectedVacations1).build();
//        Teacher expectedTeacher2 = Teacher.builder().firstName("Marie").lastName("Curie").id(2)
//                .gender(Gender.FEMALE).degree(Degree.MASTER).subjects(expectedSubjects2)
//                .email("marie@curie.com").phoneNumber("+322223").address(expectedAddress2)
//                .vacations(expectedVacations2).build();
//        List<Teacher> expectedTeachers = new ArrayList<>(Arrays.asList(expectedTeacher1, expectedTeacher2));
//
//        Lecture lectureToReplaceTeacher = Lecture.builder().date(LocalDate.of(2021, 1, 1)).
//                subject(expectedSubject3).timeslot(expectedTimeslot1).teacher(expectedTeacher2).build();
//
//        List<Teacher> expectedTeachersPage = new ArrayList<>(List.of(expectedTeacher2));
//    }
}
