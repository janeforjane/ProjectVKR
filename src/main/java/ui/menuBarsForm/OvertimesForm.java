package ui.menuBarsForm;

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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import dao.exception.DataStorageException;
import entities.BusinessTripWeekEnd;
import entities.BusinessTripWeekday;
import entities.Overtime;
import entities.PaidOvertime;
import logic.interfaces.BTWeekEndLogic;
import logic.interfaces.BTWeekdayLogic;
import logic.interfaces.OvertimeLogic;
import logic.interfaces.PaidOvertimeLogic;
import ui.MyMainForm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = OvertimesForm.ROUTE)
@Theme(value = Material.class, variant = Material.DARK)
public class OvertimesForm extends Div {

    public static final String ROUTE = "OvertimesForm";
    public static final String TITLE = "OvertimesForm";


    //data
    OvertimeLogic overtimeLogic;
    PaidOvertimeLogic paidOvertimeLogic;
    LocalDate today;
    List<Overtime> overtimeList = new ArrayList<>();
    List<PaidOvertime> paidOvertimeList = new ArrayList<>();

    //ui components
    VerticalLayout headerLabel = new VerticalLayout();
    Header text = new Header();
    Header text2 = new Header();
    Header count = new Header();

    Grid<Overtime> overtimeGrid = new Grid<>(Overtime.class, false);
    Tab tab1 = new Tab("Переработки");
    Div page1 = new Div();

    Grid<PaidOvertime> paidOvertimeGrid = new Grid<>(PaidOvertime.class, false);
    Tab tab2 = new Tab("Оплачиваемые переработки");
    Div page2 = new Div();


    VerticalLayout bottomLayer = new VerticalLayout();

    public OvertimesForm() {

        try {
            InitialContext initialContext = new InitialContext();

            overtimeLogic = (OvertimeLogic) initialContext.lookup("java:module/OvertimeLogicImpl");
            paidOvertimeLogic = (PaidOvertimeLogic) initialContext.lookup("java:module/PaidOvertimeLogicImpl");

        } catch (NamingException e) {
            e.printStackTrace();
        }


        //headers
        headerLabel.setMargin(true);
        headerLabel.setPadding(true);

        text.setText("Сегодня");
        text.getStyle().set("color", "#E0FFFF");

        text2.setText("в командировке");
        text2.getStyle().set("color", "#E0FFFF");

        today = LocalDate.now();


        int countOfOvertimes = 0;
        try {

            List<Overtime> allActiveOvertimes = overtimeLogic.getAllActiveOvertimesForPeriod
                    (LocalDate.of(today.getYear(),1,1), LocalDate.of(today.getYear(),12,31));
            List<PaidOvertime> allActivePaidOvertimes = paidOvertimeLogic.getAllActivefPaidOvertimeForPeriod
                    (LocalDate.of(today.getYear(),1,1), LocalDate.of(today.getYear(),12,31));

//            if (allActiveBTWeekEnd.size()!=0){
//                System.out.println("WeekEnd ----- " + allActiveBTWeekEnd.get(0).getDateOfEvent());
//            }else {
//                System.out.println("WeekEnd empty ----- ");
//            }
//            if (allActiveBTWeekday.size()!=0){
//                System.out.println("Weekday -----" +allActiveBTWeekday.get(0).getDateOfEvent());
//            }else {
//
//                System.out.println("Weekday empty ----- ");
//            }

            overtimeGrid.setItems(allActiveOvertimes);
            paidOvertimeGrid.setItems(allActivePaidOvertimes);

            countOfOvertimes = allActiveOvertimes.size() + allActivePaidOvertimes.size();
        } catch (DataStorageException e) {
            Notification.show("Возникли проблемы с БД");
        }

        count.setText(String.valueOf(countOfOvertimes));
        count.getStyle().set("color", "#1E90FF");

        headerLabel.add(text,text2,count);

        add(headerLabel);

        //tabs

        //tab 1



        overtimeGrid.addColumn(new ValueProvider<Overtime, String>() {
            @Override
            public String apply(Overtime overtime) {
                String lastName = overtime.getEmployee().getLastName();
                String name = overtime.getEmployee().getName();
                return lastName + " " + name;
            }
        }).setHeader("Сотрудник");

        overtimeGrid.addColumn(new LocalDateRenderer<>(Overtime::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");

        overtimeGrid.addColumn(new ValueProvider<Overtime, String>() {
            @Override
            public String apply(Overtime overtime) {
                String dateOfAbsence = "";
                if (overtime.getAbsenceForOvertime() != null) {
                    dateOfAbsence = String.valueOf(overtime.getAbsenceForOvertime().getDateOfEvent());
                }
                return dateOfAbsence;
            }
        }).setHeader("Отгул за переработку");

        overtimeGrid.addColumn("comment").setHeader("Комментарий");

        page1.add(overtimeGrid);


        //tab 2



        paidOvertimeGrid.addColumn(new ValueProvider<PaidOvertime, String>() {
            @Override
            public String apply(PaidOvertime paidOvertime) {
                String lastName = paidOvertime.getEmployee().getLastName();
                String name = paidOvertime.getEmployee().getName();
                return lastName + " " + name;
            }
        }).setHeader("Сотрудник");

        paidOvertimeGrid.addColumn(new LocalDateRenderer<>(PaidOvertime::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");

        paidOvertimeGrid.addColumn("comment").setHeader("Комментарий");


        page2.add(paidOvertimeGrid);
        page2.setVisible(false);





        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);
        Tabs tabs = new Tabs(tab1, tab2);
        Div pages = new Div(page1, page2);

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
}
