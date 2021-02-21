package logic;

import box.StatusEvent;
import dao.interfaces.DAOOvertime;
import dao.exception.DataStorageException;
import entities.Absence;
import entities.Employee;
import entities.Overtime;
import logic.exception.DateIsBusyException;
import logic.interfaces.AbsenceLogic;
import logic.interfaces.EventLogic;
import logic.interfaces.OvertimeLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OvertimeLogicImpl implements OvertimeLogic {

    DAOOvertime daoOvertime;
    EventLogic eventLogic;
    AbsenceLogic absenceLogic;

    @Override
    public void createOvertime(Employee employee, LocalDate dateOfOvertime) throws DateIsBusyException, DataStorageException {
        //проверить нет ли переработки на этот день
        //проверить выходной ли это
        //создать командировку

        if (eventLogic.isDateFree(employee, dateOfOvertime)){

            Overtime overtime = new Overtime(employee,dateOfOvertime);
            overtime.setStatusEvent(StatusEvent.ACTIVE);
            daoOvertime.newOvertime(overtime);

        }else {

            throw new DateIsBusyException("Date "+ dateOfOvertime.toString()+" is busy");
        }

    }

    @Override
    public void modifyOvertimeComment(Overtime overtime) throws DataStorageException {
        daoOvertime.modifyOvertime(overtime);

    }

    @Override
    public void removeAbsenceFromOvertime(Overtime overtime) throws DataStorageException {

        overtime.setAbsenceForOvertime(null);
        daoOvertime.modifyOvertime(overtime);
    }

    @Override
    public Absence getAbsenceForOvertime(Overtime overtime) {
        Absence absenceForOvertime = overtime.getAbsenceForOvertime();

        return absenceForOvertime;
    }

    @Override
    public List<Overtime> getAllActiveOvertimes(Employee employee, int year) throws DataStorageException {

        return daoOvertime.getAllEmployeeActiveOvertimeDays(employee, year);
    }

    @Override
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
    public int getCountOfOvertimeDays(Employee employee, int year) throws DataStorageException {
        return daoOvertime.getAllEmployeeActiveOvertimeDays(employee, year).size();
    }

    @Override
    public int getCountOfOvertimesForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        daoOvertime.getAllActiveOvertimeDaysForPeriod(dateFrom, dateTo);
        return 0;
    }

    @Override
    public void cancelOvertime(Overtime overtime) throws DataStorageException {
        //деактивировать переработку
        //найти absence с ней связанный - его отвязать от этой
        //переработки - уведомление юзеру что если есть отгул то он будет отвязан от переработки


        Absence absenceForBusinessTripWeekEnd = overtime.getAbsenceForOvertime();

        //убирается связь с переработкой у absence
        absenceLogic.removeReasonForAbsence(absenceForBusinessTripWeekEnd);

        //убирается связь с absence у переработки, переработка деактивируется
        removeAbsenceFromOvertime(overtime);
        overtime.setStatusEvent(StatusEvent.NOTACTIVE);

        //скорректированная переработка передается в БД
        daoOvertime.modifyOvertime(overtime);

    }
}
