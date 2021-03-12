package logic.interfaces;

import dao.exception.DataStorageException;
import entities.BusinessTripWeekday;
import entities.Employee;
import logic.exception.DateIsBusyException;

import java.time.LocalDate;
import java.util.List;

public interface BTWeekdayLogic {


    //enter
    void createBTWeekday (Employee employee, LocalDate dateOfBTWeekday) throws DateIsBusyException, DataStorageException;
    void modifyCommentBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException;

    //get
    List<BusinessTripWeekday> getAllActiveEmployeeBTWeekday(Employee employee, int year) throws DataStorageException;
    List<BusinessTripWeekday> getAllActiveBTWeekday(int year) throws DataStorageException;
    int getCountOfActiveEmployeeBTWeekday(Employee employee, int year) throws DataStorageException;
//    int getCountOfActiveBTForPeriod(LocalDate dateFrom, LocalDate dateTo);
//    int getCountOfBusinessTripsForPeriodOfEmployee(Employee employee, LocalDate dateFrom, LocalDate dateTo);

    //remove
    void cancelBTWeekday (BusinessTripWeekday businessTripWeekday) throws DataStorageException;
}
