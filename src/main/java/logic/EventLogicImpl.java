package logic;

import dao.*;
import dao.exception.DataStorageException;
import dao.interfaces.*;
import entities.Employee;
import entities.Event;
import entities.Vacation;
import logic.exception.EventNotFoundException;
import logic.interfaces.EventLogic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EventLogicImpl implements EventLogic {

    @EJB
    DAOVacation daoVacation;
    @EJB
    DAOSickday daoSickday;
    @EJB
    DAOBTWeekEnd daobtWeekEnd;
    @EJB
    DAOBTWeekday daobtWeekday;
    @EJB
    DAOOvertime daoOvertime;
    @EJB
    DAOAbsence daoAbsence;
    @EJB
    DAOPaidOvertime daoPaidOvertime;
    @EJB
    DAOEmployee daoEmployee;

    private static final Logger log = LogManager.getLogger(EventLogicImpl.class);

    @Override
    public List<Event> getAllEvents() {
        //todo нигде не используется
        return null;
    }


    @Override
    public List<Event> getAllActiveEvents(Employee employee, int year) throws DataStorageException{
        List<Event> allActiveEvents = new ArrayList<>();

        allActiveEvents.addAll(daoVacation.getAllEmployeeActiveVacationDays(employee, year));
        allActiveEvents.addAll(daoSickday.getAllEmployeeActiveSickdays(employee, year));
        allActiveEvents.addAll(daobtWeekEnd.getAllEmployeeActiveBTWeekEnd(employee, year));
        allActiveEvents.addAll(daobtWeekday.getAllEmployeeActiveBTWeekdays(employee, year));
        allActiveEvents.addAll(daoOvertime.getAllEmployeeActiveOvertimeDays(employee, year));
        allActiveEvents.addAll(daoPaidOvertime.getAllEmployeeActivePaidOvertime(employee, year));
        allActiveEvents.addAll(daoAbsence.getAllEmployeeActiveAbsences(employee, year));

        return allActiveEvents;

    }

    @Override
    public boolean isDateFree(Employee employee, LocalDate date) throws DataStorageException{

        boolean isDateFree = true;
        List<Event> allActiveEvents = new ArrayList<>();
        allActiveEvents.addAll(getAllActiveEvents(employee, date.getYear()));

        if (allActiveEvents.size() > 0) {

            for (int i = 0; i < allActiveEvents.size(); i++) {
                if (allActiveEvents.get(i).getDateOfEvent().equals(date)) {
                    isDateFree = false;
                    break;
                }
            }
        }
        return isDateFree;
    }

    @Override
    public int countOfBusinessTrips(Employee employee, int year) throws DataStorageException {
        int countOfBTWeekdays = daobtWeekday.getAllEmployeeActiveBTWeekdays(employee, year).size();
        int countOfBTWeekends = daobtWeekEnd.getAllEmployeeActiveBTWeekEnd(employee, year).size();

        return countOfBTWeekdays+countOfBTWeekends;
    }

    @Override
    public List<Event> getAllAbsencesEmployeeEvents(Employee employee, int year) throws DataStorageException, EventNotFoundException {
        //все отсуствия (без переработок и командировок в выходных)
        List<Event> allAbsencesEmployeeEvents = new ArrayList<>();

        allAbsencesEmployeeEvents.addAll(daoVacation.getAllEmployeeActiveVacationDays(employee, year));
        allAbsencesEmployeeEvents.addAll(daoSickday.getAllEmployeeActiveSickdays(employee, year));
        allAbsencesEmployeeEvents.addAll(daobtWeekday.getAllEmployeeActiveBTWeekdays(employee, year));
        allAbsencesEmployeeEvents.addAll(daoAbsence.getAllEmployeeActiveAbsences(employee, year));

        return allAbsencesEmployeeEvents;
    }

    @Override
    public List<Event> getAllAbsencesEmployeeEventsForPeriod(Employee employee, LocalDate dateFrom, LocalDate dateTo) {
        return null;
        //todo доделать если нужно
    }

    @Override
    public List<Event> getAllEmployeeEvents(Employee employee, int year) {
        return null;
        //todo доделать если нужно
    }
}
