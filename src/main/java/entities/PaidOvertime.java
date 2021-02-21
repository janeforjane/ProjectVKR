package entities;

import box.StatusEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paid_overtimes", schema = "public")
public class PaidOvertime extends Event {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "date_of_event")
    private LocalDate dateOfEvent;

    @Column(name = "status_event")
    @Enumerated(EnumType.STRING)
    private StatusEvent statusEvent;
    @Column(name = "comment_field")
    private String comment;
//    private int year;
    @Column(name = "date_time_of_create", insertable = false, updatable = false)
    private LocalDateTime dateTimeOfCreate;


    public PaidOvertime(Employee employee, LocalDate dateOfEvent) {
        this.employee = employee;
        this.dateOfEvent = dateOfEvent;
    }

    public PaidOvertime() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "PaidOvertime{" +
                "ID=" + ID +
                ", employee=" + employee +
                ", dateOfEvent=" + dateOfEvent +
                ", statusEvent=" + statusEvent +
                ", comment='" + comment + '\'' +
                ", dateTimeOfCreate=" + dateTimeOfCreate +
                '}';
    }
}
