package logic;

import box.StatusEvent;
import dao.interfaces.DAOSickday;
import dao.exception.DataStorageException;
import entities.Employee;
import entities.Sickday;
import logic.exception.DateIsBusyException;
import logic.interfaces.EventLogic;
import logic.interfaces.SickdayLogic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.*;
import java.time.LocalDate;
import java.util.List;
@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SickdayLogicImpl implements SickdayLogic {

    @EJB
    DAOSickday daoSickday;
    @EJB
    EventLogic eventLogic;

    private static final Logger log = LogManager.getLogger(SickdayLogicImpl.class);

    @Override
    public void createSickday(Employee employee, LocalDate dateOfSickday) throws DateIsBusyException, DataStorageException {

        //todo если дата занята - то надо спросить и перенести
        if (eventLogic.isDateFree(employee, dateOfSickday)){

            Sickday sickday = new Sickday(employee, dateOfSickday);
            sickday.setStatusEvent(StatusEvent.ACTIVE);
            daoSickday.newSickday(sickday);

            log.info("Logic: createSickday - sickday was create");

        }else {
            throw new DateIsBusyException("Date "+ dateOfSickday.toString()+" is busy");
        }
    }

    @Override
    public void modifySickdayComment(Sickday sickday) throws DataStorageException {
        daoSickday.modifySickday(sickday);

        log.info("Logic: modifySickdayComment - sickday comment was create");
    }

    @Override
    public List<Sickday> getAllActiveSickDays(Employee employee, int year) throws DataStorageException {

        List<Sickday> allEmployeeActiveSickdays = daoSickday.getAllEmployeeActiveSickdays(employee, year);

        return allEmployeeActiveSickdays;
    }

    @Override
    public int getCountOfSickdaysOfEmployee(Employee employee, int year) throws DataStorageException {
        List<Sickday> allEmployeeActiveSickdays = daoSickday.getAllEmployeeActiveSickdays(employee, year);
        return allEmployeeActiveSickdays.size();
    }

    @Override
    public int getCountOfAllSickdaysForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        return daoSickday.getAllActiveSickdaysForPeriod(dateFrom, dateTo).size();
    }

    @Override
    public void cancelSickday(Sickday sickday) throws DataStorageException {

        sickday.setStatusEvent(StatusEvent.NOTACTIVE);
        daoSickday.modifySickday(sickday);

        log.info("Logic: cancelSickday - sickday was modify(StatusEvent.NOTACTIVE)");

    }
}
