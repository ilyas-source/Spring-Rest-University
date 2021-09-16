package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.SubjectService;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Main.CR;

@Component
public class SubjectsMenu {

    SubjectService subjectService;

    public SubjectsMenu(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public String getStringOfSubjects(List<Subject> subjects) {
        StringBuilder result = new StringBuilder();
        subjects.sort(Comparator.comparing(Subject::getId));

        for (Subject subject : subjects) {
            result.append(getStringFromSubject(subject) + CR);
        }
        return result.toString();
    }

    public String getStringFromSubject(Subject subject) {
        return subject.getId() + ". " + subject.getName() + ": " + subject.getDescription();
    }
}
