package dao.interfaces;

import dao.exception.DataStorageException;
import entities.Employee;
import entities.Vacation;
import logic.exception.EventNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface DAOVacation {


    void newVacation (Vacation vacation) throws DataStorageException;
    void modifyVacation(Vacation vacation) throws DataStorageException;
    void removeVacation(Vacation vacation) throws DataStorageException;
    Vacation getVacation(Vacation vacation) throws DataStorageException;
    List<Vacation> getAllVacation() throws DataStorageException;
    List<Vacation> getAllActiveVacationForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    List<Vacation> getAllEmployeeVacation(Employee employee, int year) throws DataStorageException;//с отменами тоже
    List<Vacation> getAllEmptyVacationDays(Employee employee, int year) throws DataStorageException;//запланированные отпуска без дат
    List<Vacation> getAllEmployeeActiveVacationDays(Employee employee, int year) throws DataStorageException;
}
