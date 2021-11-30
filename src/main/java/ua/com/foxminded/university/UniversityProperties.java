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
