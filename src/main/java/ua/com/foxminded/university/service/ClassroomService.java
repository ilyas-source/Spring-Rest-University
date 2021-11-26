package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.exception.ClassroomInvalidCapacityException;
import ua.com.foxminded.university.exception.ClassroomOccupiedException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.repository.ClassroomRepository;
import ua.com.foxminded.university.repository.LectureRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomService.class);

    private ClassroomRepository classroomRepository;
    private LectureRepository lectureRepository;
    private LectureService lectureService;

    public ClassroomService(ClassroomRepository classroomRepository, LectureRepository lectureRepository, LectureService lectureService) {
        this.classroomRepository = classroomRepository;
        this.lectureRepository = lectureRepository;
        this.lectureService = lectureService;
    }

    public void create(Classroom classroom) {
        logger.debug("Creating a new classroom: {} ", classroom);
        verifyCapacityIsCorrect(classroom);
        verifyNameIsUnique(classroom);
        classroomRepository.save(classroom);
    }

    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    public Optional<Classroom> findById(int id) {
        return classroomRepository.findById(id);
    }

    public Classroom getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find classroom by id " + id));
    }

    public void update(Classroom classroom) {
        logger.debug("Updating classroom: {} ", classroom);
        verifyCapacityIsEnough(classroom);
        verifyNameIsUnique(classroom);
        classroomRepository.save(classroom);
    }

    public void delete(int id) {
        logger.debug("Deleting classroom by id: {} ", id);
        Classroom classroom = getById(id);
        verifyHasNoLectures(classroom);
        classroomRepository.delete(classroom);
    }

    private void verifyNameIsUnique(Classroom classroom) {
        classroomRepository.findByName(classroom.getName())
                .filter(c -> c.getId() != classroom.getId())
                .ifPresent(c -> {
                    throw new EntityNotUniqueException(
                            String.format("Classroom %s already exists", classroom.getName()));
                });
    }

    private void verifyCapacityIsEnough(Classroom classroom) {
        int requiredCapacity = lectureRepository.findByClassroom(classroom)
                .stream()
                .map(lectureService::countStudentsInLecture)
                .mapToInt(v -> v)
                .max().orElse(0);
        if (classroom.getCapacity() < requiredCapacity) {
            throw new ClassroomInvalidCapacityException(
                    String.format("Classroom too small: required %s, but was %s", requiredCapacity, classroom.getCapacity()));
        }
    }

    private void verifyCapacityIsCorrect(Classroom classroom) {
        if (classroom.getCapacity() < 1) {
            throw new ClassroomInvalidCapacityException("Classroom capacity should not be less than 1");
        }
    }

    private void verifyHasNoLectures(Classroom classroom) {
        if (!lectureRepository.findByClassroom(classroom).isEmpty()) {
            throw new ClassroomOccupiedException(
                    String.format("There are scheduled lectures, can't delete classroom %s", classroom.getName()));
        }
    }
}
