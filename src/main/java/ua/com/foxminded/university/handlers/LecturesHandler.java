package ua.com.foxminded.university.handlers;

import java.util.List;

import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.University;

public class LecturesHandler {

    public static String getStringOfLectures(List<Lecture> lectures) {
	StringBuilder result = new StringBuilder();
	for (Lecture lecture : lectures) {
	    result.append(lectures.indexOf(lecture) + 1).append(". " + lecture);
	}
	return result.toString();
    }

    public static Lecture getLectureFromScanner(University university) {
	// TODO Auto-generated method stub
	return null;
    }

    public static void updateALecture(University university) {
	// TODO Auto-generated method stub

    }

    public static void deleteALecture(University university) {
	// TODO Auto-generated method stub

    }
}
