package ui.employeeForms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import dao.exception.DataStorageException;
import entities.*;
import logic.interfaces.*;
import ui.MyMainForm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = CurrentEmployeeForm.ROUTE)
@Theme(value = Material.class, variant = Material.DARK)
public class CurrentEmployeeForm extends Div implements HasUrlParameter<Integer> {

    public static final String ROUTE = "CurrentEmployeeForm";
    public static final String TITLE = "CurrentEmployeeForm";
    private final Header currentEmployeeLabel;
    private EmployeeLogic employeeLogic;
    private VacationLogic vacationLogic;
    private SickdayLogic sickdayLogic;
    private BTWeekdayLogic btWeekdayLogic;
    private BTWeekEndLogic btWeekEndLogic;
    private OvertimeLogic overtimeLogic;
    private PaidOvertimeLogic paidOvertimeLogic;
    private AbsenceLogic absenceLogic;

    int employeeId;

    //ui components
    VerticalLayout headerLabel = new VerticalLayout();

    Grid<Vacation> vacationGrid = new Grid<>(Vacation.class, false);
    Tab tab1 = new Tab("Отпуск");
    Div page1 = new Div();
    Grid<Sickday> sickdayGrid = new Grid<>(Sickday.class,false);
    Tab tab2 = new Tab("Больничные");
    Div page2 = new Div();
//    Grid<BusinessTripWeekday> businessTripWeekdayGrid = new Grid<>(BusinessTripWeekday.class,false);
    Grid<Event> eventBTGrid = new Grid<>(Event.class,false);
    Tab tab3 = new Tab("Командировки");
    Div page3 = new Div();
    Grid<Event> eventOverGrid = new Grid<>(Event.class,false);
    Tab tab4 = new Tab("Переработки");
    Div page4 = new Div();
    Grid<Absence> absenceGrid = new Grid<>(Absence.class,false);
    Tab tab5 = new Tab("Отсутствия");
    Div page5 = new Div();

    VerticalLayout bottomLayer = new VerticalLayout();



    public CurrentEmployeeForm() {


        //java:module/VacationLogicImpl
        //java:module/EventLogicImpl
        //java:module/PaidOvertimeLogicImpl
        //java:module/BTWeekdayLogicImpl
        //java:module/BTWeekEndLogicImpl
        //java:module/OvertimeLogicImpl
        //java:module/AbsenceLogicImpl
        //java:module/SickdayLogicImpl

        //ejb

        try {
            InitialContext initialContext = new InitialContext();
            employeeLogic = (EmployeeLogic) initialContext.lookup("java:module/EmployeeLogicImpl");
            vacationLogic = (VacationLogic) initialContext.lookup("java:module/VacationLogicImpl");
            sickdayLogic = (SickdayLogic) initialContext.lookup("java:module/SickdayLogicImpl");
            btWeekdayLogic = (BTWeekdayLogic) initialContext.lookup("java:module/BTWeekdayLogicImpl");
            btWeekEndLogic = (BTWeekEndLogic) initialContext.lookup("java:module/BTWeekEndLogicImpl");
            btWeekEndLogic = (BTWeekEndLogic) initialContext.lookup("java:module/BTWeekEndLogicImpl");
            overtimeLogic = (OvertimeLogic) initialContext.lookup("java:module/OvertimeLogicImpl");
            paidOvertimeLogic = (PaidOvertimeLogic) initialContext.lookup("java:module/PaidOvertimeLogicImpl");
            paidOvertimeLogic = (PaidOvertimeLogic) initialContext.lookup("java:module/PaidOvertimeLogicImpl");
            absenceLogic = (AbsenceLogic) initialContext.lookup("java:module/AbsenceLogicImpl");

        } catch (NamingException e) {
            e.printStackTrace();
        }

//        vacationGrid.


        //header
        headerLabel.setMargin(true);
        headerLabel.setPadding(true);

        Header header = new Header();
        header.setText("Страница сотрудника");
        header.getStyle().set("color", "#9370DB");
        header.getStyle().set("font-size", "large");

        currentEmployeeLabel = new Header();

        currentEmployeeLabel.getStyle().set("color", "#1E90FF");
        currentEmployeeLabel.getStyle().set("font-size", "medium");

        headerLabel.add(header);
        headerLabel.add(currentEmployeeLabel);
        add(headerLabel);


        //tabs

        //tab 1 vac

//        vacationGrid.addColumn(new ValueProvider<Vacation, String>() {
//        @Override
//        public String apply(Vacation vacation) {
//            return vacation.getID().toString();
//            }
//        }).setHeader("ID");

//        vacationGrid.addColumn(new ValueProvider<Vacation, String>() {
//            @Override
//            public String apply(Vacation vacation) {
//                String lastName = vacation.getEmployee().getLastName();
//                String name = vacation.getEmployee().getName();
//                return lastName + " " + name;
//            }
//        }).setHeader("Сотрудник");

//        vacationGrid.addColumn("dateOfEvent").setHeader("Дата");

        vacationGrid.addColumn(new LocalDateRenderer<>(Vacation::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        vacationGrid.addColumn("comment").setHeader("Комментарий");


        //tab 2 sick

//        sickdayGrid.addColumn(new ValueProvider<Sickday, String>() {
//            @Override
//            public String apply(Sickday sickday) {
//                String lastName = sickday.getEmployee().getLastName();
//                String name = sickday.getEmployee().getName();
//                return lastName + " " + name;
//            }
//        }).setHeader("Сотрудник");

//        sickdayGrid.addColumn("ID").setHeader("ID");
        sickdayGrid.addColumn(new LocalDateRenderer<>(Sickday::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        sickdayGrid.addColumn("comment").setHeader("Комментарий");

        page2.setVisible(false);

        //tab 3
//        eventBTGrid.addColumn(new ValueProvider<Event, String>() {
//            @Override
//            public String apply(Event event) {
//                String lastName = event.getEmployee().getLastName();
//                String name = event.getEmployee().getName();
//                return lastName + " " + name;
//            }
//        }).setHeader("Командировки");


//        eventBTGrid.addColumn("dateOfEvent").setHeader("Дата");
        eventBTGrid.addColumn(new LocalDateRenderer<>(Event::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        eventBTGrid.addColumn("comment").setHeader("Комментарий");


        page3.setVisible(false);

        //tab 4

        eventOverGrid.addColumn(new LocalDateRenderer<>(Event::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        eventOverGrid.addColumn("comment").setHeader("Комментарий");


        page4.setVisible(false);

        //tab 5

        absenceGrid.addColumn(new LocalDateRenderer<>(Event::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        absenceGrid.addColumn("comment").setHeader("Комментарий");


        page5.setVisible(false);




        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);
        tabsToPages.put(tab3, page3);
        tabsToPages.put(tab4, page4);
        tabsToPages.put(tab5, page5);
        Tabs tabs = new Tabs(tab1, tab2, tab3, tab4, tab5);
        Div pages = new Div(page1, page2, page3, page4, page5);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, pages);


        //back button

        Button back = new Button("На главную");
        back.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        back.addClickListener(event -> UI.getCurrent().navigate(MyMainForm.class));
        bottomLayer.setMargin(true);
        bottomLayer.setPadding(true);
        bottomLayer.add(back);

        add(bottomLayer);

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer employeeId) {

        //employee
        Long empId = Long.valueOf(employeeId);
        Employee currentEmployee = null;
        List<Vacation> vacationList = null;
        List<Sickday> sickdayList = null;
        List<BusinessTripWeekday> bTripWeekdayList = null;
        List<BusinessTripWeekEnd> bTripWeekEndList = null;
        List<Event> eventBTList = new ArrayList<>();
        List<Overtime> overtimeList = null;
        List<PaidOvertime> paidOvertimeList = null;
        List<Event> eventOverList = new ArrayList<>();
        List<Absence> absenceList = null;

        try {
            currentEmployee = employeeLogic.getEmployee(empId).get();
            vacationList = vacationLogic.getAllFactVacationDays(currentEmployee, 2020);
            sickdayList = sickdayLogic.getAllActiveSickDays(currentEmployee,2020);
            bTripWeekdayList = btWeekdayLogic.getAllActiveEmployeeBTWeekday(currentEmployee, 2020);
            bTripWeekEndList = btWeekEndLogic.getAllActiveEmployeeBTWeekEnd(currentEmployee, 2020);
            overtimeList = overtimeLogic.getAllActiveOvertimes(currentEmployee, 2020);
            paidOvertimeList = paidOvertimeLogic.getAllActivePaidOvertime(currentEmployee, 2020);
            absenceList = absenceLogic.getAllActiveAbsenceDays(currentEmployee, 2020);


        } catch (DataStorageException e) {
            Notification.show("Запись сотрудника с идентификатором  "+empId+" не найдена");
        }

        currentEmployeeLabel.setText(currentEmployee.getName() + " " + currentEmployee.getLastName());

        //tab1
        vacationGrid.setItems(vacationList);
        page1.add(vacationGrid);

        //tab2

        sickdayGrid.setItems(sickdayList);
        page2.add(sickdayGrid);

        //tab3
        eventBTList.addAll(bTripWeekdayList);
        eventBTList.addAll(bTripWeekEndList);

        eventBTGrid.setItems(eventBTList);
        page3.add(eventBTGrid);

        //tab4
        eventOverList.addAll(overtimeList);
        eventOverList.addAll(paidOvertimeList);

        eventOverGrid.setItems(eventOverList);
        page4.add(eventOverGrid);

        //tab5

        absenceGrid.setItems(absenceList);
        page5.add(absenceGrid);


        this.employeeId = employeeId;

    }
}
