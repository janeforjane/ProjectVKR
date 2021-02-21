package dao;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOSickday;
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
import javax.persistence.PersistenceUnit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Stateless
@Local
@TransactionManagement(value= TransactionManagementType.BEAN)
public class DAOSickdayImpl implements DAOSickday {

    @PersistenceUnit(unitName = "input-message")
    private EntityManagerFactory entityManagerFactory;

    private static final Logger log = LogManager.getLogger(DAOSickdayImpl.class);

    @Override
    public void newSickday(Sickday sickday) throws DataStorageException {

        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();

            entityManager.getTransaction().begin();
            entityManager.persist(sickday);
            entityManager.getTransaction().commit();

            log.info("newSickday:new sickday was persist in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void modifySickday(Sickday sickday) throws DataStorageException {

        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(sickday);
            entityManager.getTransaction().commit();

            log.info("modifySickday:sickday was modify in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public void removeSickday(Sickday sickday) throws DataStorageException {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(sickday);
            entityManager.getTransaction().commit();

            log.info("removeSickday:sickday was remove in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public Sickday getSickday(Sickday sickday) throws DataStorageException {

        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            Sickday sickdayFromDB = entityManager.find(Sickday.class, sickday.getID());

            if (sickdayFromDB != null) {
                return sickdayFromDB;

            }else {
                log.debug("getSickday: EventNotFoundException with ID"+ sickday.getID());
                throw new EventNotFoundException("Sickday  " + sickday.getDateOfEvent() + " doesn`t exist");
            }
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public List<Sickday> getAllSickdays() throws DataStorageException {

        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List allSickdays = entityManager
                    .createQuery("from Sickday ", Sickday.class)
                    .getResultList();

            entityManager.getTransaction().commit();

            return allSickdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<Sickday> getAllActiveSickdays() throws DataStorageException {
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List allSickdays = entityManager
                    .createQuery("from Sickday where statusEvent=:st_ev", Sickday.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .getResultList();

            entityManager.getTransaction().commit();

            return allSickdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<Sickday> getAllActiveSickdaysForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List allSickdays = entityManager
                    .createQuery("from Sickday where statusEvent=:st_ev and   dateOfEvent between :from and :to", Sickday.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("from", dateFrom)
                    .setParameter("to", dateTo)
//                    .setParameter("empl", employee)
                    .getResultList();

            entityManager.getTransaction().commit();

            return allSickdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }



    @Override
    public List<Sickday> getAllEmployeeActiveSickdays(Employee employee, int year) throws DataStorageException {

        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            final List allSickdays =new ArrayList();

            List allSickdays2 = entityManager
                    .createQuery("from Sickday where statusEvent=:st_ev  " +
                            "and employee=:empl and   dateOfEvent between :from and :to", Sickday.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("empl", employee)
                    .setParameter("from", LocalDate.of(year, 1,1))
                    .setParameter("to", LocalDate.of(year, 12,31))
                    .getResultList();

            allSickdays.addAll(allSickdays2);

            entityManager.getTransaction().commit();

            return allSickdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }
}
