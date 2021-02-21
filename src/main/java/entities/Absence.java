package entities;

import box.StatusEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "absences", schema = "public")
public class Absence extends Event{

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

    @Column(name = "comment_field")
    private String comment;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="overtime_id")
    private Overtime reasonsOfAbsenceOvertime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="business_trip_weekend_id")
    private BusinessTripWeekEnd reasonsOfAbsenceBusinessTrip;

    @Column(name = "date_time_of_create", insertable = false, updatable = false)
    private LocalDateTime dateTimeOfCreate;

    public Absence() {
    }

    public Absence(Employee employee, LocalDate dateOfEvent) {
        this.employee = employee;
        this.dateOfEvent = dateOfEvent;
    }


    public BusinessTripWeekEnd getReasonsOfAbsenceBusinessTrip() {
        return reasonsOfAbsenceBusinessTrip;
    }

    public void setReasonsOfAbsenceBusinessTrip(BusinessTripWeekEnd reasonsOfAbsenceBusinessTrip) {
        this.reasonsOfAbsenceBusinessTrip = reasonsOfAbsenceBusinessTrip;
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

    public Overtime getReasonsOfAbsenceOvertime() {
        return reasonsOfAbsenceOvertime;
    }

    public void setReasonsOfAbsenceOvertime(Overtime reasonsOfAbsence) {
        this.reasonsOfAbsenceOvertime = reasonsOfAbsence;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public String toString() {
        return "Absence{" +
                "ID=" + ID +
                ", employee=" + employee +
                ", dateOfEvent=" + dateOfEvent +
                ", statusEvent=" + statusEvent +
                ", comment='" + comment + '\'' +
                ", reasonsOfAbsenceOvertime=" + reasonsOfAbsenceOvertime +
                ", reasonsOfAbsenceBusinessTrip=" + reasonsOfAbsenceBusinessTrip +
                ", dateTimeOfCreate=" + dateTimeOfCreate +
                '}';
    }
}
