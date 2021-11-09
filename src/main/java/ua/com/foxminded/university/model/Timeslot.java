package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "timeslots")
@NamedQueries({
        @NamedQuery(name = "SelectAllTimeslots", query = "from Timeslot"),
        @NamedQuery(name = "FindTimeslotByBothTimes", query = "from Timeslot where beginTime = : beginTime and endTime = :endTime")
})
public class Timeslot {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "begin_time")
    private LocalTime beginTime;
    @Column(name = "end_time")
    private LocalTime endTime;

    public Timeslot() {
    }

    public Timeslot(LocalTime beginTime, LocalTime endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public Timeslot(int id, LocalTime beginTime, LocalTime endTime) {
        this.id = id;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean intersects(Timeslot timeslot, int breakLength) {
        return timeslot.getBeginTime().isAfter(this.endTime.plusMinutes(breakLength)) ||
                timeslot.getEndTime().isBefore(this.beginTime.minusMinutes(breakLength));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + id;
        result = prime * result + ((beginTime == null) ? 0 : beginTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Timeslot other = (Timeslot) obj;
        if (endTime == null) {
            if (other.endTime != null)
                return false;
        } else if (!endTime.equals(other.endTime))
            return false;
        if (id != other.id)
            return false;
        if (beginTime == null) {
            if (other.beginTime != null)
                return false;
        } else if (!beginTime.equals(other.beginTime))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Timeslot [id=" + id + ", beginTime=" + beginTime + ", endTime=" + endTime + "]";
    }
}
