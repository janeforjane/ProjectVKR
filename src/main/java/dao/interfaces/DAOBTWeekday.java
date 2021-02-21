package dao.interfaces;

import dao.exception.DataStorageException;
import entities.BusinessTripWeekday;
import entities.Employee;
import entities.Sickday;
import entities.Vacation;

import java.time.LocalDate;
import java.util.List;

public interface DAOBTWeekday {

    void newBTWeekday (BusinessTripWeekday businessTripWeekday) throws DataStorageException;
    void modifyBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException;
    void removeBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException;
    BusinessTripWeekday getBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException;
    List<BusinessTripWeekday> getAllBTWeekdays();
    List<BusinessTripWeekday> getAllEmployeeBTWeekdays(Employee employee, int year);
    List<BusinessTripWeekday> getAllEmployeeBTWeekdaysForPeriod(Employee employee, LocalDate dateFrom, LocalDate dateTo);
    List<BusinessTripWeekday> getAllBTWeekdaysForPeriod(LocalDate dateFrom, LocalDate dateTo);

    List<BusinessTripWeekday> getAllEmployeeActiveBTWeekdays(Employee employee, int year) throws DataStorageException;



}
