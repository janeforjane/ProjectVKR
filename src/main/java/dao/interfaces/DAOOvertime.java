package dao.interfaces;

import dao.exception.DataStorageException;
import entities.Absence;
import entities.Employee;
import entities.Overtime;

import java.time.LocalDate;
import java.util.List;

public interface DAOOvertime {
    void newOvertime (Overtime overtime) throws DataStorageException;
    void modifyOvertime(Overtime overtime) throws DataStorageException;
    void removeOvertime(Overtime overtime) throws DataStorageException;
    Overtime getOvertime(Overtime overtime) throws DataStorageException;
//    List<Overtime> getAllOvertimes();
    List<Overtime> getAllEmployeeActiveOvertimeDays(Employee employee, int year) throws DataStorageException;
    List<Overtime> getAllActiveOvertimeDaysForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
}
