package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Timeslot;
import ua.com.foxminded.university.service.TimeslotService;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Main.CR;

@Component
public class TimeslotsMenu {

    TimeslotService timeslotService;

    public TimeslotsMenu(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
    }

    public String getStringOfTimeslots(List<Timeslot> timeslots) {
        StringBuilder result = new StringBuilder();
        timeslots.sort(Comparator.comparing(Timeslot::getId));

        for (Timeslot timeslot : timeslots) {
            result.append(getStringFromTimeslot(timeslot) + CR);
        }
        return result.toString();
    }

    public String getStringFromTimeslot(Timeslot timeslot) {
        return timeslot.getId() + ". " + timeslot.getBeginTime() + "-" + timeslot.getEndTime();
    }
}

