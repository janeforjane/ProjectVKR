package entities;

import box.EventTag;
import box.StatusEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vacations", schema = "public")
public class Vacation extends Event{

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(name = "date_of_event")
    private LocalDate dateOfEvent;

    @Column(name = "status_event")
    @Enumerated(EnumType.STRING)
    private StatusEvent statusEvent;

    @Column(name = "event_tag")
    @Enumerated(EnumType.STRING)
    private EventTag eventTag;

    @Column(name = "comment_field")
    private String comment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_replace_id")
    private Vacation replaceDays;
    @Column(name = "year_of_event")
    private Integer yearOfEvent;
    @Column(name = "date_time_of_create", insertable = false, updatable = false)
    private LocalDateTime dateTimeOfCreate;


    public Vacation(Employee employee, int yearOfEvent) {
        this.employee = employee;
        this.yearOfEvent = yearOfEvent;
    }

    public Vacation(Employee employee, LocalDate dateOfEvent) {
        this.employee = employee;
        this.dateOfEvent = dateOfEvent;
        this.yearOfEvent = dateOfEvent.getYear();
    }

    public Vacation() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
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

    public EventTag getEventTag() {
        return eventTag;
    }

    public void setEventTag(EventTag eventTag) {
        this.eventTag = eventTag;
    }

//    public List<Vacation> getReplaceDays() {
//        return replaceDays;
//    }
//
//    public void setReplaceDays(List<Vacation> replaceDays) {
//        this.replaceDays = replaceDays;
//    }


    public Vacation getReplaceDays() {
        return replaceDays;
    }

    public void setReplaceDays(Vacation replaceDays) {
        this.replaceDays = replaceDays;
    }

    public Integer getYearOfEvent() {
        return yearOfEvent;
    }

    public void setYearOfEvent(Integer yearOfEvent) {
        this.yearOfEvent = yearOfEvent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Vacation{" +
                "ID=" + ID +
                ", employee=" + employee +
                ", dateOfEvent=" + dateOfEvent +
                ", statusEvent=" + statusEvent +
                ", eventTag=" + eventTag +
                ", comment='" + comment + '\'' +
                ", replaceDays=" + replaceDays +
                ", yearOfEvent=" + yearOfEvent +
                ", dateTimeOfCreate=" + dateTimeOfCreate +
                '}';
    }


    // 1
    // dateOfEvent = 01.04
    // statusEvent = NOTACTIVE
    // eventTag = PLAN
    // replaceDays = 2

    // 11
    // dateOfEvent = 21.09
    // statusEvent = ACTIVE
    // eventTag = PLAN
    // replaceDays = 2


    // 2
    // dateOfEvent = 03.05
    // statusEvent = NOTACTIVE
    // eventTag = REPLACE
    // replaceDays = 3

    // 3
    // dateOfEvent = 15.08
    // statusEvent = ACTIVE
    // eventTag = REPLACE
    // replaceDays = -
}
