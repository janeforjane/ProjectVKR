package logic;

import box.StatusEvent;
import dao.interfaces.DAOAbsence;
import dao.exception.DataStorageException;
import entities.*;
import logic.exception.DateIsBusyException;
import logic.exception.ReasonAlreadyExistException;
import logic.interfaces.AbsenceLogic;
import logic.interfaces.BTWeekEndLogic;
import logic.interfaces.EventLogic;
import logic.interfaces.OvertimeLogic;

import javax.ejb.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AbsenceLogicImpl implements AbsenceLogic {

    @EJB
    EventLogic eventLogic;
    @EJB
    DAOAbsence daoAbsence;
    @EJB
    BTWeekEndLogic btWeekEndLogic;
    @EJB
    OvertimeLogic overtimeLogic;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createAbsence(Employee employee, LocalDate dateOfAbsence) throws DateIsBusyException, DataStorageException {

        if (eventLogic.isDateFree(employee, dateOfAbsence)){

            Absence absence = new Absence(employee, dateOfAbsence);
            absence.setStatusEvent(StatusEvent.ACTIVE);
            daoAbsence.newAbsence(absence);
        }else {
            throw new DateIsBusyException("Date "+ dateOfAbsence.toString()+" is busy");
        }
    }

    @Override
    public void modifyAbsenceComment(Absence absence) throws DataStorageException {
        daoAbsence.modifyAbsence(absence);
    }

    @Override
    public void removeReasonForAbsence(Absence absence, CommonOvertime commonOvertime) throws DataStorageException {

        if (commonOvertime instanceof Overtime){

            absence.setReasonsOfAbsenceOvertime(null);
        }else {

            absence.setReasonsOfAbsenceBusinessTrip(null);
        }

        daoAbsence.modifyAbsence(absence);
        //todo ИСКЛЮЧЕНИЕ если нечего удалять

    }

    @Override
    public void addReasonOfAbsence(Absence absence, CommonOvertime additionalCommonOvertime) throws DataStorageException, ReasonAlreadyExistException {

        if (absence.getReasonsOfAbsenceOvertime() == null) {
            if(additionalCommonOvertime instanceof Overtime){
                absence.setReasonsOfAbsenceOvertime((Overtime) additionalCommonOvertime);
            }else {
                absence.setReasonsOfAbsenceBusinessTrip((BusinessTripWeekEnd) additionalCommonOvertime);
            }
            daoAbsence.modifyAbsence(absence);

        }else {

            throw new ReasonAlreadyExistException("Reason of absence is already exist");
        }
    }


    @Override
    public List<Absence> getAllActiveAbsenceDays(Employee employee, int year) throws DataStorageException {

        List<Absence> allActiveEmployeeAbsences = daoAbsence.getAllEmployeeActiveAbsences(employee, year);

        return allActiveEmployeeAbsences;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public int getCountOfAbsencesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {

        return daoAbsence.getAllActiveAbsencesForPeriod(dateFrom, dateTo).size();
    }

    @Override
    public List<Absence> getAllAbsenceDaysWithReasons(Employee employee, int year) throws DataStorageException {

        List<Absence> allActiveEmployeeAbsences = daoAbsence.getAllEmployeeActiveAbsences(employee, year);
        List<Absence> allAbsenceDaysWithReasons = allActiveEmployeeAbsences.stream()
                .filter(e -> e.getReasonsOfAbsenceOvertime() != null)
                .collect(Collectors.toList());

        return allAbsenceDaysWithReasons;

//        List<Absence> allAbsenceDaysWithReasons = new ArrayList<>();
//
//        for (int i = 0; i < allActiveEmployeeAbsences.size(); i++) {
//            if(allActiveEmployeeAbsences.get(i).getReasonsOfAbsence() != null){
//                allAbsenceDaysWithReasons.add(allActiveEmployeeAbsences.get(i));
//            }
//        }
//
//        if(allAbsenceDaysWithReasons.size()>0){
//            return allAbsenceDaysWithReasons;
//        }else {
//            return Collections.EMPTY_LIST;
//        }

    }

    @Override
    public List<Absence> getAllAbsenceDaysWithoutReasons(Employee employee, int year) throws DataStorageException {
        List<Absence> allActiveEmployeeAbsences = daoAbsence.getAllEmployeeActiveAbsences(employee, year);
//        List<Absence> allAbsenceDaysWithoutReasons = new ArrayList<>();

        List<Absence> allAbsenceDaysWithoutReasons = allActiveEmployeeAbsences.stream()
                .filter(e -> e.getReasonsOfAbsenceOvertime() == null)
                .collect(Collectors.toList());


//        for (int i = 0; i < allActiveEmployeeAbsences.size(); i++) {
//            if(allActiveEmployeeAbsences.get(i).getReasonsOfAbsence() == null){
//                allAbsenceDaysWithoutReasons.add(allActiveEmployeeAbsences.get(i));
//            }
//        }

        return allAbsenceDaysWithoutReasons;

    }

    @Override
    public int getCountOfEmployeeAbsenceDays(Employee employee, int year) throws DataStorageException {
        return getAllActiveAbsenceDays(employee, year).size();

    }

    @Override
    public void cancelAbsence(Absence absence) throws DataStorageException {
        //получить день-переработка за этот absence и у него убрать ссылку на этот absence
        CommonOvertime reasonOfAbsence = absence.getReasonsOfAbsenceOvertime();
        reasonOfAbsence.setAbsenceForOvertime(null);

        //сделать absence не активным и сохранить в БД
        absence.setStatusEvent(StatusEvent.NOTACTIVE);
        daoAbsence.modifyAbsence(absence);

        //сохранить день-переработка в новом виде без absence
        if (reasonOfAbsence instanceof BusinessTripWeekEnd) {

            btWeekEndLogic.removeAbsenceFromBTWeekEnd((BusinessTripWeekEnd) reasonOfAbsence);
        }else if(reasonOfAbsence instanceof Overtime){
            overtimeLogic.removeAbsenceFromOvertime((Overtime) reasonOfAbsence);
        }
    }
}
