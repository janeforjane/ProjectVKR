package logic.interfaces;

import dao.exception.DataStorageException;
import entities.Employee;
import logic.exception.DuplicateFIOException;
import logic.exception.EmployeeNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeLogic {


    //enter
    void createEmployee(String name, String secondName, String lastName, LocalDate entryDate, String position) throws DuplicateFIOException, DataStorageException;
    void modifyEmployee(Employee employee) throws EmployeeNotFoundException, DataStorageException;
    //get
    Optional<Employee> getEmployee(Long id) throws DataStorageException;
    List<Employee> getFilterEmployees(Employee employee) throws DataStorageException;//поиск сотрудников по заполненным полям в employee,
                                                        // исключая уволенных
    List<Employee> getAllActiveEmployees() throws DataStorageException;// исключая уволенных
    List<Employee> getAllNotActiveEmployees() throws DataStorageException;// только уволенные
    List<Employee> getAllEmployees() throws DataStorageException;// не исключая уволенных

    //remove
    void removeEmployee(Employee employee, LocalDate cancelDate) throws EmployeeNotFoundException, DataStorageException;//увольнение - проставить cancelDate

}
