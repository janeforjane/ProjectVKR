package logic.interfaces;

import dao.exception.DataStorageException;
import entities.Absence;
import entities.Employee;
import entities.Overtime;
import logic.exception.DateIsBusyException;

import java.time.LocalDate;
import java.util.List;

public interface OvertimeLogic {

    //enter
    void createOvertime(Employee employee, LocalDate dateOfOvertime) throws DateIsBusyException, DataStorageException;
    void modifyOvertimeComment(Overtime overtime) throws DataStorageException;
    void removeAbsenceFromOvertime(Overtime overtime) throws DataStorageException;

    //get
    Absence getAbsenceForOvertime(Overtime overtime);
    List<Overtime> getAllActiveOvertimes(Employee employee, int year) throws DataStorageException;
    List<Overtime> getAllOvertimesWithAbsences(Employee employee, int year) throws DataStorageException;
    List<Overtime> getAllOvertimesWithoutAbsences(Employee employee, int year) throws DataStorageException;
    int getCountOfOvertimeDays (Employee employee, int year) throws DataStorageException;
    int getCountOfOvertimesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;

    //remove
    void cancelOvertime(Overtime overtime) throws DataStorageException;

}
