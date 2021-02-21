package logic;

import box.EventTag;
import box.StatusEvent;
import dao.interfaces.DAOVacation;
import dao.exception.DataStorageException;
import entities.Employee;
import entities.Vacation;
import logic.exception.DateIsBusyException;
import logic.exception.EventNotFoundException;
import logic.exception.VacationHaveDateException;
import logic.interfaces.EventLogic;
import logic.interfaces.VacationLogic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VacationLogicImpl implements VacationLogic {

    @EJB
    DAOVacation daoVacation;
    @EJB
    EventLogic eventLogic;

    private static final Logger log = LogManager.getLogger(VacationLogicImpl.class);


    @Override
    public void createPlanVacation(Vacation vacation) throws DateIsBusyException, DataStorageException {

        // если без даты - записать в БД новый vacation

        if (vacation.getDateOfEvent() == null) {

            daoVacation.newVacation(vacation);
            log.info("vacation without date - new free vacation created");
        }else { // если с датой - то проверить свободна ли дата

            log.info("vacation with date");

                if (eventLogic.isDateFree(vacation.getEmployee(), vacation.getDateOfEvent())) {

                    log.info("isDateFree = true");

                    List<Vacation> allEmptyVacationDays = daoVacation.getAllEmptyVacationDays(vacation.getEmployee(), vacation.getYearOfEvent());

                    if (allEmptyVacationDays.size() > 0) {

                        log.info("allEmptyVacationDays.size() > 0 = true");

                        Vacation oldVacation = allEmptyVacationDays.get(0);
                        oldVacation.setDateOfEvent(vacation.getDateOfEvent());
                        oldVacation.setComment(vacation.getComment());
                        oldVacation.setEventTag(EventTag.PLAN);
                        oldVacation.setStatusEvent(StatusEvent.ACTIVE);

                        daoVacation.modifyVacation(oldVacation);
                        log.info("was free vacation - vacation was modify(add date)");

                    }else {
                        log.info("allEmptyVacationDays.size() > 0 = false");
                        vacation.setEventTag(EventTag.PLAN);
                        vacation.setStatusEvent(StatusEvent.ACTIVE);

                        daoVacation.newVacation(vacation);
                        log.info("wasn't free vacation - new vacation created");
                    }
                }else {
                    log.info("date is not free - vacation didn't create");
                    throw new DateIsBusyException("Date "+ vacation.getDateOfEvent().toString()+" is busy");
                }
        }

        // если с датой - то проверить свободна ли дата,
        // если с датой - проставить PLAN в eventTag
        // если с датой - проставить ACTIVE в statusEvent

        // проверить наличие vacation без дат
        // если есть перезаписать новый vacation в него
        // если нет vacation без дат - записывать этот как новый

    }

    @Override
    public void addCommentForVacation(Vacation vacation) throws DataStorageException {

        daoVacation.modifyVacation(vacation);
        log.info("addCommentForVacation");

    }

    @Override
    public void replaceVacation(Employee employee, Vacation vacationDayForCancel, LocalDate dateOfNewVacation) throws DateIsBusyException, DataStorageException {
        log.info("replaceVacation");
        //проверить свободна ли дата
        //получить из БД заменяемый vacation
        //создать новый vacation
        //связать старый и новый
        //сделать старый неактивным
        //сделать replace признак у нового

        if (eventLogic.isDateFree(employee,dateOfNewVacation)) {

            Vacation oldVacation = daoVacation.getVacation(vacationDayForCancel);
            oldVacation.setStatusEvent(StatusEvent.NOTACTIVE);


            Vacation newVacation = new Vacation(employee, dateOfNewVacation);
            newVacation.setStatusEvent(StatusEvent.ACTIVE);
            newVacation.setEventTag(EventTag.REPLACE);


            oldVacation.setReplaceDays(newVacation);

            daoVacation.newVacation(newVacation);
            daoVacation.modifyVacation(oldVacation);
            log.info("newVacation was created");
            log.info("oldVacation was modify");


        }else {

            throw new DateIsBusyException("Date "+ dateOfNewVacation.toString()+" is busy");
        }
    }

    @Override
    public int getCountOfVacationsOfWeek(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        List<Vacation> allVacationOfWeek = new ArrayList<>();
        List<Vacation> allActiveVacationForPeriod = daoVacation.getAllActiveVacationForPeriod(dateFrom, dateTo);

        allVacationOfWeek.addAll(allActiveVacationForPeriod);

        return allVacationOfWeek.size();
    }

    @Override
    public int getCountEmployeeAvailableVacationDays(Employee employee, int year) throws DataStorageException {
        // количество дней отпуска - без даты + с датой Активные

        List<Vacation> allEmptyVacationDays = new ArrayList<>();
        List<Vacation> allEmptyVacationDays2 = daoVacation.getAllEmptyVacationDays(employee, year);

        allEmptyVacationDays.addAll(allEmptyVacationDays2);

        List<Vacation> allFactVacationsDays = new ArrayList<>();
        List<Vacation> allFactVacationDays2 = getAllFactVacationDays(employee, year);

        allFactVacationsDays.addAll(allFactVacationDays2);


        return allEmptyVacationDays.size() + allFactVacationsDays.size();
    }


    @Override
    public List<Vacation> getAllPlanVacationDays(Employee employee, int year) throws DataStorageException {

        List<Vacation> allEmployeeVacation = new ArrayList<>();
        List<Vacation> allEmployeeVacation2 = daoVacation.getAllEmployeeVacation(employee, year);

        allEmployeeVacation.addAll(allEmployeeVacation2);

        List<Vacation> allPlanEmployeeVacation = new ArrayList<Vacation>();

        if (allEmployeeVacation.size() != 0) {

            for (int i = 0; i < allEmployeeVacation.size(); i++) {
                if (allEmployeeVacation.get(i).getEventTag() == EventTag.PLAN) {

                    allPlanEmployeeVacation.add(allEmployeeVacation.get(i));
                }
            }
        }
        return allPlanEmployeeVacation;
    }

    @Override
    public List<Vacation> getAllFactVacationDays(Employee employee, int year) throws DataStorageException {

        List<Vacation> allEmployeeVacation = new ArrayList<>();
        List<Vacation> allEmployeeVacation2 = daoVacation.getAllEmployeeVacation(employee, year);

        allEmployeeVacation.addAll(allEmployeeVacation2);

        List<Vacation> allFactEmployeeVacation = new ArrayList<Vacation>();

        if (allEmployeeVacation.size() != 0) {

            for (int i = 0; i < allEmployeeVacation.size(); i++) {

                if (allEmployeeVacation.get(i).getStatusEvent() == StatusEvent.ACTIVE) {

                    allFactEmployeeVacation.add(allEmployeeVacation.get(i));
                }
            }
        }
        return allFactEmployeeVacation;
    }

    @Override
    public void removeEmptyVacation(Vacation vacation) throws VacationHaveDateException, DataStorageException {
        // только для отпусков без даты



        if(vacation.getDateOfEvent() == null){
            daoVacation.removeVacation(vacation);
            log.info("removeEmptyVacation");
        }else {

            throw new VacationHaveDateException("Can't remove this vacation. At first need to remove date from it");
        }
    }

    @Override
    public void removeVacationDate(Vacation vacation) throws VacationHaveDateException, DataStorageException {
        // сделать отпуск с датой просто отпуском без даты

            vacation.setDateOfEvent(null);
            daoVacation.modifyVacation(vacation);
        log.info("removeVacationDate - modifyVacation");

    }
}
