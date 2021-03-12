package ui.employeeForms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import dao.exception.DataStorageException;
import entities.*;
import logic.exception.DateIsBusyException;
import logic.exception.EmployeeNotFoundException;
import logic.interfaces.*;
import ui.MyMainForm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = CurrentVacationForm.ROUTE)
@Theme(value = Material.class, variant = Material.DARK)
public class CurrentVacationForm extends Div implements HasUrlParameter<Integer> {

    public static final String ROUTE = "CurrentVacationForm";
    public static final String TITLE = "CurrentVacationForm";
    private EmployeeLogic employeeLogic;
    private VacationLogic vacationLogic;

    int employeeId;

    //ui components
    VerticalLayout headerLabel = new VerticalLayout();
    Header currentEmployeeLabel = new Header();
    Button createVac = new Button("Создать отпуск");

    Grid<Vacation> planVacationGrid = new Grid<>(Vacation.class, false);
    Tab tab1 = new Tab("Плановый отпуск");
    Div page1 = new Div();
    Grid<Vacation> factVacationGrid = new Grid<>(Vacation.class,false);
    Tab tab2 = new Tab("Текущий отпуск");
    Div page2 = new Div();
    Grid<Vacation> replaceVacationGrid = new Grid<>(Vacation.class,false);
    Tab tab3 = new Tab("Переносы");
    Div page3 = new Div();

    VerticalLayout bottomLayer = new VerticalLayout();

    public CurrentVacationForm() {

        try {
            InitialContext initialContext = new InitialContext();
            employeeLogic = (EmployeeLogic) initialContext.lookup("java:module/EmployeeLogicImpl");
            vacationLogic = (VacationLogic) initialContext.lookup("java:module/VacationLogicImpl");

        } catch (NamingException e) {
            e.printStackTrace();
        }

        //header
        headerLabel.setMargin(true);
        headerLabel.setPadding(true);

        Header header = new Header();
        header.setText("Страница сотрудника");
        header.getStyle().set("color", "#9370DB");
        header.getStyle().set("font-size", "large");


        currentEmployeeLabel.getStyle().set("color", "#1E90FF");
        currentEmployeeLabel.getStyle().set("font-size", "medium");

        headerLabel.add(header);
        headerLabel.add(currentEmployeeLabel);
        headerLabel.add(createVac);
        add(headerLabel);




        //tabs

        //tab plan

        planVacationGrid.addColumn(new LocalDateRenderer<>(Vacation::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        planVacationGrid.addColumn("comment").setHeader("Комментарий");


        //tab fact


        factVacationGrid.addColumn(new LocalDateRenderer<>(Vacation::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        factVacationGrid.addColumn("comment").setHeader("Комментарий");

        page2.setVisible(false);

        //tab replace

        replaceVacationGrid.addColumn(new LocalDateRenderer<>(Vacation::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Плановая дата");

        replaceVacationGrid.addColumn(new ValueProvider<Vacation, String>() {
            @Override
            public String apply(Vacation vacation) {
                String l = String.valueOf(vacation.getReplaceDays().getDateOfEvent());

                return l;
            }
        }).setHeader("Дата переноса");


        replaceVacationGrid.addColumn("comment").setHeader("Комментарий");

        page3.setVisible(false);




        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);
        tabsToPages.put(tab3, page3);
        Tabs tabs = new Tabs(tab1, tab2, tab3);
        Div pages = new Div(page1, page2, page3);

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
    public void setParameter(BeforeEvent event, Integer employeeId) {

        //employee
        Long empId = Long.valueOf(employeeId);
        Employee currentEmployee = null;
        List<Vacation> planVacationList = null;
        List<Vacation> factVacationList = null;
        List<Vacation> replaceVacationList = null;

        try {
            currentEmployee = employeeLogic.getEmployee(empId).get();
            planVacationList = vacationLogic.getAllPlanVacationDays(currentEmployee, 2020);
            factVacationList = vacationLogic.getAllFactVacationDays(currentEmployee, 2020);
            replaceVacationList = vacationLogic.getAllReplacedVacationDays(currentEmployee, 2020);


        } catch (DataStorageException e) {
            Notification.show("Запись сотрудника с идентификатором  "+empId+" не найдена");
        }

        currentEmployeeLabel.setText(currentEmployee.getName() + " " + currentEmployee.getLastName());

        //tab1
        planVacationGrid.setItems(planVacationList);
        page1.add(planVacationGrid);

        //tab2

        factVacationGrid.setItems(factVacationList);
        page2.add(factVacationGrid);

        //tab3
        replaceVacationGrid.setItems(replaceVacationList);
        page3.add(replaceVacationGrid);


        //createVac Button
        Employee finalCurrentEmployee = currentEmployee;
        createVac.addClickListener(ev -> {

            Dialog dialog = new Dialog();
            VerticalLayout dialogLayout = new VerticalLayout();
            VerticalLayout v2 = new VerticalLayout();

            DatePicker vacationDate = new DatePicker("Дата отпуска");
            v2.add(vacationDate);
            TextField comment = new TextField("Комментарий");
            v2.add(comment);


            Button save = new Button("Сохранить");
            save.getStyle().set("border", "1px solid #9370DB");
            save.addClickListener(e -> {

                if(!vacationDate.isEmpty()) {

                    Vacation vacation = new Vacation(finalCurrentEmployee, vacationDate.getValue());
                    vacation.setComment(comment.getValue());
                    try {
                        vacationLogic.createPlanVacation(vacation);
                        dialog.close();
                    } catch (DateIsBusyException ex) {
                        Notification.show("Эта дата занята другим событием. Выберите другую дату");
                    } catch (DataStorageException ex) {
                        Notification.show("Проблема с БД");
                        dialog.close();
                    }
                }
            });

            Button save2 = new Button("Сохранить без даты");
            save2.getStyle().set("border", "1px solid #9370DB");
            save2.addClickListener(e -> {

                if(vacationDate.isEmpty()) {

                    Vacation vacation = new Vacation(finalCurrentEmployee, LocalDate.now().getYear());
                    vacation.setComment(comment.getValue());
                    try {
                        vacationLogic.createPlanVacation(vacation);
                        dialog.close();
                    } catch (DateIsBusyException ex) {
                        Notification.show("Эта дата занята другим событием");
                    } catch (DataStorageException ex) {
                        Notification.show("Проблема с БД");
                        dialog.close();
                    }
                }
            });



            dialogLayout.setPadding(true);
            dialogLayout.setMargin(true);
            v2.setMargin(true);
            v2.setPadding(true);
            dialogLayout.add(v2);
            dialogLayout.add(save);
            dialogLayout.add(save2);
            dialog.add(dialogLayout);

            dialog.setWidth("400px");
            dialog.setHeight("400px");
            dialog.open();

        });



    }
}
