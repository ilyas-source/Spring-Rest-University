package ua.com.foxminded.university.dao;

import java.util.List;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Lecture;

public interface LectureDao extends GeneralDao<Lecture> {

    public List<Lecture> findByClassroom(Classroom classroom);

    int countStudentsInLecture(Lecture lecture);
}
