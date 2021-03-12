package dao;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOBTWeekday;
import entities.BusinessTripWeekday;
import entities.Employee;
import entities.Sickday;
import logic.exception.EventNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Local
//@TransactionManagement(value= TransactionManagementType.BEAN)
public class DAOBTWeekdayImpl implements DAOBTWeekday {

//    @PersistenceUnit(unitName = "input-message")
//    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext(unitName = "input-message")
    private EntityManager entityManager;

    private static final Logger log = LogManager.getLogger(DAOBTWeekdayImpl.class);

    @Override
    public void newBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException {


        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//            entityManager.getTransaction().begin();
            entityManager.persist(businessTripWeekday);
//            entityManager.getTransaction().commit();

            log.info("newBTWeekday:new BTWeekday was persist in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void modifyBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException {

        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
            entityManager.merge(businessTripWeekday);
//            entityManager.getTransaction().commit();

            log.info("modifyBTWeekday:BTWeekday was modify in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void removeBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException {

        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
            entityManager.remove(businessTripWeekday);
//            entityManager.getTransaction().commit();

            log.info("removeBTWeekday:BTWeekday was remove in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }

    }

    @Override
    public BusinessTripWeekday getBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException {
        //todo doesn't use from interface

        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();

            BusinessTripWeekday businessTripWeekdayFromDB = entityManager.find(BusinessTripWeekday.class, businessTripWeekday.getID());

            if (businessTripWeekdayFromDB != null) {
                return businessTripWeekdayFromDB;

            }else {
                log.debug("getBTWeekday: BTWeekdayNotFoundException with ID"+ businessTripWeekdayFromDB.getID());
                throw new EventNotFoundException("BTWeekday  " + businessTripWeekday.getDateOfEvent() + " doesn`t exist");
            }
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public List<BusinessTripWeekday> getAllBTWeekdays() {
        //todo doesn't use from interface
        return null;
    }

    @Override
    public List<BusinessTripWeekday> getAllEmployeeBTWeekdays(Employee employee, int year) {
        //todo doesn't use from interface
        return null;
    }

    @Override
    public List<BusinessTripWeekday> getAllEmployeeBTWeekdaysForPeriod(Employee employee, LocalDate dateFrom, LocalDate dateTo) {
        //todo doesn't use from interface
        return null;
    }

    @Override
    public List<BusinessTripWeekday> getAllBTWeekdaysForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        try {


            final List allEmployeeActiveBTWeekdays = new ArrayList();

            List allEmployeeActiveBTWeekdays2 = entityManager
                    .createQuery("from BusinessTripWeekday where statusEvent=:st_ev  " +
                            "and   dateOfEvent between :from and :to", BusinessTripWeekday.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("from", dateFrom)
                    .setParameter("to", dateTo)
                    .getResultList();

            allEmployeeActiveBTWeekdays.addAll(allEmployeeActiveBTWeekdays2);

            return allEmployeeActiveBTWeekdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<BusinessTripWeekday> getAllEmployeeActiveBTWeekdays(Employee employee, int year) throws DataStorageException {


        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            final List allEmployeeActiveBTWeekdays = new ArrayList();

            List allEmployeeActiveBTWeekdays2 = entityManager
                    .createQuery("from BusinessTripWeekday where statusEvent=:st_ev  " +
                            "and employee=:empl and   dateOfEvent between :from and :to", BusinessTripWeekday.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("empl", employee)
                    .setParameter("from", LocalDate.of(year, 1,1))
                    .setParameter("to", LocalDate.of(year, 12,31))
                    .getResultList();

            allEmployeeActiveBTWeekdays.addAll(allEmployeeActiveBTWeekdays2);

//            entityManager.getTransaction().commit();

            return allEmployeeActiveBTWeekdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }
}
