package ui.menuBarsForm;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import dao.exception.DataStorageException;
import entities.Employee;
import entities.Sickday;
import entities.Vacation;
import logic.exception.EmployeeNotFoundException;
import logic.interfaces.EmployeeLogic;
import logic.interfaces.VacationLogic;
import ui.MyMainForm;
import ui.employeeForms.CurrentEmployeeForm;
import ui.employeeForms.CurrentVacationForm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = VacationForm.ROUTE)
@Theme(value = Material.class, variant = Material.DARK)
public class VacationForm extends Div {

    public static final String ROUTE = "VacationForm";
    public static final String TITLE = "VacationForm";

    //data
    VacationLogic vacationLogic;
    LocalDate today;
    List<Vacation> vacationList = new ArrayList<>();

    //ui components
    VerticalLayout headerLabel = new VerticalLayout();
    Header text = new Header();
    Header text2 = new Header();
    Header count = new Header();
    Grid<Vacation> vacationGrid = new Grid<>(Vacation.class, false);
    VerticalLayout bottomLayer = new VerticalLayout();


    public VacationForm() {

        try {
            InitialContext initialContext = new InitialContext();
            vacationLogic = (VacationLogic) initialContext.lookup("java:module/VacationLogicImpl");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        //headers
        headerLabel.setMargin(true);
        headerLabel.setPadding(true);

        text.setText("Сегодня");
        text.getStyle().set("color", "#E0FFFF");

        text2.setText("в отпуске");
        text2.getStyle().set("color", "#E0FFFF");

        today = LocalDate.now();
        int countOfVacations = 0;
        try {
            countOfVacations = vacationLogic.getCountOfVacationsOfWeek(today, today);
                } catch (DataStorageException e) {
            Notification.show("Возникли проблемы с БД");
            }

        count.setText(String.valueOf(countOfVacations));
        count.getStyle().set("color", "#1E90FF");

        headerLabel.add(text,text2,count);

        add(headerLabel);

        //grid

        vacationGrid.setRowsDraggable(true);
        vacationGrid.setColumnReorderingAllowed(true);
        vacationGrid.setVisible(true);

        try {
            vacationList.addAll(vacationLogic.getAllFactVacationDaysForAll(today.getYear()));
        } catch (DataStorageException e) {
            Notification.show("Возникли проблемы с БД");
        }

        vacationGrid.setItems(vacationList);

        vacationGrid.addColumn(new ValueProvider<Vacation, String>() {
            @Override
            public String apply(Vacation vacation) {
                String lastName = vacation.getEmployee().getLastName();
                String name = vacation.getEmployee().getName();
                return lastName + " " + name;
            }
        }).setHeader("Сотрудник");

        vacationGrid.addColumn(new LocalDateRenderer<>(Vacation::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        vacationGrid.addColumn("comment").setHeader("Комментарий");

        add(vacationGrid);



        GridContextMenu<Vacation> contextMenu = vacationGrid.addContextMenu();

//        EmployeeLogic finalEmployeeLogic = employeeLogic;

        contextMenu.addItem("Перейти к отпуску сотрудника", ev -> {
            int id = (int) ev.getItem().get().getEmployee().getID();

            UI.getCurrent().navigate(CurrentVacationForm.class, id);


        });


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
