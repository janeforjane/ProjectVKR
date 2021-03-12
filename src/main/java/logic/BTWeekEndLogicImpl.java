package logic;

import box.StatusEvent;
import dao.interfaces.DAOAbsence;
import dao.interfaces.DAOBTWeekEnd;
import dao.exception.DataStorageException;
import entities.Absence;
import entities.BusinessTripWeekEnd;
import entities.Employee;
import logic.exception.DateIsBusyException;
import logic.exception.ReasonAlreadyExistException;
import logic.interfaces.AbsenceLogic;
import logic.interfaces.BTWeekEndLogic;
import logic.interfaces.EventLogic;

import javax.ejb.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BTWeekEndLogicImpl implements BTWeekEndLogic {

    @EJB
    DAOBTWeekEnd daobtWeekEnd;
    @EJB
    DAOAbsence daoAbsence;
    @EJB
    EventLogic eventLogic;
    @EJB
    AbsenceLogic absenceLogic;


    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createBTWeekEndWithoutAbsence(Employee employee, LocalDate dateOfBTWeekEnd) throws DateIsBusyException, DataStorageException {
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
    @Transactional(Transactional.TxType.REQUIRED)
    public void createBTWeekEndWithAbsence(Employee employee, LocalDate dateOfBTWeekEnd, LocalDate dateOfAbsence) throws DateIsBusyException, DataStorageException {
        //проверить нет ли переработки на день командировки
        //проверить нет ли события на день отгула
        //создать командировку
        //создать отгул
        //связать командировку и отгул

        if (eventLogic.isDateFree(employee, dateOfBTWeekEnd) && eventLogic.isDateFree(employee, dateOfAbsence) ){

            BusinessTripWeekEnd businessTripWeekEnd = new BusinessTripWeekEnd(employee,dateOfBTWeekEnd);
            Absence absence = new Absence(employee, dateOfAbsence);
            businessTripWeekEnd.setStatusEvent(StatusEvent.ACTIVE);
            absence.setStatusEvent(StatusEvent.ACTIVE);


            absence.setReasonsOfAbsenceBusinessTrip(businessTripWeekEnd);

            daobtWeekEnd.newBTWeekEnd(businessTripWeekEnd);
            businessTripWeekEnd.setAbsenceForOvertime(absence);
            daoAbsence.newAbsence(absence);
            daobtWeekEnd.modifyBTWeekEnd(businessTripWeekEnd);

        }else {
            throw new DateIsBusyException("One of dates or both : "+ dateOfBTWeekEnd.toString()+" or "+ dateOfAbsence.toString() + "are busy");
        }

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void addAbsence(Employee employee, BusinessTripWeekEnd businessTripWeekEnd, Absence absence) throws ReasonAlreadyExistException, DataStorageException {
        /*
        проверить наличие absence у BT, если есть - выкинуть исключение
        проверить наличие BT у absence, если есть - выкинуть исключение
        если все пусто - связать между собой
         */

        if(businessTripWeekEnd.getAbsenceForOvertime() != null){
            throw new ReasonAlreadyExistException("businessTripWeekEnd " + businessTripWeekEnd.getID() + " at " +
                    businessTripWeekEnd.getDateOfEvent() + " already had an absence: at " + businessTripWeekEnd.getAbsenceForOvertime().getDateOfEvent());
        }

        if (absence.getReasonsOfAbsenceBusinessTrip() != null ){
            throw new ReasonAlreadyExistException("absence " + absence.getID() + " at " +
                    absence.getDateOfEvent() + " already had a BTTripWeekend in a reason of absence: at " + businessTripWeekEnd.getDateOfEvent());
        }

        absence.setReasonsOfAbsenceBusinessTrip(businessTripWeekEnd);
        businessTripWeekEnd.setAbsenceForOvertime(absence);

        daoAbsence.modifyAbsence(absence);
        daobtWeekEnd.modifyBTWeekEnd(businessTripWeekEnd);

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void modifyBTWeekendComment(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {
        daobtWeekEnd.modifyBTWeekEnd(businessTripWeekEnd);

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<BusinessTripWeekEnd> getAllActiveEmployeeBTWeekEnd(Employee employee, int year) throws DataStorageException {

        return daobtWeekEnd.getAllEmployeeActiveBTWeekEnd(employee, year);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public int getCountOfBTWeekEndDays(Employee employee, int year) throws DataStorageException {
        return daobtWeekEnd.getAllEmployeeActiveBTWeekEnd(employee, year).size();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void removeAbsenceFromBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {

        businessTripWeekEnd.setAbsenceForOvertime(null);
        daobtWeekEnd.modifyBTWeekEnd(businessTripWeekEnd);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void cancelBTWeekEnd(BusinessTripWeekEnd businessTripWeekEnd) throws DataStorageException {
        //деактивировать командировку
        //найти absence с ней связанный - его отвязать от этой
        //командировки - уведомление юзеру что если есть отгул то он будет отвязан от командировки


        Absence absenceForBusinessTripWeekEnd = businessTripWeekEnd.getAbsenceForOvertime();

        //убирается связь с командировкой у absence
        absenceLogic.removeReasonForAbsence(absenceForBusinessTripWeekEnd, businessTripWeekEnd);

        //убирается связь с absence у командировки, командировка деактивируется
        removeAbsenceFromBTWeekEnd(businessTripWeekEnd);
        businessTripWeekEnd.setStatusEvent(StatusEvent.NOTACTIVE);

        //скорректированная командировка передается в БД
        daobtWeekEnd.modifyBTWeekEnd(businessTripWeekEnd);

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
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
    @Transactional(Transactional.TxType.REQUIRED)
    public List<BusinessTripWeekEnd> getAllActiveBTWeekEnd(int year) throws DataStorageException {

        List<BusinessTripWeekEnd> allActiveBTWeekEnd = daobtWeekEnd.getAllActiveBTWeekEnd(year);
        return allActiveBTWeekEnd;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
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
    @Transactional(Transactional.TxType.REQUIRED)
    public Absence getAbsenceForOvertime(BusinessTripWeekEnd businessTripWeekEnd) {
        Absence absenceForOvertime = businessTripWeekEnd.getAbsenceForOvertime();

        return absenceForOvertime;
    }
}
