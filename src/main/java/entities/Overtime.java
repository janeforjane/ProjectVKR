package entities;

import box.StatusEvent;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "overtimes", schema = "public")
public class Overtime extends CommonOvertime{

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "date_of_event")
    private LocalDate dateOfEvent;

    @Column(name = "status_event")
    @Enumerated(EnumType.STRING)
    private StatusEvent statusEvent;

    @Column(name = "comment_field")
    private String comment;

    @OneToOne(mappedBy = "reasonsOfAbsenceOvertime")
    private Absence absenceForOvertime;


    @Column(name = "date_time_of_create", insertable = false, updatable = false)
    private LocalDateTime dateTimeOfCreate;

    public Overtime() {
    }

    public Overtime(Employee employee, LocalDate dateOfEvent) {
        this.employee = employee;
        this.dateOfEvent = dateOfEvent;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @Override
    public Employee getEmployee() {
        return employee;
    }

    @Override
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public LocalDate getDateOfEvent() {
        return dateOfEvent;
    }

    @Override
    public void setDateOfEvent(LocalDate dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    @Override
    public StatusEvent getStatusEvent() {
        return statusEvent;
    }

    @Override
    public void setStatusEvent(StatusEvent statusEvent) {
        this.statusEvent = statusEvent;
    }

    @Override
    public Absence getAbsenceForOvertime() {
        return absenceForOvertime;
    }

    @Override
    public void setAbsenceForOvertime(Absence absenceForOvertime) {
        this.absenceForOvertime = absenceForOvertime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public String toString() {
        return "Overtime{" +
                "ID=" + ID +
                ", employee=" + employee +
                ", dateOfEvent=" + dateOfEvent +
                ", statusEvent=" + statusEvent +
                ", comment='" + comment + '\'' +
                ", absenceForOvertime=" + absenceForOvertime +
                ", dateTimeOfCreate=" + dateTimeOfCreate +
                '}';
    }
}
