package entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees", schema = "public")
public class Employee {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "name")
    private String name;
    @Column(name = "second_name")
    private String secondName;
    @Column(name = "entry_date")
    private LocalDate entryDate;
    @Column(name = "cancel_date")
    private LocalDate cancelDate;
    @Column(name = "position")
    private String position;
    @Column(name = "comment_field")
    private String comment;
    @Column(name = "date_time_of_create", insertable = false, updatable = false)
    private LocalDateTime dateTimeOfCreate; //

    public Employee() {
    }

    public Employee(String lastName, String name, String secondName, LocalDate entryDate, String position) {

        this.lastName = lastName;
        this.name = name;
        this.secondName = secondName;
        this.entryDate = entryDate;
        this.position = position;
    }

    public void setDateTimeOfCreate(LocalDateTime dateTimeOfCreate) {
        this.dateTimeOfCreate = dateTimeOfCreate;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDateTime getDateTimeOfCreate() {
        return dateTimeOfCreate;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (getID() != employee.getID()) return false;
        if (!getLastName().equals(employee.getLastName())) return false;
        if (!getName().equals(employee.getName())) return false;
        if (!getSecondName().equals(employee.getSecondName())) return false;
        if (!getEntryDate().equals(employee.getEntryDate())) return false;
        if (getCancelDate() != null ? !getCancelDate().equals(employee.getCancelDate()) : employee.getCancelDate() != null)
            return false;
        if (!getPosition().equals(employee.getPosition())) return false;
        return getComment() != null ? getComment().equals(employee.getComment()) : employee.getComment() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getID() ^ (getID() >>> 32));
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getSecondName().hashCode();
        result = 31 * result + getEntryDate().hashCode();
        result = 31 * result + (getCancelDate() != null ? getCancelDate().hashCode() : 0);
        result = 31 * result + getPosition().hashCode();
        result = 31 * result + (getComment() != null ? getComment().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "ID=" + ID +
                ", lastName='" + lastName + '\'' +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", entryDate=" + entryDate +
                ", cancelDate=" + cancelDate +
                ", position='" + position + '\'' +
                ", comment='" + comment + '\'' +
                ", dateTimeOfCreate=" + dateTimeOfCreate +
                '}';
    }
}
