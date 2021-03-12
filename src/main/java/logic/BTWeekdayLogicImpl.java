package logic;

import box.StatusEvent;
import dao.interfaces.DAOBTWeekday;
import dao.exception.DataStorageException;
import entities.BusinessTripWeekday;
import entities.Employee;
import logic.exception.DateIsBusyException;
import logic.interfaces.BTWeekdayLogic;
import logic.interfaces.EventLogic;

import javax.ejb.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BTWeekdayLogicImpl implements BTWeekdayLogic {
    @EJB
    DAOBTWeekday daobtWeekday;
    @EJB
    EventLogic eventLogic;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createBTWeekday(Employee employee, LocalDate dateOfBTWeekday) throws DateIsBusyException, DataStorageException {

        if (eventLogic.isDateFree(employee, dateOfBTWeekday)){

            BusinessTripWeekday businessTripWeekday = new BusinessTripWeekday(employee, dateOfBTWeekday);
            businessTripWeekday.setStatusEvent(StatusEvent.ACTIVE);
//            businessTripWeekday.setYear(dateOfBTWeekday.getYear());
            daobtWeekday.newBTWeekday(businessTripWeekday);

        }else {
            throw new DateIsBusyException("Date "+ dateOfBTWeekday.toString()+" is busy");
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void modifyCommentBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException {
        daobtWeekday.modifyBTWeekday(businessTripWeekday);

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<BusinessTripWeekday> getAllActiveEmployeeBTWeekday(Employee employee, int year) throws DataStorageException {

        List<BusinessTripWeekday> businessTripActiveWeekdays = daobtWeekday.getAllEmployeeActiveBTWeekdays(employee, year);

        return businessTripActiveWeekdays;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<BusinessTripWeekday> getAllActiveBTWeekday(int year) throws DataStorageException {
        return daobtWeekday.getAllBTWeekdaysForPeriod(LocalDate.of(year,1,1),LocalDate.of(year,12,31));
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public int getCountOfActiveEmployeeBTWeekday(Employee employee, int year) throws DataStorageException {

        List<BusinessTripWeekday> businessTripActiveWeekdays = daobtWeekday.getAllEmployeeActiveBTWeekdays(employee, year);
        return businessTripActiveWeekdays.size();
    }

//    @Override
//    public int getCountOfActiveBTForPeriod(LocalDate dateFrom, LocalDate dateTo) {
//        return daobtWeekday.getAllBTWeekdaysForPeriod(dateFrom, dateTo).size();
//    }

//    @Override
//    public int getCountOfBusinessTripsForPeriodOfEmployee(Employee employee, LocalDate dateFrom, LocalDate dateTo) {
//        //нужна ли эта инфа?
//        return daobtWeekday.getAllEmployeeBTWeekdaysForPeriod(employee, dateFrom,dateTo).size();
//    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void cancelBTWeekday(BusinessTripWeekday businessTripWeekday) throws DataStorageException {
        businessTripWeekday.setStatusEvent(StatusEvent.NOTACTIVE);
        daobtWeekday.modifyBTWeekday(businessTripWeekday);

    }
}
