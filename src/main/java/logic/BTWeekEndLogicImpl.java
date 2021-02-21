package logic;

import box.StatusEvent;
import dao.interfaces.DAOBTWeekEnd;
import dao.exception.DataStorageException;
import entities.Absence;
import entities.BusinessTripWeekEnd;
import entities.Employee;
import logic.exception.DateIsBusyException;
import logic.interfaces.AbsenceLogic;
import logic.interfaces.BTWeekEndLogic;
import logic.interfaces.EventLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BTWeekEndLogicImpl implements BTWeekEndLogic {


    DAOBTWeekEnd daobtWeekEnd;
    EventLogic eventLogic;
    AbsenceLogic absenceLogic;


    @Override
    public void createBTWeekEnd(Employee employee, LocalDate dateOfBTWeekEnd) throws DateIsBusyException, DataStorageException {
        //проверить нет ли переработки на этот день
        //создать командировку

        if (eventLogic.isDateFree(employee, dateOfBTWeekEnd)){

            BusinessTripWeekEnd businessTripWeekEnd = new BusinessTripWeekEnd(employee,dateOfBTWeekEnd);
            businessTripWeekEnd.setStatusEvent(StatusEvent.ACTIVE);
            daobtWeekEnd.newBTWeekEnd(businessTripWeekEnd);

        }else {
            throw new DateIsBusyException("Date "+ dateOfBTWeekEnd.toString()+" is busy");
        }
    }

    @Override
    public void modifyBTWeekendComment(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {
        daobtWeekEnd.modifyBTWeekEnd(businessTripWeekEnd);

    }

    @Override
    public List<BusinessTripWeekEnd> getAllActiveBTWeekEnd(Employee employee, int year) throws DataStorageException {

        return daobtWeekEnd.getAllEmployeeActiveBTWeekEnd(employee, year);
    }

    @Override
    public int getCountOfBTWeekEndDays(Employee employee, int year) throws DataStorageException {
        return daobtWeekEnd.getAllEmployeeActiveBTWeekEnd(employee, year).size();
    }

    @Override
    public void removeAbsenceFromBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {

        businessTripWeekEnd.setAbsenceForOvertime(null);
        daobtWeekEnd.modifyBTWeekEnd(businessTripWeekEnd);
    }

    @Override
    public void cancelBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {
        //деактивировать командировку
        //найти absence с ней связанный - его отвязать от этой
        //командировки - уведомление юзеру что если есть отгул то он будет отвязан от командировки


        Absence absenceForBusinessTripWeekEnd = businessTripWeekEnd.getAbsenceForOvertime();

        //убирается связь с командировкой у absence
        absenceLogic.removeReasonForAbsence(absenceForBusinessTripWeekEnd);

        //убирается связь с absence у командировки, командировка деактивируется
        removeAbsenceFromBTWeekEnd(businessTripWeekEnd);
        businessTripWeekEnd.setStatusEvent(StatusEvent.NOTACTIVE);

        //скорректированная командировка передается в БД
        daobtWeekEnd.modifyBTWeekEnd(businessTripWeekEnd);

    }

    @Override
    public List<BusinessTripWeekEnd> getAllBTWeekEndWithAbsences(Employee employee, int year) throws DataStorageException {

        List<BusinessTripWeekEnd> allEmployeeActiveBTWeekEnd = daobtWeekEnd.getAllEmployeeActiveBTWeekEnd(employee, year);
        List<BusinessTripWeekEnd> allBTWeekEndWithAbsences = new ArrayList<>();

        for (int i = 0; i < allEmployeeActiveBTWeekEnd.size(); i++) {
            if(allEmployeeActiveBTWeekEnd.get(i).getAbsenceForOvertime() != null){

                allBTWeekEndWithAbsences.add(allEmployeeActiveBTWeekEnd.get(i));
            }
        }

        return allBTWeekEndWithAbsences;
    }

    @Override
    public List<BusinessTripWeekEnd> getAllBTWeekEndWithoutAbsences(Employee employee, int year) throws DataStorageException {
        List<BusinessTripWeekEnd> allEmployeeActiveBTWeekEnd = daobtWeekEnd.getAllEmployeeActiveBTWeekEnd(employee, year);
        List<BusinessTripWeekEnd> allBTWeekEndWithoutAbsences = new ArrayList<>();

        for (int i = 0; i < allEmployeeActiveBTWeekEnd.size(); i++) {
            if(allEmployeeActiveBTWeekEnd.get(i).getAbsenceForOvertime() == null){

                allBTWeekEndWithoutAbsences.add(allEmployeeActiveBTWeekEnd.get(i));
            }
        }

        return allBTWeekEndWithoutAbsences;
    }

    @Override
    public Absence getAbsenceForOvertime(BusinessTripWeekEnd businessTripWeekEnd) {
        Absence absenceForOvertime = businessTripWeekEnd.getAbsenceForOvertime();

        return absenceForOvertime;
    }
}
