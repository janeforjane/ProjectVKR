package dao;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOBTWeekEnd;
import entities.BusinessTripWeekEnd;
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
public class DAOBTWeekEndImpl implements DAOBTWeekEnd {


//    @PersistenceUnit(unitName = "input-message")
//    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext(unitName = "input-message")
    private EntityManager entityManager;

    private static final Logger log = LogManager.getLogger(DAOBTWeekEndImpl.class);

    @Override
    public void newBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {

        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//            entityManager.getTransaction().begin();
            entityManager.persist(businessTripWeekEnd);
//            entityManager.getTransaction().commit();

            log.info("newBTWeekEnd:new BTWeekEnd was persist in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public void modifyBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {

        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//            entityManager.getTransaction().begin();
            entityManager.merge(businessTripWeekEnd);
//            entityManager.getTransaction().commit();

            log.info("modifyBTWeekEnd: BTWeekEnd was modify in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void removeBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {

        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//            entityManager.getTransaction().begin();
            entityManager.remove(businessTripWeekEnd);
//            entityManager.getTransaction().commit();

            log.info("removeBTWeekEnd: BTWeekEnd was remove in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public BusinessTripWeekEnd getBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {
        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();

            BusinessTripWeekEnd BTWeekEndFromDB = entityManager.find(BusinessTripWeekEnd.class, businessTripWeekEnd.getID());

            if (BTWeekEndFromDB != null) {
                return BTWeekEndFromDB;

            }else {
                log.debug("getBTWeekEnd: EventEndNotFoundException with ID"+ businessTripWeekEnd.getID());
                throw new EventNotFoundException("BTWeekEnd  " + businessTripWeekEnd.getDateOfEvent() + " doesn`t exist");
            }
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public List<BusinessTripWeekEnd> getAllActiveBTWeekEnd(int year) throws DataStorageException {
        try {


            final List allActiveBTWeekEnd = new ArrayList();

            List allActiveBTWeekEnd2 = entityManager
                    .createQuery("from BusinessTripWeekEnd where statusEvent=:st_ev  " +
                            "and   dateOfEvent between :from and :to", BusinessTripWeekEnd.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("from", LocalDate.of(year, 1,1))
                    .setParameter("to", LocalDate.of(year, 12,31))
                    .getResultList();

            allActiveBTWeekEnd.addAll(allActiveBTWeekEnd2);

//            entityManager.getTransaction().commit();

            return allActiveBTWeekEnd;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<BusinessTripWeekEnd> getAllEmployeeActiveBTWeekEnd(Employee employee, int year) throws DataStorageException {
        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            final List allEmployeeActiveBTWeekEnd = new ArrayList();

            List allEmployeeActiveBTWeekEnd2 = entityManager
                    .createQuery("from BusinessTripWeekEnd where statusEvent=:st_ev  " +
                            "and employee=:empl and   dateOfEvent between :from and :to", BusinessTripWeekEnd.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("empl", employee)
                    .setParameter("from", LocalDate.of(year, 1,1))
                    .setParameter("to", LocalDate.of(year, 12,31))
                    .getResultList();

            allEmployeeActiveBTWeekEnd.addAll(allEmployeeActiveBTWeekEnd2);

//            entityManager.getTransaction().commit();

            return allEmployeeActiveBTWeekEnd;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }
}
