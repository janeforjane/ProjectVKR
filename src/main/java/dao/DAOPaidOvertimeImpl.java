package dao;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOPaidOvertime;
import entities.Employee;
import entities.PaidOvertime;
import entities.Sickday;
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
//@LocalBean
@Local
@TransactionManagement(value= TransactionManagementType.BEAN)
public class DAOPaidOvertimeImpl implements DAOPaidOvertime {


    @PersistenceUnit(unitName = "input-message")
    private EntityManagerFactory entityManagerFactory;

    private static final Logger log = LogManager.getLogger(DAOPaidOvertimeImpl.class);

    @Override
    public void newPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException {

        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();

            entityManager.getTransaction().begin();
            entityManager.persist(paidOvertime);
            entityManager.getTransaction().commit();

            log.info("newPaidOvertime:new paidOvertime was persist in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public void modifyPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException {

        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(paidOvertime);
            entityManager.getTransaction().commit();

            log.info("modifyPaidOvertime:paidOvertime was modify in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }

    }

    @Override
    public List<PaidOvertime> getAllEmployeePaidOvertimeForPeriod(Employee employee, LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List allSickdays = entityManager
                    .createQuery("from PaidOvertime where employee=:empl and   dateOfEvent between :from and :to", PaidOvertime.class)
                    .setParameter("empl", employee)
                    .setParameter("from", dateFrom)
                    .setParameter("to", dateTo)
                    .getResultList();

            entityManager.getTransaction().commit();

            return allSickdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<PaidOvertime> getAllPaidOvertimeForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List allSickdays = entityManager
                    .createQuery("from PaidOvertime where dateOfEvent between :from and :to", PaidOvertime.class)
                    .setParameter("from", dateFrom)
                    .setParameter("to", dateTo)
                    .getResultList();

            entityManager.getTransaction().commit();

            return allSickdays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<PaidOvertime> getAllEmployeeActivePaidOvertime(Employee employee, int year) throws DataStorageException {

        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            final List allPaidOvertimedays =new ArrayList();

            List allPaidOvertimedays2 = entityManager
                    .createQuery("from PaidOvertime where statusEvent=:st_ev  and employee=:empl and dateOfEvent between :from and :to", PaidOvertime.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("empl", employee)
                    .setParameter("from", LocalDate.of(year, 1,1))
                    .setParameter("to", LocalDate.of(year,12,31))
                    .getResultList();

            allPaidOvertimedays.addAll(allPaidOvertimedays2);

            entityManager.getTransaction().commit();

            return allPaidOvertimedays;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }
}
