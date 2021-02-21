package logic.interfaces;

import dao.exception.DataStorageException;
import entities.Employee;
import entities.Vacation;
import logic.exception.DateIsBusyException;
import logic.exception.EventNotFoundException;
import logic.exception.VacationHaveDateException;

import java.time.LocalDate;
import java.util.List;

public interface VacationLogic {


    //enter
    void createPlanVacation(Vacation vacation) throws DateIsBusyException, DataStorageException;
    void replaceVacation(Employee employee, Vacation vacationDayForCancel, LocalDate dateOfNewVacation) throws DateIsBusyException, DataStorageException;
    void addCommentForVacation(Vacation vacation) throws DataStorageException;

    //get
    int getCountOfVacationsOfWeek(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException;
    int getCountEmployeeAvailableVacationDays(Employee employee, int year) throws DataStorageException;
    List<Vacation> getAllPlanVacationDays(Employee employee, int year) throws DataStorageException;
    List<Vacation> getAllFactVacationDays(Employee employee, int year) throws DataStorageException;

    //remove
    void removeEmptyVacation (Vacation vacation) throws VacationHaveDateException, DataStorageException; // для уменьшения количества отпускных дней
    void removeVacationDate (Vacation vacation) throws VacationHaveDateException, DataStorageException; //для отмены даты отпускного дня
                                                                // по сути - перенос на запись без даты


}
