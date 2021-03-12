package dao.interfaces;

import dao.exception.DataStorageException;
import entities.BusinessTripWeekEnd;
import entities.BusinessTripWeekday;
import entities.Employee;

import java.time.LocalDate;
import java.util.List;

public interface DAOBTWeekEnd {


    void newBTWeekEnd (BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException;
    void modifyBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException;
    void removeBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException;
    BusinessTripWeekEnd getBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException;
    List<BusinessTripWeekEnd> getAllActiveBTWeekEnd(int year) throws DataStorageException;
//    List<BusinessTripWeekEnd> getAllEmployeeBTWeekEnd(Employee employee, int year);
//    List<BusinessTripWeekEnd> getAllEmployeeBTWeekendsForPeriod(Employee employee, LocalDate dateFrom, LocalDate dateTo);
    List<BusinessTripWeekEnd> getAllEmployeeActiveBTWeekEnd(Employee employee, int year) throws DataStorageException;
}
