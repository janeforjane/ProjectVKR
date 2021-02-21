package dao;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOAbsence;
import entities.Absence;
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
public class DAOAbsenceImpl implements DAOAbsence {

    @PersistenceUnit(unitName = "input-message")
    private EntityManagerFactory entityManagerFactory;

    private static final Logger log = LogManager.getLogger(DAOAbsenceImpl.class);

    @Override
    public void newAbsence(Absence absence) throws DataStorageException {

        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();

            entityManager.getTransaction().begin();
            entityManager.persist(absence);
            entityManager.getTransaction().commit();

            log.info("newAbsence:new absence was persist in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void modifyAbsence(Absence absence) throws DataStorageException {

        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(absence);
            entityManager.getTransaction().commit();

            log.info("modifyAbsence:absence was modify in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }

    }

    @Override
    public void removeAbsence(Absence absence) throws DataStorageException {

        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(absence);
            entityManager.getTransaction().commit();

            log.info("removeAbsence:absence was remove in DB");


        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }

    }

    @Override
    public Absence getAbsence(Absence absence) throws DataStorageException {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            Absence absenceFromDB = entityManager.find(Absence.class, absence.getID());

            if (absenceFromDB != null) {
                return absenceFromDB;

            }else {
                log.debug("getAbsence: EventNotFoundException with ID"+ absence.getID());
                throw new EventNotFoundException("Absence  " + absence.getDateOfEvent() + " doesn`t exist");
            }
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);
        }
    }

    @Override
    public List<Absence> getAllEmployeeActiveAbsences(Employee employee, int year) throws DataStorageException {
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            final List allAbsences = new ArrayList();

            List allAbsences2 = entityManager
                    .createQuery("from Absence where statusEvent=:st_ev  " +
                            "and employee=:empl and   dateOfEvent between :from and :to", Absence.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("empl", employee)
                    .setParameter("from", LocalDate.of(year, 1,1))
                    .setParameter("to", LocalDate.of(year, 12,31))
                    .getResultList();
            allAbsences.addAll(allAbsences2);

            entityManager.getTransaction().commit();

            return allAbsences;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }

    @Override
    public List<Absence> getAllActiveAbsencesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List allAbsences = entityManager
                    .createQuery("from Absence where statusEvent=:st_ev and   dateOfEvent between :from and :to", Absence.class)
                    .setParameter("st_ev", StatusEvent.ACTIVE)
                    .setParameter("from", dateFrom)
                    .setParameter("to", dateTo)
//                    .setParameter("empl", employee)
                    .getResultList();

            entityManager.getTransaction().commit();

            return allAbsences;
        }catch (Exception e){
            throw new DataStorageException("Error. Data Storage error.", e);

        }
    }
}
