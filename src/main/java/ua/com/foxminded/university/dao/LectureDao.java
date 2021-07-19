package ua.com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Timeslot;

public interface LectureDao extends GeneralDao<Lecture> {

    List<Lecture> findByClassroom(Classroom classroom);

    List<Lecture> findByTeacher(Teacher teacher);

    Optional<Lecture> findByDateTimeClassroom(LocalDate date, Timeslot timeslot, Classroom classroom);

    List<Lecture> findByDateTime(LocalDate date, Timeslot timeslot);

    Optional<Lecture> findByDateTimeTeacher(LocalDate date, Timeslot timeslot, Teacher teacher);

    List<Lecture> findBySubject(Subject subject);

    List<Lecture> findByTimeslot(Timeslot timeslot);
}
