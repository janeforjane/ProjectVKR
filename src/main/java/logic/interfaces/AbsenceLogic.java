package logic.interfaces;

import dao.exception.DataStorageException;
import entities.Absence;
import entities.CommonOvertime;
import entities.Employee;
import logic.exception.DateIsBusyException;
import logic.exception.ReasonAlreadyExistException;

import java.time.LocalDate;
import java.util.List;

public interface AbsenceLogic {

    //enter
    void createAbsence(Employee employee, LocalDate dateOfAbsence) throws DateIsBusyException, DataStorageException;
    void modifyAbsenceComment(Absence absence) throws DataStorageException;
    void removeReasonForAbsence (Absence absence, CommonOvertime commonOvertime) throws DataStorageException;//save in DB absence
    void addReasonOfAbsence(Absence absence, CommonOvertime additionalCommonOvertime) throws DataStorageException, ReasonAlreadyExistException;

    //get
    List<Absence> getAllActiveAbsenceDays(Employee employee, int year) throws DataStorageException;
    List<Absence> getAllAbsenceDaysWithReasons(Employee employee, int year) throws DataStorageException;
    List<Absence> getAllAbsenceDaysWithoutReasons(Employee employee, int year) throws DataStorageException;
    int getCountOfEmployeeAbsenceDays(Employee employee, int year) throws DataStorageException;
    int getCountOfAbsencesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    List<Absence> getAllActiveOfAbsencesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;


    //remove
    void cancelAbsence(Absence absence) throws DataStorageException;
}
