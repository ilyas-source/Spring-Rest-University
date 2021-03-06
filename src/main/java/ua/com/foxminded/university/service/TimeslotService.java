package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.UniversityProperties;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.TimeslotRepository;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.TimeslotInUseException;
import ua.com.foxminded.university.exception.TimeslotTooShortException;
import ua.com.foxminded.university.exception.TimeslotsIntersectionException;
import ua.com.foxminded.university.model.Timeslot;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TimeslotService {

    private static final Logger logger = LoggerFactory.getLogger(TimeslotService.class);

    private TimeslotRepository timeslotRepository;
    private LectureRepository lectureRepository;
    private UniversityProperties universityProperties;

    public TimeslotService(TimeslotRepository timeslotRepository, LectureRepository lectureRepository, UniversityProperties universityProperties) {
        this.timeslotRepository = timeslotRepository;
        this.lectureRepository = lectureRepository;
        this.universityProperties = universityProperties;
    }

    public void create(Timeslot timeslot) {
        logger.debug("Creating a new timeslot: {} ", timeslot);
        verifyHasNoIntersections(timeslot);
        verifyIsLongEnough(timeslot);
        timeslotRepository.save(timeslot);
    }

    public List<Timeslot> findAll() {
        return timeslotRepository.findAll();
    }

    public Optional<Timeslot> findById(int id) {
        return timeslotRepository.findById(id);
    }

    public Timeslot getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find timeslot by id " + id));
    }

    public void update(Timeslot timeslot) {
        logger.debug("Updating timeslot: {} ", timeslot);
        verifyHasNoIntersections(timeslot);
        verifyIsLongEnough(timeslot);
        timeslotRepository.save(timeslot);
    }

    public void delete(int id) {
        logger.debug("Deleting timeslot by id: {} ", id);
        Timeslot timeslot = getById(id);
        verifyHasNoLecturesScheduled(timeslot);
        timeslotRepository.delete(timeslot);
    }

    private void verifyHasNoIntersections(Timeslot timeslot) {
        if (timeslotRepository.findByBeginTimeAndEndTime(
                timeslot.getEndTime(), timeslot.getEndTime()).isPresent()) {
            return;
        }
        int minimumBreakLength = universityProperties.getMinimumBreakLength();
        var timeslotWithBreaks = new Timeslot(timeslot.getBeginTime().minusMinutes(minimumBreakLength),
                timeslot.getEndTime().plusMinutes(minimumBreakLength));
        if (timeslotRepository.countByEndTimeIsGreaterThanEqualAndBeginTimeIsLessThanEqual(
                timeslotWithBreaks.getBeginTime(), timeslotWithBreaks.getEndTime()) > 0) {
            throw new TimeslotsIntersectionException(
                    "New timeslot has intersections with existing timetable, can't create/update");
        }
    }

    private void verifyIsLongEnough(Timeslot timeslot) {
        long duration = (Duration.between(timeslot.getBeginTime(), timeslot.getEndTime()).getSeconds()) / 60;
        if (duration < universityProperties.getMinimumTimeslotLength()) {
            throw new TimeslotTooShortException(String.format(
                    "Minimum timeslot length %s min, but was %s min, can't create timeslot",
                    universityProperties.getMinimumTimeslotLength(), duration));
        }
    }

    private void verifyHasNoLecturesScheduled(Timeslot timeslot) {
        if (!lectureRepository.findByTimeslot(timeslot).isEmpty()) {
            throw new TimeslotInUseException("Timeslot has sheduled lectures, can't delete");
        }
    }
}
