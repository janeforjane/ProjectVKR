package dao.interfaces;

import dao.exception.DataStorageException;
import entities.BusinessTripWeekday;
import entities.Employee;
import entities.PaidOvertime;

import java.time.LocalDate;
import java.util.List;

public interface DAOPaidOvertime {

    void newPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException;
    void modifyPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException;


    List<PaidOvertime> getAllEmployeePaidOvertimeForPeriod(Employee employee, LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    List<PaidOvertime> getAllPaidOvertimeForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    List<PaidOvertime> getAllEmployeeActivePaidOvertime(Employee employee, int year) throws DataStorageException;

}
