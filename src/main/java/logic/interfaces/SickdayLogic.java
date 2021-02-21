package logic.interfaces;

import dao.exception.DataStorageException;
import entities.Absence;
import entities.Employee;
import entities.Sickday;
import logic.exception.DateIsBusyException;

import java.time.LocalDate;
import java.util.List;

public interface SickdayLogic {


    //enter
    void createSickday (Employee employee, LocalDate dateOfSickday) throws DateIsBusyException, DataStorageException;
    void modifySickdayComment(Sickday sickday) throws DataStorageException;

    //get
    List<Sickday> getAllActiveSickDays(Employee employee, int year) throws DataStorageException;
    int getCountOfSickdaysOfEmployee(Employee employee, int year) throws DataStorageException;
    int getCountOfAllSickdaysForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;


    //remove
    void cancelSickday (Sickday sickday) throws DataStorageException;
}
