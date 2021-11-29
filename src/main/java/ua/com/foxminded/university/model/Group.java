package ua.com.foxminded.university.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 2, max = 12, message = "Name should be between 2 and 12 characters")
    @NotEmpty(message = "Name should not be empty")
    @Column
    private String name;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Group other = (Group) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return id + ":" + name;
    }
}
