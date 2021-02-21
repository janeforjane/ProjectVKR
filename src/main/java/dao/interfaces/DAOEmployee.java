package dao.interfaces;

import dao.exception.DataStorageException;
import entities.Employee;
import logic.exception.EmployeeNotFoundException;

import java.util.List;
import java.util.Optional;

public interface DAOEmployee {

    void newEmployee (Employee employee) throws DataStorageException;
    void modifyEmployee(Employee employee) throws EmployeeNotFoundException, DataStorageException;
    Optional<Employee> getEmployee(Long id) throws DataStorageException;
//    void removeEmployee(Employee employee);
//    Vacation getEmployee(Employee employee);
    List<Employee> getAllEmployees() throws DataStorageException;
    boolean isEmployeeExist(String lastname, String name, String secondName) throws DataStorageException;

}
