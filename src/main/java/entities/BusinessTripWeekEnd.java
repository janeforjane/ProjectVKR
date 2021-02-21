package entities;

import box.StatusEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "business_trip_weekends", schema = "public")
public class BusinessTripWeekEnd extends CommonOvertime {

    @Column(name = "ID")
    @Id
    private long ID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "date_of_event")
    private LocalDate dateOfEvent;

    @Column(name = "status_event")
    @Enumerated(EnumType.STRING)
    private StatusEvent statusEvent;

    @OneToOne(mappedBy = "reasonsOfAbsenceBusinessTrip")
    private Absence absenceForOvertime;

    @Column(name = "comment_field")
    private String comment;
//    private int year;

    @Column(name = "date_time_of_create", insertable = false, updatable = false)
    private LocalDateTime dateTimeOfCreate;


    public BusinessTripWeekEnd() {
    }

    public BusinessTripWeekEnd(Employee employee, LocalDate dateOfEvent) {
        this.employee = employee;
        this.dateOfEvent = dateOfEvent;
    }


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Employee getEmployee() {
        return employee;
    }


    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public LocalDate getDateOfEvent() {
        return dateOfEvent;
    }


    public void setDateOfEvent(LocalDate dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }


    public StatusEvent getStatusEvent() {
        return statusEvent;
    }


    public void setStatusEvent(StatusEvent statusEvent) {
        this.statusEvent = statusEvent;
    }

    public Absence getAbsenceForOvertime() {
        return absenceForOvertime;
    }

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
        return "BusinessTripWeekEnd{" +
                "ID=" + ID +
                ", employee=" + employee +
                ", dateOfEvent=" + dateOfEvent +
                ", statusEvent=" + statusEvent +
                ", absenceForOvertime=" + absenceForOvertime +
                ", comment='" + comment + '\'' +
                ", dateTimeOfCreate=" + dateTimeOfCreate +
                '}';
    }
}
