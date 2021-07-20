package ua.com.foxminded.university.exception;

public class TeacherIsCannotTeachSubject extends RuntimeException {

    public TeacherIsCannotTeachSubject(String message) {
	super(message);
    }
}
