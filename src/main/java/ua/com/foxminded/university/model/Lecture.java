package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "lectures")
@NamedQueries({
        @NamedQuery(name = "SelectAllLectures", query = "from Lecture order by date"),
        @NamedQuery(name = "FindLecturesBySubject", query = "from Lecture where subject = :subject"),
        @NamedQuery(name = "FindLecturesByClassroom", query = "from Lecture where classroom = :classroom"),
        @NamedQuery(name = "FindLecturesByTimeslot", query = "from Lecture where timeslot = :timeslot"),
        @NamedQuery(name = "FindLecturesByTeacher", query = "from Lecture where teacher = :teacher"),
        @NamedQuery(name = "FindLecturesByDateTime",
                query = "from Lecture where date = :date and timeslot = :timeslot"),
        @NamedQuery(name = "FindLectureByDateTimeClassroom",
                query = "from Lecture where date = :date and timeslot = :timeslot and classroom = :classroom"),
        @NamedQuery(name = "FindLectureByDateTimeTeacher",
                query = "from Lecture where date = :date and timeslot = :timeslot and teacher = :teacher"),
        @NamedQuery(name = "FindLecturesByTeacherAndPeriod",
                query = "from Lecture where date >= :startDate and date <= :endDate and teacher = :teacher")
    //    @NamedQuery(name = "FindLecturesByStudentAndPeriod",
    //            query = "from Lecture where date >= :startDate and date <= :endDate and group = :group"),

})
public class Lecture {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private LocalDate date;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Timeslot timeslot;
    @ManyToMany
    @JoinTable(
            name = "lectures_groups",
            joinColumns = @JoinColumn(name = "lecture_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Subject subject;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Teacher teacher;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Classroom classroom;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id = 0;
        private LocalDate date;
        private Subject subject;
        private Timeslot timeslot;
        private List<Group> groups;
        private Teacher teacher;
        private Classroom classroom;

        private Builder() {
        }

        public Builder date(LocalDate val) {
            this.date = val;
            return this;
        }

        public Builder subject(Subject val) {
            this.subject = val;
            return this;
        }

        public Builder id(int val) {
            this.id = val;
            return this;
        }

        public Builder timeslot(Timeslot val) {
            this.timeslot = val;
            return this;
        }

        public Builder groups(List<Group> val) {
            this.groups = val;
            return this;
        }

        public Builder teacher(Teacher val) {
            this.teacher = val;
            return this;
        }

        public Builder classroom(Classroom val) {
            this.classroom = val;
            return this;
        }

        public Lecture build() {
            return new Lecture(this);
        }
    }

    public Lecture() {
    }

    public Lecture(Builder builder) {
        id = builder.id;
        date = builder.date;
        timeslot = builder.timeslot;
        groups = builder.groups;
        subject = builder.subject;
        teacher = builder.teacher;
        classroom = builder.classroom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeSlot) {
        this.timeslot = timeSlot;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classroom == null) ? 0 : classroom.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((groups == null) ? 0 : groups.hashCode());
        result = prime * result + id;
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
        result = prime * result + ((timeslot == null) ? 0 : timeslot.hashCode());
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
        Lecture other = (Lecture) obj;
        if (classroom == null) {
            if (other.classroom != null)
                return false;
        } else if (!classroom.equals(other.classroom))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (groups == null) {
            if (other.groups != null)
                return false;
        } else if (!groups.equals(other.groups))
            return false;
        if (id != other.id)
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        if (timeslot == null) {
            if (other.timeslot != null)
                return false;
        } else if (!timeslot.equals(other.timeslot))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Lecture [id=" + id + ", date=" + date + ", timeslot=" + timeslot + ", groups=" + groups + ", subject=" + subject
                + ", teacher=" + teacher + ", classroom=" + classroom + "]";
    }
}
