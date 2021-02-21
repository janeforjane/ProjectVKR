package dao.interfaces;

import dao.exception.DataStorageException;
import entities.Employee;
import entities.Sickday;
import entities.Vacation;

import java.time.LocalDate;
import java.util.List;

public interface DAOSickday {


    //todo нужны ли отмененные записи?

    void newSickday (Sickday sickday) throws DataStorageException;
    void modifySickday(Sickday sickday) throws DataStorageException;
    void removeSickday(Sickday sickday) throws DataStorageException;
    Sickday getSickday(Sickday sickday) throws DataStorageException;
    List<Sickday> getAllSickdays() throws DataStorageException;
    List<Sickday> getAllActiveSickdays() throws DataStorageException;
    List<Sickday> getAllActiveSickdaysForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    List<Sickday> getAllEmployeeActiveSickdays(Employee employee, int year) throws DataStorageException;

}
