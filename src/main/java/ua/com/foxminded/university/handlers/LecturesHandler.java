package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.util.List;

import ua.com.foxminded.university.model.Lecture;

public class LecturesHandler {

    public static String getStringOfLectures(List<Lecture> lectures) {
	StringBuilder result = new StringBuilder();
	for (Lecture lecture : lectures) {
	    result.append(lectures.indexOf(lecture) + ". " + lecture);
	}
	return result.toString();
    }

}
