package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teachers")
@NamedQueries({
        @NamedQuery(name = "SelectAllTeachers",
                query = "from Teacher order by lastName")
})
public class Teacher {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private Gender gender;
    @Column
    private Degree degree;
    @ManyToMany
    @JoinTable(
            name="teachers_subjects",
            joinColumns = @JoinColumn(name="teacher_id"),
            inverseJoinColumns = @JoinColumn(name="subject_id"))
    private List<Subject> subjects;
    @Column
    private String email;
    @Column
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Address address;
    @OneToMany
    private List<Vacation> vacations;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id = 0;
        private String firstName;
        private String lastName;
        private Gender gender;
        private Degree degree;
        private List<Subject> subjects;
        private String email;
        private String phoneNumber;
        private Address address;
        private List<Vacation> vacations;

        public Builder() {
        }

        public Builder firstName(String val) {
            this.firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            this.lastName = val;
            return this;
        }

        public Builder id(int val) {
            this.id = val;
            return this;
        }

        public Builder gender(Gender val) {
            this.gender = val;
            return this;
        }

        public Builder degree(Degree val) {
            this.degree = val;
            return this;
        }

        public Builder subjects(List<Subject> val) {
            this.subjects = val;
            return this;
        }

        public Builder email(String val) {
            this.email = val;
            return this;
        }

        public Builder phoneNumber(String val) {
            this.phoneNumber = val;
            return this;
        }

        public Builder vacations(List<Vacation> val) {
            this.vacations = val;
            return this;
        }

        public Builder address(Address val) {
            this.address = val;
            return this;
        }

        public Teacher build() {
            return new Teacher(this);
        }
    }

    private Teacher(Builder builder) {
        id = builder.id;
        firstName = builder.firstName;
        lastName = builder.lastName;
        gender = builder.gender;
        degree = builder.degree;
        subjects = builder.subjects;
        email = builder.email;
        phone = builder.phoneNumber;
        address = builder.address;
        vacations = builder.vacations;
    }

    public Teacher() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((degree == null) ? 0 : degree.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + id;
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        result = prime * result + ((subjects == null) ? 0 : subjects.hashCode());
        result = prime * result + ((vacations == null) ? 0 : vacations.hashCode());
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
        Teacher other = (Teacher) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (degree != other.degree)
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (gender != other.gender)
            return false;
        if (id != other.id)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (phone == null) {
            if (other.phone != null)
                return false;
        } else if (!phone.equals(other.phone))
            return false;
        if (subjects == null) {
            if (other.subjects != null)
                return false;
        } else if (!subjects.equals(other.subjects))
            return false;
        if (vacations == null) {
            if (other.vacations != null)
                return false;
        } else if (!vacations.equals(other.vacations))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Teacher [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", degree="
                + degree + ", subjects=" + subjects + ", email=" + email + ", phone=" + phone + ", address=" + address
                + ", vacations=" + vacations + "]";
    }
}
