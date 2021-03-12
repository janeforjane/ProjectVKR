package ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import ui.menuBarsForm.*;


public class MainLayout extends AppLayout {

    public MainLayout() {

        addToNavbar(new DrawerToggle());
        addToNavbar(new H2("Title"));

        VerticalLayout menuBar = new VerticalLayout();
//        menuBar.add(new RouterLink(MainForm.TITLE, MainForm.class));
//        menuBar.add(new RouterLink(HorizontalForm.TITLE, HorizontalForm.class));
//        menuBar.add(new RouterLink(EmployeeForm.TITLE, EmployeeForm.class));

        RouterLink emplFormlink = new RouterLink(EmployeesForm.TITLE, EmployeesForm.class);
        emplFormlink.setText("Сотрудники");
        menuBar.add(emplFormlink);

        RouterLink vacationFormlink = new RouterLink(VacationForm.TITLE, VacationForm.class);
        vacationFormlink.setText("Отпуск");
        menuBar.add(vacationFormlink);

        RouterLink sickFormlink = new RouterLink(SickdaysForm.TITLE, SickdaysForm.class);
        sickFormlink.setText("Больничные");
        menuBar.add(sickFormlink);

        RouterLink BTFormlink = new RouterLink(BusinessTripsForm.TITLE, BusinessTripsForm.class);
        BTFormlink.setText("Командировки");
        menuBar.add(BTFormlink);


        RouterLink OvertimesFormlink = new RouterLink(OvertimesForm.TITLE, OvertimesForm.class);
        OvertimesFormlink.setText("Переработки");
        menuBar.add(OvertimesFormlink);


        RouterLink AbsencesFormlink = new RouterLink(AbsencesForm.TITLE, AbsencesForm.class);
        AbsencesFormlink.setText("Отсутствия");
        menuBar.add(AbsencesFormlink);

//        RouterLink link = new RouterLink(VacationForm.TITLE, VacationForm.class);
//        link.setText("Vacations");
//        menuBar.add(link);

        //java:module/OvertimeLogicImpl
        //java:module/AbsenceLogicImpl
        //java:module/SickdayLogicImpl
        //java:module/BTWeekEndLogicImpl
        //java:module/VacationLogicImpl
        //java:module/EventLogicImpl
        //java:module/PaidOvertimeLogicImpl
        //java:module/BTWeekdayLogicImpl



        Integer l = 555;

//        RouterLink CurrentEmployeeFormlink = new RouterLink(CurrentEmployeeForm.TITLE, CurrentEmployeeForm.class, l);
//        link.setText("CurrentEmployee");
//        menuBar.add(CurrentEmployeeFormlink);

        addToDrawer(menuBar);

    }
}
