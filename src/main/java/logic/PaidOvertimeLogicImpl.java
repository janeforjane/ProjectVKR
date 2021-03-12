package logic;

import box.StatusEvent;
import dao.interfaces.DAOPaidOvertime;
import dao.exception.DataStorageException;
import entities.Employee;
import entities.PaidOvertime;
import logic.exception.DateIsBusyException;
import logic.interfaces.EventLogic;
import logic.interfaces.PaidOvertimeLogic;

import javax.ejb.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PaidOvertimeLogicImpl implements PaidOvertimeLogic {

    @EJB
    DAOPaidOvertime daoPaidOvertime;
    @EJB
    EventLogic eventLogic;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createPaidOvertime(Employee employee, LocalDate dateOfPaidOvertime) throws DateIsBusyException, DataStorageException {

        if(eventLogic.isDateFree(employee, dateOfPaidOvertime)){

            PaidOvertime paidOvertime = new PaidOvertime(employee, dateOfPaidOvertime);
            paidOvertime.setStatusEvent(StatusEvent.ACTIVE);
            daoPaidOvertime.newPaidOvertime(paidOvertime);

        }else {
            throw new DateIsBusyException("Date "+ dateOfPaidOvertime.toString()+" is busy");
        }

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void modifyCommentPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException {
        daoPaidOvertime.modifyPaidOvertime(paidOvertime);

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<PaidOvertime> getAllActivePaidOvertime(Employee employee, int year) throws DataStorageException {
        return daoPaidOvertime.getAllEmployeeActivePaidOvertime(employee,year);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public int getCountOfPaidOvertime(Employee employee, int year) throws DataStorageException {
        return daoPaidOvertime.getAllEmployeeActivePaidOvertime(employee,year).size();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public int getCountOfPaidOvertimeForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        return daoPaidOvertime.getAllPaidOvertimeForPeriod(dateFrom, dateTo).size();
    }

    @Override
    public List<PaidOvertime> getAllActivefPaidOvertimeForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        return daoPaidOvertime.getAllPaidOvertimeForPeriod(dateFrom, dateTo);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public int getCountOfPaidOvertimeForPeriodOfEmployee(Employee employee, LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        return daoPaidOvertime.getAllEmployeePaidOvertimeForPeriod(employee, dateFrom, dateTo).size();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void cancelPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException {
        paidOvertime.setStatusEvent(StatusEvent.NOTACTIVE);
        daoPaidOvertime.modifyPaidOvertime(paidOvertime);

    }
}
