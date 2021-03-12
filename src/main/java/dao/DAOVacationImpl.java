package dao;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOVacation;
import entities.Employee;
import entities.Vacation;
import logic.exception.DateIsBusyException;
import logic.exception.EmployeeNotFoundException;
import logic.exception.EventNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
//@LocalBean
@Local
//@TransactionManagement(value= TransactionManagementType.BEAN)
public class DAOVacationImpl implements DAOVacation {

//    @PersistenceUnit(unitName = "input-message")
//    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext(unitName = "input-message")
    private EntityManager entityManager;


    private static final Logger log = LogManager.getLogger(DAOVacationImpl.class);

    @Override
    public void newVacation(Vacation vacation) throws DataStorageException {

        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//            entityManager.getTransaction().begin();
            entityManager.persist(vacation);
//            entityManager.getTransaction().commit();

            log.info("newVacation:new vacation was persist in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void modifyVacation(Vacation vacation) throws DataStorageException {

        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
            entityManager.merge(vacation);
//            entityManager.getTransaction().commit();

            log.info("modifyVacation:vacation was modify in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }

    }

    @Override
    public void removeVacation(Vacation vacation) throws DataStorageException {


        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
            entityManager.remove(vacation);
//            entityManager.getTransaction().commit();

            log.info("removeVacation:vacation was remove in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public Vacation getVacation(Vacation vacation) throws DataStorageException {

        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();

            Vacation vacationFromDB = entityManager.find(Vacation.class, vacation.getID());

            if (vacationFromDB != null) {
                return vacationFromDB;

            }else {
                log.debug("getVacation: EventNotFoundException with ID"+ vacation.getID());
                throw new EventNotFoundException("Vacation  " + vacation.getDateOfEvent() + " doesn`t exist");
            }
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public List<Vacation> getAllVacation() throws DataStorageException {
        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            List allVacations = entityManager
                    .createQuery("from Vacation where yearOfEvent=:year", Vacation.class)
                    .setParameter("year", 2020)
                    .getResultList();

//            entityManager.getTransaction().commit();


            return allVacations;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<Vacation> getAllActiveVacationForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            final List allVacations = new ArrayList();

            List allVacations2 = entityManager
                    .createQuery("from Vacation where statusEvent=:st_ev and   dateOfEvent between :from and :to", Vacation.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("from", dateFrom)
                    .setParameter("to", dateTo)
                    .getResultList();


            allVacations.addAll(allVacations2);
//            entityManager.getTransaction().commit();

            return allVacations;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<Vacation> getAllEmployeeVacation(Employee employee, int year) throws DataStorageException {
        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            List allVacations = entityManager
                    .createQuery("from Vacation where employee=:empl and yearOfEvent=:year ", Vacation.class)
//                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("empl", employee)
                    .setParameter("year", year)
                    .getResultList();

//            entityManager.getTransaction().commit();

            return allVacations;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<Vacation> getAllEmptyVacationDays(Employee employee, int year) throws DataStorageException {

        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            final List allVacations = new ArrayList();

            List allVacations2 = entityManager
//                    .createQuery("from Vacation where yearOfEvent=:year and employee=:empl and dateOfEvent=:dtEvent", Vacation.class)
                    .createQuery("from Vacation where yearOfEvent=:year and employee=:empl and dateOfEvent=null", Vacation.class)
//                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("year", year)
                    .setParameter("empl", employee)
//                    .setParameter("dtEvent", null)
                    .getResultList();

            allVacations.addAll(allVacations2);

//            entityManager.getTransaction().commit();

            return allVacations;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<Vacation> getAllEmployeeActiveVacationDays(Employee employee, int year) throws DataStorageException {
        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();


            final List allVacations = new ArrayList();

            List allVacations2 = entityManager
                    .createQuery("from Vacation where statusEvent=:st_ev and yearOfEvent=:year and employee=:empl", Vacation.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("year", year)
                    .setParameter("empl", employee)
                    .getResultList();

            allVacations.addAll(allVacations2);

//            entityManager.getTransaction().commit();
            return allVacations;

        }
        catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }
}

