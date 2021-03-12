package logic.interfaces;

import dao.exception.DataStorageException;
import entities.BusinessTripWeekday;
import entities.Employee;
import entities.PaidOvertime;
import logic.exception.DateIsBusyException;

import java.time.LocalDate;
import java.util.List;

public interface PaidOvertimeLogic {


    //enter
    void createPaidOvertime (Employee employee, LocalDate dateOfPaidOvertime) throws DateIsBusyException, DataStorageException;
    void modifyCommentPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException;

    //get
    List<PaidOvertime> getAllActivePaidOvertime(Employee employee, int year) throws DataStorageException;
    int getCountOfPaidOvertime (Employee employee, int year) throws DataStorageException;
    int getCountOfPaidOvertimeForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    List<PaidOvertime> getAllActivefPaidOvertimeForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    int getCountOfPaidOvertimeForPeriodOfEmployee(Employee employee, LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;

    //remove
    void cancelPaidOvertime (PaidOvertime paidOvertime) throws DataStorageException;

}
