package logic;

import box.StatusEvent;
import dao.interfaces.DAOPaidOvertime;
import dao.exception.DataStorageException;
import entities.Employee;
import entities.PaidOvertime;
import logic.exception.DateIsBusyException;
import logic.interfaces.EventLogic;
import logic.interfaces.PaidOvertimeLogic;

import java.time.LocalDate;
import java.util.List;

public class PaidOvertimeLogicImpl implements PaidOvertimeLogic {

    DAOPaidOvertime daoPaidOvertime;
    EventLogic eventLogic;

    @Override
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
    public void modifyCommentPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException {
        daoPaidOvertime.modifyPaidOvertime(paidOvertime);

    }

    @Override
    public List<PaidOvertime> getAllActivePaidOvertime(Employee employee, int year) throws DataStorageException {
        return daoPaidOvertime.getAllEmployeeActivePaidOvertime(employee,year);
    }

    @Override
    public int getCountOfPaidOvertime(Employee employee, int year) throws DataStorageException {
        return daoPaidOvertime.getAllEmployeeActivePaidOvertime(employee,year).size();
    }

    @Override
    public int getCountOfPaidOvertimeForPeriod(LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        return daoPaidOvertime.getAllPaidOvertimeForPeriod(dateFrom, dateTo).size();
    }

    @Override
    public int getCountOfPaidOvertimeForPeriodOfEmployee(Employee employee, LocalDate dateFrom, LocalDate dateTo) throws DataStorageException {
        return daoPaidOvertime.getAllEmployeePaidOvertimeForPeriod(employee, dateFrom, dateTo).size();
    }

    @Override
    public void cancelPaidOvertime(PaidOvertime paidOvertime) throws DataStorageException {
        paidOvertime.setStatusEvent(StatusEvent.NOTACTIVE);
        daoPaidOvertime.modifyPaidOvertime(paidOvertime);

    }
}
