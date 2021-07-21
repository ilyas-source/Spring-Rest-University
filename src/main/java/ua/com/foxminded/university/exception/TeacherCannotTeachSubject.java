package ua.com.foxminded.university.exception;

public class TeacherCannotTeachSubject extends RuntimeException {

    public TeacherCannotTeachSubject(String message) {
	super(message);
    }
}
