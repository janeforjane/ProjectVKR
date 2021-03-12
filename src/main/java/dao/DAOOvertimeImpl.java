package dao;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOOvertime;
import entities.Employee;
import entities.Overtime;
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
public class DAOOvertimeImpl implements DAOOvertime {

//    @PersistenceUnit(unitName = "input-message")
//    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext(unitName = "input-message")
    private EntityManager entityManager;

    private static final Logger log = LogManager.getLogger(DAOOvertimeImpl.class);

    @Override
    public void newOvertime(Overtime overtime) throws DataStorageException {

        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();

//            entityManager.getTransaction().begin();
            entityManager.persist(overtime);
//            entityManager.getTransaction().commit();

            log.info("newOvertime:new overtime was persist in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void modifyOvertime(Overtime overtime) throws DataStorageException {

        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
            entityManager.merge(overtime);
//            entityManager.getTransaction().commit();

            log.info("modifyOvertime:overtime was modify in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void removeOvertime(Overtime overtime) throws DataStorageException {


        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
            entityManager.remove(overtime);
//            entityManager.getTransaction().commit();

            log.info("removeOvertime:overtime was remove in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public Overtime getOvertime(Overtime overtime) throws DataStorageException {
        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();

            Overtime overtimeFromDB = entityManager.find(Overtime.class, overtime.getID());

            if (overtimeFromDB != null) {
                return overtimeFromDB;

            }else {
                log.debug("getOvertime: EventNotFoundException with ID"+ overtime.getID());
                throw new EventNotFoundException("Overtime  " + overtime.getDateOfEvent() + " doesn`t exist");
            }
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public List<Overtime> getAllEmployeeActiveOvertimeDays(Employee employee, int year) throws DataStorageException {
        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            List allOvertimes = new ArrayList();

            List allOvertimes2 = entityManager
                    .createQuery("from Overtime where statusEvent=:st_ev  " +
                            "and employee=:empl and   dateOfEvent between :from and :to", Overtime.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("empl", employee)
                    .setParameter("from", LocalDate.of(year, 1,1))
                    .setParameter("to", LocalDate.of(year, 12,31))
                    .getResultList();

            allOvertimes.addAll(allOvertimes2);

//            entityManager.getTransaction().commit();

            return allOvertimes;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<Overtime> getAllActiveOvertimeDaysForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        try {

//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();

            List allSickdays = new ArrayList();
            List allSickdays2 = entityManager
                    .createQuery("from Overtime where statusEvent=:st_ev and   dateOfEvent between :from and :to", Overtime.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("from", dateFrom)
                    .setParameter("to", dateTo)
                    .getResultList();

//            entityManager.getTransaction().commit();

            allSickdays.addAll(allSickdays2);

            return allSickdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }
}
