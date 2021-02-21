package dao.interfaces;

import dao.exception.DataStorageException;
import entities.Absence;
import entities.Employee;

import java.time.LocalDate;
import java.util.List;

public interface DAOAbsence {

    void newAbsence(Absence absence) throws DataStorageException;
    void modifyAbsence(Absence absence) throws DataStorageException;
    void removeAbsence(Absence absence) throws DataStorageException;
    Absence getAbsence(Absence absence) throws DataStorageException;

//    List<Absence> getAllAbsences();
//    List<Absence> getAllEmployeeAbsences(Employee employee, int year);
    List<Absence> getAllEmployeeActiveAbsences(Employee employee, int year) throws DataStorageException;
    List<Absence> getAllActiveAbsencesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;



}
