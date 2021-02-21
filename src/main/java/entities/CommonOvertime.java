package entities;

import box.StatusEvent;

import java.time.LocalDate;

public class CommonOvertime extends Event {

    private Employee employee;
    private LocalDate dateOfEvent;
    private StatusEvent statusEvent;
    private Absence absenceForOvertime;

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

    public Absence getAbsenceForOvertime() {
        return absenceForOvertime;
    }

    public void setAbsenceForOvertime(Absence absenceForOvertime) {
        this.absenceForOvertime = absenceForOvertime;
    }
}
