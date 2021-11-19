package ua.com.foxminded.university;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ua.com.foxminded.university.model.Degree;

import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "university")
public class UniversityProperties {

    private String defaultSortAttribute;
    private String defaultSortDirection;
    private Integer maxStudents;
    private Map<Degree, Integer> vacationDays;
    private Map<String, Integer> timeslot;

    public String getDefaultSortAttribute() {
        return defaultSortAttribute;
    }

    public void setDefaultSortAttribute(String defaultSortAttribute) {
        this.defaultSortAttribute = defaultSortAttribute;
    }

    public String getDefaultSortDirection() {
        return defaultSortDirection;
    }

    public void setDefaultSortDirection(String defaultSortDirection) {
        this.defaultSortDirection = defaultSortDirection;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Map<Degree, Integer> getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(Map<Degree, Integer> vacationDays) {
        this.vacationDays = vacationDays;
    }

    public Map<String, Integer> getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Map<String, Integer> timeslot) {
        this.timeslot = timeslot;
    }
}
