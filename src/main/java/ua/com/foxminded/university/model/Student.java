package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
@NamedQueries({
        @NamedQuery(name = "SelectAllStudents", query = "from Student"),
        @NamedQuery(name = "FindStudentByGroup", query = "from Student where group = :group"),
        @NamedQuery(name = "FindStudentByAddress", query = "from Student where address = :address"),
        @NamedQuery(name = "findStudentByNameAndBirthDate",
                query = "from Student where firstName = :firstName AND lastName = :lastName AND birthDate = :birthDate")


})
public class Student {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name="birth_date")
    private LocalDate birthDate;
    @Column
    private String email;
    @Column
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Address address;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Group group;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id = 0;
        private String firstName;
        private String lastName;
        private Gender gender;
        private LocalDate birthDate;
        private String email;
        private String phone;
        private Address address;
        private Group group;

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

        public Builder birthDate(LocalDate val) {
            this.birthDate = val;
            return this;
        }

        public Builder email(String val) {
            this.email = val;
            return this;
        }

        public Builder phone(String val) {
            this.phone = val;
            return this;
        }

        public Builder address(Address val) {
            this.address = val;
            return this;
        }

        public Builder group(Group val) {
            this.group = val;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

    public Student() {
    }

    private Student(Builder builder) {
        id = builder.id;
        firstName = builder.firstName;
        lastName = builder.lastName;
        gender = builder.gender;
        birthDate = builder.birthDate;
        email = builder.email;
        phone = builder.phone;
        address = builder.address;
        group = builder.group;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + id;
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
        Student other = (Student) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (birthDate == null) {
            if (other.birthDate != null)
                return false;
        } else if (!birthDate.equals(other.birthDate))
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
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
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
        return true;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", " + firstName + ", " + lastName + ", " + gender + ", "
                + birthDate + ", " + email + ", " + phone + ", " + address + ", " + group + "]";
    }
}
