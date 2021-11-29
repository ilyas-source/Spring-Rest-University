package ua.com.foxminded.university;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ua.com.foxminded.university.model.Degree;

import java.util.Map;

@ConfigurationProperties(prefix = "university")
public class UniversityProperties {

    private int maxStudents;
    private Map<Degree, Integer> vacationDays;
    private Map<String, String> defaultSort;
    private int minimumTimeslotLength;
    private int minimumBreakLength;
    private String nameIsEmptyMessage;
    private String lastNameIsEmptyMessage;
    private String capacityNegativeMessage;
    private String wrongEmailMessage;
    private String wrongLengthMessage;
    private String groupNotAssignedMessage;
    private String timeslotNotAssignedMessage;
    private String teacherNotAssignedMessage;
    private String subjectNotAssignedMessage;
    private String floorNegativeMessage;
    private String roomNumberNegativeMessage;

    public String getNameIsEmptyMessage() {
        return nameIsEmptyMessage;
    }

    public void setNameIsEmptyMessage(String nameIsEmptyMessage) {
        this.nameIsEmptyMessage = nameIsEmptyMessage;
    }

    public String getLastNameIsEmptyMessage() {
        return lastNameIsEmptyMessage;
    }

    public void setLastNameIsEmptyMessage(String lastNameIsEmptyMessage) {
        this.lastNameIsEmptyMessage = lastNameIsEmptyMessage;
    }

    public String getCapacityNegativeMessage() {
        return capacityNegativeMessage;
    }

    public void setCapacityNegativeMessage(String capacityNegativeMessage) {
        this.capacityNegativeMessage = capacityNegativeMessage;
    }

    public String getWrongEmailMessage() {
        return wrongEmailMessage;
    }

    public void setWrongEmailMessage(String wrongEmailMessage) {
        this.wrongEmailMessage = wrongEmailMessage;
    }

    public String getWrongLengthMessage() {
        return wrongLengthMessage;
    }

    public void setWrongLengthMessage(String wrongLengthMessage) {
        this.wrongLengthMessage = wrongLengthMessage;
    }

    public String getGroupNotAssignedMessage() {
        return groupNotAssignedMessage;
    }

    public void setGroupNotAssignedMessage(String groupNotAssignedMessage) {
        this.groupNotAssignedMessage = groupNotAssignedMessage;
    }

    public String getTimeslotNotAssignedMessage() {
        return timeslotNotAssignedMessage;
    }

    public void setTimeslotNotAssignedMessage(String timeslotNotAssignedMessage) {
        this.timeslotNotAssignedMessage = timeslotNotAssignedMessage;
    }

    public String getTeacherNotAssignedMessage() {
        return teacherNotAssignedMessage;
    }

    public void setTeacherNotAssignedMessage(String teacherNotAssignedMessage) {
        this.teacherNotAssignedMessage = teacherNotAssignedMessage;
    }

    public String getSubjectNotAssignedMessage() {
        return subjectNotAssignedMessage;
    }

    public void setSubjectNotAssignedMessage(String subjectNotAssignedMessage) {
        this.subjectNotAssignedMessage = subjectNotAssignedMessage;
    }

    public String getFloorNegativeMessage() {
        return floorNegativeMessage;
    }

    public void setFloorNegativeMessage(String floorNegativeMessage) {
        this.floorNegativeMessage = floorNegativeMessage;
    }

    public String getRoomNumberNegativeMessage() {
        return roomNumberNegativeMessage;
    }

    public void setRoomNumberNegativeMessage(String roomNumberNegativeMessage) {
        this.roomNumberNegativeMessage = roomNumberNegativeMessage;
    }

    public Map<String, String> getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(Map<String, String> defaultSort) {
        this.defaultSort = defaultSort;
    }

    public int getMinimumTimeslotLength() {
        return minimumTimeslotLength;
    }

    public void setMinimumTimeslotLength(int minimumTimeslotLength) {
        this.minimumTimeslotLength = minimumTimeslotLength;
    }

    public int getMinimumBreakLength() {
        return minimumBreakLength;
    }

    public void setMinimumBreakLength(int minimumBreakLength) {
        this.minimumBreakLength = minimumBreakLength;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Map<Degree, Integer> getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(Map<Degree, Integer> vacationDays) {
        this.vacationDays = vacationDays;
    }
}
