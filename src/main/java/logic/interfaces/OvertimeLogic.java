package logic.interfaces;

import dao.exception.DataStorageException;
import entities.Absence;
import entities.BusinessTripWeekEnd;
import entities.Employee;
import entities.Overtime;
import logic.exception.DateIsBusyException;
import logic.exception.ReasonAlreadyExistException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OvertimeLogic {

    //enter
    void createOvertimeWithoutAbsence(Employee employee, LocalDate dateOfOvertime) throws DateIsBusyException, DataStorageException;
    void createOvertimeWithAbsence(Employee employee, LocalDate dateOfOvertime, LocalDate dateOfAbsence) throws DateIsBusyException, DataStorageException;
    void addAbsence(Employee employee, Overtime overtime, Absence absence) throws ReasonAlreadyExistException, DataStorageException;
    void modifyOvertimeComment(Overtime overtime) throws DataStorageException;
    void removeAbsenceFromOvertime(Overtime overtime) throws DataStorageException;

    //get
    Absence getAbsenceForOvertime(Overtime overtime);
    List<Overtime> getAllActiveOvertimes(Employee employee, int year) throws DataStorageException;
    List<Overtime> getAllOvertimesWithAbsences(Employee employee, int year) throws DataStorageException;
    List<Overtime> getAllOvertimesWithoutAbsences(Employee employee, int year) throws DataStorageException;
    int getCountOfOvertimeDays (Employee employee, int year) throws DataStorageException;
    int getCountOfOvertimesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    List<Overtime> getAllActiveOvertimesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;

    //remove
    void cancelOvertime(Overtime overtime) throws DataStorageException;

}
