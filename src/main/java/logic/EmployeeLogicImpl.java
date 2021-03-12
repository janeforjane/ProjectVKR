package logic;

import dao.interfaces.DAOEmployee;
import dao.exception.DataStorageException;
import entities.Employee;
import logic.exception.DuplicateFIOException;
import logic.exception.EmployeeNotFoundException;
import logic.interfaces.EmployeeLogic;

import javax.ejb.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmployeeLogicImpl implements EmployeeLogic {

    @EJB
    DAOEmployee daoEmployee;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createEmployee(String name, String secondName, String lastName, LocalDate entryDate, String position) throws DuplicateFIOException, DataStorageException {

        Employee newEmployee = new Employee(lastName, name, secondName, entryDate, position);

        if (daoEmployee.isEmployeeExist(lastName,name, secondName)) {

            throw new DuplicateFIOException("Employee: "+ name +" "+secondName+" "+ lastName+" already exist.");
        }else {
            daoEmployee.newEmployee(newEmployee);
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void modifyEmployee(Employee employee) throws EmployeeNotFoundException, DataStorageException {

        daoEmployee.modifyEmployee(employee);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Optional<Employee> getEmployee(Long id) throws DataStorageException {
        return daoEmployee.getEmployee(id);

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<Employee> getFilterEmployees(Employee employee) throws DataStorageException {
        //todo поиск только по имя, фамилия, должность

        List<Employee> allEmployees = daoEmployee.getAllEmployees();
        List<Employee> filterEmployees = new ArrayList<>();

        for (int i = 0; i < allEmployees.size(); i++) {
            if (allEmployees.get(i).getLastName().contains(employee.getLastName())){
                if(allEmployees.get(i).getLastName().contains(employee.getLastName())){
                    if(allEmployees.get(i).getPosition().contains(employee.getPosition())){
                        filterEmployees.add(allEmployees.get(i));
                    }
                }
            }
        }
        return filterEmployees;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<Employee> getAllActiveEmployees() throws DataStorageException {

        List<Employee> allEmployees = daoEmployee.getAllEmployees();
//        List<Employee> allActiveEmployees = new ArrayList<>();

        List<Employee> allActiveEmployees = allEmployees.stream()
                .filter(e -> e.getCancelDate() == null)
                .collect(Collectors.toList());

//        for (int i = 0; i < allEmployees.size(); i++) {
//            if (allEmployees.get(i).getCancelDate() == null){
//
//                allActiveEmployees.add(allEmployees.get(i));
//            }
//        }


        return allActiveEmployees;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<Employee> getAllNotActiveEmployees() throws DataStorageException {

        List<Employee> allEmployees = daoEmployee.getAllEmployees();
//        List<Employee> allNotActiveEmployees = new ArrayList<>();

        List<Employee> allNotActiveEmployees= allEmployees.stream()
                .filter(e -> e.getCancelDate() ==null)
                .collect(Collectors.toList());

//        for (int i = 0; i < allEmployees.size(); i++) {
//            if (allEmployees.get(i).getCancelDate() == null){
//
//                allNotActiveEmployees.add(allEmployees.get(i));
//            }
//        }
        return allNotActiveEmployees;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<Employee> getAllEmployees() throws DataStorageException {

        return daoEmployee.getAllEmployees();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void removeEmployee(Employee employee, LocalDate cancelDate) throws EmployeeNotFoundException, DataStorageException {

        employee.setCancelDate(cancelDate);
        daoEmployee.modifyEmployee(employee);

    }
}
