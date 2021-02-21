package logic.interfaces;

import dao.exception.DataStorageException;
import entities.*;
import logic.exception.DateIsBusyException;

import java.time.LocalDate;
import java.util.List;

public interface BTWeekEndLogic {

    //enter
    void createBTWeekEnd(Employee employee, LocalDate dateOfBTWeekEnd) throws DateIsBusyException, DataStorageException;
    void removeAbsenceFromBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException;
    void modifyBTWeekendComment(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException;
//    void createAbsenceForBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd, Absence absence);

    //get
    Absence getAbsenceForOvertime(BusinessTripWeekEnd businessTripWeekEnd);
    List<BusinessTripWeekEnd> getAllActiveBTWeekEnd(Employee employee, int year) throws DataStorageException;
    List<BusinessTripWeekEnd> getAllBTWeekEndWithAbsences(Employee employee, int year) throws DataStorageException;
    List<BusinessTripWeekEnd> getAllBTWeekEndWithoutAbsences(Employee employee, int year) throws DataStorageException;
    int getCountOfBTWeekEndDays (Employee employee, int year) throws DataStorageException;

    //remove
    void cancelBTWeekEnd (BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException;

}
