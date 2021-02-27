package logic.interfaces;

import dao.exception.DataStorageException;
import entities.*;
import logic.exception.DateIsBusyException;
import logic.exception.ReasonAlreadyExistException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BTWeekEndLogic {

    //enter
    void createBTWeekEndWithoutAbsence(Employee employee, LocalDate dateOfBTWeekEnd) throws DateIsBusyException, DataStorageException;
    void createBTWeekEndWithAbsence(Employee employee, LocalDate dateOfBTWeekEnd, LocalDate dateOfAbsence) throws DateIsBusyException, DataStorageException;
    void addAbsence(Employee employee, BusinessTripWeekEnd businessTripWeekEnd, Absence absence) throws ReasonAlreadyExistException, DataStorageException;
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
