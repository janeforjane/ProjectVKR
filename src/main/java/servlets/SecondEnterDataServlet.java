package servlets;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOEmployee;
import dao.interfaces.DAOPaidOvertime;
import dao.interfaces.DAOSickday;
import dao.interfaces.DAOVacation;
import entities.Employee;
import entities.Sickday;
import entities.Vacation;
import logic.exception.DateIsBusyException;
import logic.exception.DuplicateFIOException;
import logic.exception.EmployeeNotFoundException;
import logic.exception.EventNotFoundException;
import logic.interfaces.EmployeeLogic;
import logic.interfaces.EventLogic;
import logic.interfaces.VacationLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "SecondEnterDataServlet", urlPatterns = "/secondEnterDataServlet")
public class SecondEnterDataServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SecondEnterDataServlet.class);

    @EJB
    DAOPaidOvertime daoPaidOvertime;
    @EJB
    DAOSickday daoSickday;
    @EJB
    DAOEmployee daoEmployee;
    @EJB
    EmployeeLogic employeeLogic;
    @EJB
    VacationLogic vacationLogic;
    @EJB
    EventLogic eventLogic;
    @EJB
    DAOVacation daoVacation;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        LocalDate vacationDate = LocalDate.of(2020,8,1);
        LocalDate vacationDate2 = LocalDate.of(2020,8,31);
        try {
            Optional<Employee> employee = employeeLogic.getEmployee(2L);
            int countOfVacations = vacationLogic.getAllFactVacationDays(employee.get(),2020).size();

            log.info("count is - {}", countOfVacations);
        } catch (DataStorageException e) {
            e.printStackTrace();
        }

//        try {
//            Optional<Employee> employee = employeeLogic.getEmployee(2L);
//            LocalDate vacationDate = LocalDate.of(2020,12,2);
//            List<Vacation> allEmptyVacationDays = new ArrayList<>();
//            List<Vacation> allEmptyVacationDays2 = vacationLogic.getAllPlanVacationDays(employee.get(), 2020);
//            allEmptyVacationDays.addAll(allEmptyVacationDays2);
//
//            vacationLogic.replaceVacation(employee.get(), allEmptyVacationDays.get(0),vacationDate);
//
//            log.info("----Size - " + allEmptyVacationDays.size());
//        } catch (DataStorageException | DateIsBusyException e) {
//            e.printStackTrace();
//        }


        //create vacation
//        try {
//            Optional<Employee> employee = employeeLogic.getEmployee(2L);
//
//            LocalDate vacationDate = LocalDate.of(2020,12,11);
//            Vacation vacation = new Vacation(employee.get(), vacationDate);
//            vacation.setComment("PPPP");
////            Vacation vacation = new Vacation(employee.get(), 2020);
//
//
//            vacationLogic.createPlanVacation(vacation);
//
//        } catch (DateIsBusyException ev) {
//            log.info("Date is busy");
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }




        //create employee

//        String name = "Morticia";
//        String secondName = "K";
//        String lastName = "Addams";
//        LocalDate entryDate = LocalDate.of(2000,6,17);
//        String position = "millionaire's  wife";
//
//        try {
//            employeeLogic.createEmployee(name, secondName, lastName, entryDate, position);
//        } catch (DuplicateFIOException e) {
//            e.printStackTrace();
//            System.out.println("DuplicateFIOException");
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//            System.out.println("DataStorageException");
//        }

        //modify employee

//        try {
//            Employee employee = employeeLogic.getEmployee(2L);
//            employee.setPosition("superman");
//            employeeLogic.modifyEmployee(employee);
//        } catch (DataStorageException | EmployeeNotFoundException e) {
//            e.printStackTrace();
//        }


        //-----getAllActiveSickdays --- ok

//        List<Sickday> allActiveSickdays = null;
//        try {
//            allActiveSickdays = daoSickday
//                    .getAllActiveSickdaysForPeriod(LocalDate.of(2021,1,1),LocalDate.of(2021,12,1));
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }
//
//        for (Sickday sickday : allActiveSickdays) {
//            log.info("Sickdays: {}", sickday);
//        }

//            for (int i = 0; i < allActiveSickdays.size(); i++) {
//                log.info("Sickdays: {}", allActiveSickdays.get(i));
//            }




        //-----getAllActiveSickdays --- ok
//        try {
//            List<Sickday> allActiveSickdays = daoSickday.getAllActiveSickdays();
//
//            for (int i = 0; i < allActiveSickdays.size(); i++) {
//                log.info("Sickdays: {}", allActiveSickdays.get(i));
//            }
//
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }

        //-----modify --- ok
//        try {
//            Sickday sickdaySample = new Sickday();
//            sickdaySample.setID(3L);
//
//            Sickday sickday = daoSickday.getSickday(sickdaySample);
//            sickday.setComment("modifyComment");
//            sickday.setStatusEvent(StatusEvent.ACTIVE);
//
//            daoSickday.modifySickday(sickday);
//            log.info("Sickday: {}", sickday);
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }

        //-----new & get one --- ok
//        Sickday newSickday = new Sickday();
//        newSickday.setComment("sssss");
//
//        try {
//            daoSickday.newSickday(newSickday);
//            daoSickday.getSickday(newSickday);
//            log.info("Sickday: {}", newSickday);
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }


//        try {
//            Sickday sickday1 = daoSickday.getSickday(newSickday);
//            log.info("Vacation: {}", sickday1);
//
//
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }

    }
}
