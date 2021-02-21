package servlets;

import box.EventTag;
import box.StatusEvent;
import dao.interfaces.DAOEmployee;
import dao.interfaces.DAOVacation;
import dao.exception.DataStorageException;
import entities.Employee;
import entities.Vacation;
import logic.EmployeeLogicImpl;
import logic.interfaces.EmployeeLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "EnterDataServlet", urlPatterns = "/enterDataServlet")
public class EnterDataServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(EnterDataServlet.class);

    @EJB
    EmployeeLogic employeeLogic;

    @EJB
    DAOVacation daoVacation;

    @EJB
    DAOEmployee daoEmployee;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //-------ok
//        try {
//            for (Employee employee : daoEmployee.getAllEmployees()) {
//                log.info("Employee: {}", employee);
//
//                List<Vacation> allEmployeeVacation
//                        = daoVacation.getAllEmployeeVacation(employee, 2020);
//                log.info("List<Vacation>: {}", allEmployeeVacation.get(0));
//            }
//        } catch (DataStorageException e) {
//
//            log.error("Error: " + e.getMessage());
//            e.printStackTrace();
//        }



        //-------ok
//        Vacation newVacation = new Vacation();
//        newVacation.setID(3L);
//        newVacation.setStatusEvent(StatusEvent.ACTIVE);
//        newVacation.setEventTag(null);
//
//        try {
//            Vacation vacation1 = daoVacation.getVacation(newVacation);
//            log.info("Vacation: {}", vacation1);
//
//
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }


        //-------ok
//        Vacation newVacation = new Vacation();
//        newVacation.setID(2L);
//
//        try {
//            Vacation vacation1 = daoVacation.getVacation(newVacation);
//            log.info("Vacation: {}", vacation1);
//
//
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }




        //-------bad value for type int

//        try {
//            for (Vacation vacation : daoVacation.getAllVacation()) {
//                log.info("Vacation: {}", vacation);
//
//            }
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }

        //----new vacation---ok

//        try {
//            for (Employee employee : daoEmployee.getAllEmployees()) {
//                log.info("Employee: {}", employee);
//                Vacation newVacation = new Vacation(employee, 2020);
//                daoVacation.newVacation(newVacation);
//            }
//        } catch (DataStorageException e) {
//
//            log.error("Error: " + e.getMessage());
//            e.printStackTrace();
//        }


        //-------ok

//        String name = req.getParameter("name");
//        String secondName = req.getParameter("second_name");
//        String lastName = req.getParameter("last_name");
//        LocalDate entryDate = LocalDate.of(2015,3,1);
//        String position = "killer";
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

    }
}
