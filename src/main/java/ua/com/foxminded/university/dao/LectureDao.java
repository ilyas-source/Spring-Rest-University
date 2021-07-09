package ua.com.foxminded.university.dao;

import java.util.List;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Teacher;

public interface LectureDao extends GeneralDao<Lecture> {

    List<Lecture> findByClassroom(Classroom classroom);

    int countStudentsInLecture(Lecture lecture);

    List<Lecture> findByTeacher(Teacher teacher);
}
