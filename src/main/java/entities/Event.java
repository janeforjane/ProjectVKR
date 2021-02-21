package entities;

import box.EventTag;
import box.StatusEvent;

import java.time.LocalDate;

public class Event {

    private Employee employee;
    private LocalDate dateOfEvent;
    private StatusEvent statusEvent;
    private String comment;


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

}
