package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.service.LectureService;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Main.CR;

@Component
public class LecturesMenu {

    private GroupsMenu groupsMenu;
    private LectureService lectureService;
    private SubjectsMenu subjectsMenu;
    private TeachersMenu teachersMenu;
    private ClassroomsMenu classroomsMenu;
    private TimeslotsMenu timeslotsMenu;

    public LecturesMenu(GroupsMenu groupsMenu, LectureService lectureService, SubjectsMenu subjectsMenu,
                        TeachersMenu teachersMenu, ClassroomsMenu classroomsMenu, TimeslotsMenu timeslotsMenu) {
        this.groupsMenu = groupsMenu;
        this.lectureService = lectureService;
        this.subjectsMenu = subjectsMenu;
        this.teachersMenu = teachersMenu;
        this.classroomsMenu = classroomsMenu;
        this.timeslotsMenu = timeslotsMenu;
    }

    public String getStringOfLectures(List<Lecture> lectures) {
        var result = new StringBuilder();
        lectures.sort(Comparator.comparing(Lecture::getId));
        for (Lecture lecture : lectures) {
            result.append(lecture.getId()).append(". " + getStringFromLecture(lecture));
        }
        return result.toString();
    }

    public String getStringFromLecture(Lecture lecture) {
        var result = new StringBuilder();
        var subject = lecture.getSubject();
        var teacher = lecture.getTeacher();
        var classroom = lecture.getClassroom();

        result.append(
                "Lecture on (" + subject.getId() + ")" + subject.getName() + " will take place on " + lecture.getDate()
                        + ", from "
                        + lecture.getTimeslot().getBeginTime() + " to " + lecture.getTimeslot().getEndTime() + " (timeslot #"
                        + lecture.getTimeslot().getId() + ")" + CR);
        result.append("Read by (" + teacher.getId() + ")" + teacher.getFirstName() + " " + teacher.getLastName() + " in ("
                + classroom.getId() + ")" + classroom.getName() + "." + CR);

        List<Group> groups = lecture.getGroups();

        result.append("Groups to attend:" + CR + groupsMenu.getStringOfGroups(groups));
        return result.toString();
    }
}
