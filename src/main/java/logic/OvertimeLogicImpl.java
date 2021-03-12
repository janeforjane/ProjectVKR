package logic;

import box.StatusEvent;
import dao.interfaces.DAOAbsence;
import dao.interfaces.DAOOvertime;
import dao.exception.DataStorageException;
import entities.Absence;
import entities.BusinessTripWeekEnd;
import entities.Employee;
import entities.Overtime;
import logic.exception.DateIsBusyException;
import logic.exception.ReasonAlreadyExistException;
import logic.interfaces.AbsenceLogic;
import logic.interfaces.EventLogic;
import logic.interfaces.OvertimeLogic;

import javax.ejb.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OvertimeLogicImpl implements OvertimeLogic {

    @EJB
    DAOOvertime daoOvertime;
    @EJB
    EventLogic eventLogic;
    @EJB
    AbsenceLogic absenceLogic;
    @EJB
    DAOAbsence daoAbsence;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createOvertimeWithoutAbsence(Employee employee, LocalDate dateOfOvertime) throws DateIsBusyException, DataStorageException {
        //проверить нет ли события на этот день
        //проверить выходной ли это
        //создать переработку

        if (eventLogic.isDateFree(employee, dateOfOvertime)){

            Overtime overtime = new Overtime(employee,dateOfOvertime);
            overtime.setStatusEvent(StatusEvent.ACTIVE);
            daoOvertime.newOvertime(overtime);

        }else {

            throw new DateIsBusyException("Date "+ dateOfOvertime.toString()+" is busy");
        }

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createOvertimeWithAbsence(Employee employee, LocalDate dateOfOvertime, LocalDate dateOfAbsence) throws DateIsBusyException, DataStorageException {

        //проверить нет ли командировки на день переработки
        //проверить нет ли события на день отгула
        //создать переработку
        //создать отгул
        //связать переработку и отгул

        if (eventLogic.isDateFree(employee, dateOfOvertime)
                && eventLogic.isDateFree(employee, dateOfAbsence) ){

            Overtime overtime = new Overtime(employee,dateOfOvertime);
            Absence absence = new Absence(employee, dateOfAbsence);
            overtime.setStatusEvent(StatusEvent.ACTIVE);
            absence.setStatusEvent(StatusEvent.ACTIVE);


            absence.setReasonsOfAbsenceOvertime(overtime);

            daoOvertime.newOvertime(overtime);
            overtime.setAbsenceForOvertime(absence);
            daoAbsence.newAbsence(absence);
            daoOvertime.modifyOvertime(overtime);

        }else {
            throw new DateIsBusyException("One of dates or both : "+ dateOfOvertime.toString()+" or "+ dateOfAbsence.toString() +"are busy");
        }

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void addAbsence(Employee employee, Overtime overtime, Absence absence) throws ReasonAlreadyExistException, DataStorageException {

        /*
        проверить наличие absence у BT, если есть - выкинуть исключение
        проверить наличие BT у absence, если есть - выкинуть исключение
        если все пусто - связать между собой
         */

        if(overtime.getAbsenceForOvertime() != null){
            throw new ReasonAlreadyExistException("businessTripWeekEnd " + overtime.getID() + " at " +
                    overtime.getDateOfEvent() + " already had an absence: at " + overtime.getAbsenceForOvertime().getDateOfEvent());
        }

        if (absence.getReasonsOfAbsenceBusinessTrip() != null ){
            throw new ReasonAlreadyExistException("absence " + absence.getID() + " at " +
                    absence.getDateOfEvent() + " already had a BTTripWeekend in a reason of absence: at " + overtime.getDateOfEvent());
        }

        absence.setReasonsOfAbsenceOvertime(overtime);
        overtime.setAbsenceForOvertime(absence);

        daoAbsence.modifyAbsence(absence);
        daoOvertime.modifyOvertime(overtime);

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void modifyOvertimeComment(Overtime overtime) throws DataStorageException {
        daoOvertime.modifyOvertime(overtime);

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void removeAbsenceFromOvertime(Overtime overtime) throws DataStorageException {

        overtime.setAbsenceForOvertime(null);
        daoOvertime.modifyOvertime(overtime);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Absence getAbsenceForOvertime(Overtime overtime) {
        Absence absenceForOvertime = overtime.getAbsenceForOvertime();

        return absenceForOvertime;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<Overtime> getAllActiveOvertimes(Employee employee, int year) throws DataStorageException {

        return daoOvertime.getAllEmployeeActiveOvertimeDays(employee, year);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<Overtime> getAllOvertimesWithAbsences(Employee employee, int year) throws DataStorageException {
        List<Overtime> overtimes = daoOvertime.getAllEmployeeActiveOvertimeDays(employee, year);
        List<Overtime> overtimesWithAbsences = new ArrayList<>();

        for (int i = 0; i < overtimes.size(); i++) {
            if(overtimes.get(i).getAbsenceForOvertime() !=null) {
                overtimesWithAbsences.add(overtimes.get(i));
            }
        }

        return overtimesWithAbsences;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<Overtime> getAllOvertimesWithoutAbsences(Employee employee, int year) throws DataStorageException {
        List<Overtime> overtimes = daoOvertime.getAllEmployeeActiveOvertimeDays(employee, year);
        List<Overtime> overtimesWithAbsences = new ArrayList<>();

        for (int i = 0; i < overtimes.size(); i++) {
            if(overtimes.get(i).getAbsenceForOvertime() ==null) {
                overtimesWithAbsences.add(overtimes.get(i));
            }
        }

        return overtimesWithAbsences;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public int getCountOfOvertimeDays(Employee employee, int year) throws DataStorageException {
        return daoOvertime.getAllEmployeeActiveOvertimeDays(employee, year).size();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public int getCountOfOvertimesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        int size = daoOvertime.getAllActiveOvertimeDaysForPeriod(dateFrom, dateTo).size();
        return size;
    }

    @Override
    public List<Overtime> getAllActiveOvertimesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {

        return daoOvertime.getAllActiveOvertimeDaysForPeriod(dateFrom, dateTo);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void cancelOvertime(Overtime overtime) throws DataStorageException {
        //деактивировать переработку
        //найти absence с ней связанный - его отвязать от этой
        //переработки - уведомление юзеру что если есть отгул то он будет отвязан от переработки


        Absence absenceForBusinessTripWeekEnd = overtime.getAbsenceForOvertime();

        //убирается связь с переработкой у absence
        absenceLogic.removeReasonForAbsence(absenceForBusinessTripWeekEnd, overtime);

        //убирается связь с absence у переработки, переработка деактивируется
        removeAbsenceFromOvertime(overtime);
        overtime.setStatusEvent(StatusEvent.NOTACTIVE);

        //скорректированная переработка передается в БД
        daoOvertime.modifyOvertime(overtime);

    }
}
