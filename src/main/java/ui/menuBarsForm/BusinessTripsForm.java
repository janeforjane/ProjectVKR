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
import entities.Vacation;
import logic.interfaces.*;
import ui.MyMainForm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = BusinessTripsForm.ROUTE)
@Theme(value = Material.class, variant = Material.DARK)
public class BusinessTripsForm extends Div{

    public static final String ROUTE = "BusinessTripsForm";
    public static final String TITLE = "BusinessTripsForm";

    //data
    BTWeekEndLogic btWeekEndLogic;
    BTWeekdayLogic btWeekdayLogic;
    LocalDate today;
    List<BusinessTripWeekEnd> allActiveBTWeekEnd = new ArrayList<>();
    List<BusinessTripWeekday> allActiveBTWeekday = new ArrayList<>();

    //ui components
    VerticalLayout headerLabel = new VerticalLayout();
    Header text = new Header();
    Header text2 = new Header();
    Header count = new Header();

    Grid<BusinessTripWeekEnd> businessTripWeekEndGrid = new Grid<>(BusinessTripWeekEnd.class, false);
    Tab tab1 = new Tab("Командировки в выходной");
    Div page1 = new Div();

    Grid<BusinessTripWeekday> businessTripWeekdayGrid = new Grid<>(BusinessTripWeekday.class, false);
    Tab tab2 = new Tab("Командировки в рабочее время");
    Div page2 = new Div();


    VerticalLayout bottomLayer = new VerticalLayout();

    public BusinessTripsForm() {

        try {
            InitialContext initialContext = new InitialContext();

            btWeekdayLogic = (BTWeekdayLogic) initialContext.lookup("java:module/BTWeekdayLogicImpl");
            btWeekEndLogic = (BTWeekEndLogic) initialContext.lookup("java:module/BTWeekEndLogicImpl");

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
        int countOfBusinessTrips = 0;
        try {

            List<BusinessTripWeekEnd> allActiveBTWeekEnd = btWeekEndLogic.getAllActiveBTWeekEnd(today.getYear());
            List<BusinessTripWeekday> allActiveBTWeekday = btWeekdayLogic.getAllActiveBTWeekday(today.getYear());

            if (allActiveBTWeekEnd.size()!=0){
                System.out.println("WeekEnd ----- " + allActiveBTWeekEnd.get(0).getDateOfEvent());
            }else {
                System.out.println("WeekEnd empty ----- ");
            }
            if (allActiveBTWeekday.size()!=0){
            System.out.println("Weekday -----" +allActiveBTWeekday.get(0).getDateOfEvent());
            }else {

                System.out.println("Weekday empty ----- ");
            }

            businessTripWeekEndGrid.setItems(allActiveBTWeekEnd);
            businessTripWeekdayGrid.setItems(allActiveBTWeekday);

            countOfBusinessTrips = allActiveBTWeekEnd.size() + allActiveBTWeekday.size();
        } catch (DataStorageException e) {
            Notification.show("Возникли проблемы с БД");
        }

        count.setText(String.valueOf(countOfBusinessTrips));
        count.getStyle().set("color", "#1E90FF");

        headerLabel.add(text,text2,count);

        add(headerLabel);



        //tabs

        //tab 1



        businessTripWeekEndGrid.addColumn(new ValueProvider<BusinessTripWeekEnd, String>() {
            @Override
            public String apply(BusinessTripWeekEnd businessTripWeekEnd) {
                String lastName = businessTripWeekEnd.getEmployee().getLastName();
                String name = businessTripWeekEnd.getEmployee().getName();
                return lastName + " " + name;
            }
        }).setHeader("Сотрудник");

        businessTripWeekEndGrid.addColumn(new LocalDateRenderer<>(BusinessTripWeekEnd::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");

        businessTripWeekEndGrid.addColumn(new ValueProvider<BusinessTripWeekEnd, String>() {
            @Override
            public String apply(BusinessTripWeekEnd businessTripWeekEnd) {
                String dateOfAbsence = "";
                if (businessTripWeekEnd.getAbsenceForOvertime() != null) {
                    dateOfAbsence = String.valueOf(businessTripWeekEnd.getAbsenceForOvertime().getDateOfEvent());
                }
                return dateOfAbsence;
            }
        }).setHeader("Отгул за командировку");

        businessTripWeekEndGrid.addColumn("comment").setHeader("Комментарий");

        page1.add(businessTripWeekEndGrid);


        //tab 2



        businessTripWeekdayGrid.addColumn(new ValueProvider<BusinessTripWeekday, String>() {
            @Override
            public String apply(BusinessTripWeekday businessTripWeekday) {
                String lastName = businessTripWeekday.getEmployee().getLastName();
                String name = businessTripWeekday.getEmployee().getName();
                return lastName + " " + name;
            }
        }).setHeader("Сотрудник");

        businessTripWeekdayGrid.addColumn(new LocalDateRenderer<>(BusinessTripWeekday::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");

        businessTripWeekdayGrid.addColumn("comment").setHeader("Комментарий");


        page2.add(businessTripWeekdayGrid);
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
