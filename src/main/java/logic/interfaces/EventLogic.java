package logic.interfaces;

import dao.exception.DataStorageException;
import entities.Employee;
import entities.Event;
import logic.exception.EventNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventLogic {

    int countOfBusinessTrips(Employee employee, int year) throws DataStorageException;
    List<Event> getAllAbsencesEmployeeEvents(Employee employee, int year) throws DataStorageException, EventNotFoundException;//все отсуствия (без переработок и командировок в выходных)
    List<Event> getAllAbsencesEmployeeEventsForPeriod(Employee employee, LocalDate dateFrom, LocalDate dateTo);//все отсуствия (без переработок и командировок в выходных)
    Optional <Event> getEmployeeEventAtDate(Employee employee, LocalDate date) throws DataStorageException;

    List<Event> getAllEvents();
    List<Event> getAllEmployeeEvents(Employee employee, int year);
    List<Event> getAllActiveEvents(Employee employee, int year) throws DataStorageException, EventNotFoundException;
    boolean isDateFree(Employee employee,LocalDate date) throws DataStorageException;// проверять только ACTIVE
}
