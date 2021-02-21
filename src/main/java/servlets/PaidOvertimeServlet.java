package servlets;

import box.StatusEvent;
import dao.exception.DataStorageException;
import dao.interfaces.DAOEmployee;
import dao.interfaces.DAOPaidOvertime;
import dao.interfaces.DAOSickday;
import entities.Employee;
import entities.PaidOvertime;
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

@WebServlet(name = "PaidOvertimeServlet", urlPatterns = "/paidOvertimeServlet")
public class PaidOvertimeServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(PaidOvertimeServlet.class);

    @EJB
    DAOPaidOvertime daoPaidOvertime;
    @EJB
    DAOEmployee daoEmployee;

    @EJB
    DAOSickday daoSickday;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {















        //-----PaidOvertime
        //------ok
//        try {
//            List<Employee> allEmployees = daoEmployee.getAllEmployees();
//
//            List<PaidOvertime> allEmployeeActivePaidOvertime = daoPaidOvertime
//                    .getAllEmployeeActivePaidOvertime(allEmployees.get(0), 2020);
//
//            PaidOvertime paidOvertime = allEmployeeActivePaidOvertime.get(0);
//            paidOvertime.setComment("new comment for modify");
//
//            daoPaidOvertime.modifyPaidOvertime(paidOvertime);
//
//            log.info("Modify PaidOvertime: {}", paidOvertime);
//
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }


        //-----ok

//        try {
//
//            List<Employee> allEmployees = daoEmployee.getAllEmployees();
//
//            PaidOvertime paidOvertime1 = new PaidOvertime();
//            paidOvertime1.setStatusEvent(StatusEvent.ACTIVE);
//            paidOvertime1.setComment("newPaidOvertimeCommment");
//            paidOvertime1.setEmployee(allEmployees.get(0));
//
//            daoPaidOvertime.newPaidOvertime(paidOvertime1);
//            log.info("PaidOvertime new1: {}", paidOvertime1);
//
//            PaidOvertime paidOvertime2 = new PaidOvertime();
//            paidOvertime2.setStatusEvent(StatusEvent.ACTIVE);
//            paidOvertime2.setComment("Another newPaidOvertimeCommment");
//            paidOvertime2.setEmployee(allEmployees.get(0));
//
//            daoPaidOvertime.newPaidOvertime(paidOvertime2);
//            log.info("PaidOvertime new2: {}", paidOvertime2);
//
//
//            List<PaidOvertime> allEmployeeActivePaidOvertime = daoPaidOvertime.getAllEmployeeActivePaidOvertime(allEmployees.get(0), 2020);
//            for (int i = 0; i < allEmployeeActivePaidOvertime.size(); i++) {
//                log.info("allEmployeeActivePaidOvertimes: {}", allEmployeeActivePaidOvertime.get(i));
//            }
//
//        } catch (DataStorageException e) {
//            e.printStackTrace();
//        }


    }
}
