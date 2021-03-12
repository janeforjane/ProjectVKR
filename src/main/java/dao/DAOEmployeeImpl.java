package dao;

import dao.exception.DataStorageException;
import dao.interfaces.DAOEmployee;
import entities.Employee;
import logic.exception.EmployeeNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.A;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
@Local
//@TransactionManagement(value = TransactionManagementType.BEAN)
public class DAOEmployeeImpl implements DAOEmployee {

//    @PersistenceUnit(unitName = "input-message")
//    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext(unitName = "input-message")
    private EntityManager entityManager;

    private static final Logger log = LogManager.getLogger(DAOEmployeeImpl.class);

    @Override
    public void newEmployee(Employee employee) throws DataStorageException {

        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//            entityManager.getTransaction().begin();
            entityManager.persist(employee);
//            entityManager.getTransaction().commit();

            log.info("newEmployee:new employee was persist in DB");

        } catch (Exception e) {
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public Optional<Employee> getEmployee(Long id) throws DataStorageException {

        Employee employeeFromDB = null;
        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();

            employeeFromDB = entityManager.find(Employee.class, id);

            if (employeeFromDB != null) {
                return Optional.of(employeeFromDB);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public void modifyEmployee(Employee employee) throws EmployeeNotFoundException, DataStorageException {

        if (employee == null) {

            log.info("given employee was null");

        }

        Employee employeeInDB = null;
        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();

            employeeInDB = entityManager.find(Employee.class, employee.getID());
            if (employeeInDB != null) {
//                entityManager.getTransaction().begin();
                entityManager.merge(employee);
//                entityManager.getTransaction().commit();

                log.info("modifyEmployee:employee was modify in DB");
            }
        } catch (Exception e) {
            throw new DataStorageException("Error. Data Storage error.", e);
        }

        if (employeeInDB == null) {
            log.debug("modifyEmployee:EmployeeNotFoundException with - {}", employee.toString());
            throw new EmployeeNotFoundException("This employee didn't exist");
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws DataStorageException {

        try {


//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            List allEmployees = new ArrayList();
            List allEmployees2 = entityManager.createQuery("from Employee").getResultList();
            allEmployees.addAll(allEmployees2);

//            entityManager.getTransaction().commit();


            return allEmployees;
        } catch (Exception e) {
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public boolean isEmployeeExist(String lastname, String name, String secondName) throws DataStorageException {
        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            boolean exist = !entityManager.createQuery
                    ("from Employee where name=:name and secondName=:secondName and lastName=:ln", Employee.class)
                    .setParameter("name", name)
                    .setParameter("secondName", secondName)
                    .setParameter("ln", lastname)
                    .getResultList().isEmpty();
//       Employee employee = entityManager.createQuery("from Employee where ");

//            entityManager.getTransaction().commit();
            return exist;
        } catch (Exception e) {
            throw new DataStorageException("isEmployeeExist:Error. Data Storage error.", e);
        }

    }
}
