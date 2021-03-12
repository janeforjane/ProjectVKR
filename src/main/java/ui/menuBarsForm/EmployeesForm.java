package ui.menuBarsForm;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;
import dao.exception.DataStorageException;
import entities.Employee;
import logic.EmployeeLogicImpl;
import logic.exception.EmployeeNotFoundException;
import logic.interfaces.EmployeeLogic;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import ui.MyMainForm;
import ui.employeeForms.CurrentEmployeeForm;
import ui.employeeForms.CurrentVacationForm;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = EmployeesForm.ROUTE)
//@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@Theme(value = Material.class, variant = Material.DARK)
public class EmployeesForm extends Div {

    public static final String ROUTE = "EmployeesForm";
    public static final String TITLE = "EmployeesForm";

    //data
    EmployeeLogic employeeLogic;

    //ui components
    VerticalLayout headerLabel = new VerticalLayout();
    Header header = new Header();
    Label info = new Label();
    HorizontalLayout employeeSearch = new HorizontalLayout();
    Grid<Employee> gridEmpl = new Grid<>(Employee.class);
    VerticalLayout bottomLayer = new VerticalLayout();

    public EmployeesForm() {

//        java:module/EmployeeLogicImpl

        //test
        try {
            InitialContext initialContext = new InitialContext();
            employeeLogic = (EmployeeLogic) initialContext.lookup("java:module/EmployeeLogicImpl");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        //test


        //header

        headerLabel.setMargin(true);
        headerLabel.setPadding(true);

        header.setText("Сотрудники");
        header.getStyle().set("color", "#9370DB");

        info.setText("          Пожалуйста, выберете сотрудника");
        info.getStyle().set("font-size", "small");

        //employee search
        employeeSearch.setMargin(true);
        employeeSearch.setPadding(true);

        List<Employee> allActiveEmployees = new ArrayList<>();


        //test
//        LocalDate date = LocalDate.of(2020,1,1);
//        Employee e1 = new Employee("rrr","eee", "fff",date,"ididi" );
//        Employee e2 = new Employee("rrr","eee", "fff",date,"ididi" );
//        Employee e3 = new Employee("ere","bvb", "ffxzxf",date,"wqwqw" );
//        allActiveEmployees.add(e1);
//        allActiveEmployees.add(e2);
//        allActiveEmployees.add(e3);
        //test



        try {
            allActiveEmployees.addAll(employeeLogic.getAllActiveEmployees());
        } catch (DataStorageException e) {
            Notification.show("Что-то пошло не так...");

            System.out.println("Что-то пошло не так...");
        }

        Select<Employee> employeeSelect = new Select<>();
        employeeSelect.setItems(allActiveEmployees);
        employeeSelect.setItemLabelGenerator(new ItemLabelGenerator<Employee>() {
            @Override
            public String apply(Employee employee) {
                String s = employee.getLastName()+" "+ employee.getName();
                return s;
            }
        });

        employeeSelect.setPlaceholder("Выбери кого-нибудь");
        employeeSelect.setLabel("Выбор сотрудника");


        //grid
        gridEmpl.setRowsDraggable(true);
        gridEmpl.setColumnReorderingAllowed(true);
        gridEmpl.setVisible(true);

        gridEmpl.setItems(allActiveEmployees);
        gridEmpl.getColumnByKey("ID")
                .setHeader("Идентификатор");
        gridEmpl.getColumnByKey("lastName")
                .setHeader("Фамилия");
        gridEmpl.getColumnByKey("name")
                .setHeader("Имя");
        gridEmpl.getColumnByKey("secondName")
                .setHeader("Отчество");
        gridEmpl.getColumnByKey("entryDate")
                .setHeader("Дата принятия");
        gridEmpl.getColumnByKey("position")
                .setHeader("Должность");
        gridEmpl.getColumnByKey("comment")
                .setHeader("Комментарий");
        gridEmpl.getColumnByKey("cancelDate")
                .setHeader("Дата увольнения");

        gridEmpl.removeColumnByKey("cancelDate");
        gridEmpl.removeColumnByKey("dateTimeOfCreate");

        gridEmpl.setColumnOrder(gridEmpl.getColumnByKey("ID")
                , gridEmpl.getColumnByKey("name")
                , gridEmpl.getColumnByKey("secondName")
                , gridEmpl.getColumnByKey("lastName")
                , gridEmpl.getColumnByKey("entryDate")
                , gridEmpl.getColumnByKey("position")
                , gridEmpl.getColumnByKey("comment"));

        GridContextMenu<Employee> contextMenu = gridEmpl.addContextMenu();

        EmployeeLogic finalEmployeeLogic = employeeLogic;
        contextMenu.addItem("Скорректировать", e -> {

            Employee employee = e.getItem().get();

            Dialog dialog = new Dialog();

            VerticalLayout dialogLayout = new VerticalLayout();

            HorizontalLayout v = new HorizontalLayout();

            TextField name = new TextField("Имя");
            name.setValue(employee.getName());
            v.add(name);
            TextField secondName = new TextField("Отчество");
            secondName.setValue(employee.getSecondName());
            v.add(secondName);
            TextField lastName = new TextField("Фамилия");
            lastName.setValue(employee.getLastName());
            v.add(lastName);

            HorizontalLayout v2 = new HorizontalLayout();

            DatePicker entryDate = new DatePicker("Дата принятия");
            entryDate.setValue(employee.getEntryDate());
            v2.add(entryDate);

            TextField comment = new TextField("Комментарий");
            if (employee.getComment() != null){
                comment.setValue(employee.getComment());
            }
            v2.add(comment);

            TextField position = new TextField("Должность");
            if (employee.getPosition() != null){
                position.setValue(employee.getPosition());
            }

            v2.add(position);

            DatePicker cancelDate = new DatePicker("Дата увольнения");

            v2.add(cancelDate);



            Button save = new Button("Сохранить");
            save.getStyle().set("border", "1px solid #9370DB");
            save.addClickListener(event -> {
                employee.setName(name.getValue());
                employee.setSecondName(secondName.getValue());
                employee.setLastName(lastName.getValue());
                employee.setEntryDate(entryDate.getValue());
                employee.setComment(comment.getValue());
                employee.setPosition(position.getValue());
                try {
                    finalEmployeeLogic.modifyEmployee(employee);
                    Notification.show("Запись "+name.getValue()+" сохранена");
                    dialog.close();
                } catch (EmployeeNotFoundException ex) {
                    Notification.show("Запись "+name.getValue()+" не найдена. Ничего не сохранено");
                    dialog.close();
                } catch (DataStorageException ex) {
                    Notification.show("Произошла ошибка при сохранении");
                    dialog.close();
                }
            });



            dialogLayout.setPadding(true);
            dialogLayout.setMargin(true);
            v.setMargin(true);
            v.setPadding(true);
            v2.setMargin(true);
            v2.setPadding(true);
            dialogLayout.add(v);
            dialogLayout.add(v2);
            dialogLayout.add(save);
            dialog.add(dialogLayout);

            dialog.setWidth("1200px");
            dialog.setHeight("400px");
            dialog.open();


        });


        contextMenu.addItem("Перейти к данным сотрудника", ev -> {
            int id = (int) ev.getItem().get().getID();

            UI.getCurrent().navigate(CurrentEmployeeForm.class, id);


        });

        contextMenu.addItem("Перейти к отпуску сотрудника", ev -> {
            int id = (int) ev.getItem().get().getID();

            UI.getCurrent().navigate(CurrentVacationForm.class, id);


        });

        Button showEmp = new Button("Показать");
        showEmp.addClickListener(event -> {

            Employee value = employeeSelect.getValue();
            gridEmpl.setVisible(true);

            gridEmpl.setItems(value);
        });

        Button all = new Button("Показать всех сотрудников");
        all.addClickListener(event -> {

            gridEmpl.setVisible(true);

            gridEmpl.setItems(allActiveEmployees);

        });

        bottomLayer.add(gridEmpl);



        //back button

        Button back = new Button("На главную");
        back.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
//        back.getStyle().set("border", "1px solid #9370DB");
        back.addClickListener(event -> UI.getCurrent().navigate(MyMainForm.class));
        bottomLayer.setMargin(true);
        bottomLayer.setPadding(true);
        bottomLayer.add(back);

        //layers
        headerLabel.add(header);
        headerLabel.add(info);

        employeeSearch.add(employeeSelect);
        employeeSearch.add(showEmp);
        employeeSearch.add(all);

        add(headerLabel);
        add(employeeSearch);
        add(bottomLayer);
    }

    Grid<Employee> getGrid(ArrayList<Employee> list){

        Grid<Employee> allEmpGrid = new Grid<>();

        allEmpGrid.setItems(list);
        allEmpGrid.getColumnByKey("ID")
                .setHeader("Идентификатор");
        allEmpGrid.getColumnByKey("lastName")
                .setHeader("Фамилия");
        allEmpGrid.getColumnByKey("name")
                .setHeader("Имя");
        allEmpGrid.getColumnByKey("secondName")
                .setHeader("Отчество");
        allEmpGrid.getColumnByKey("entryDate")
                .setHeader("Дата принятия");
        allEmpGrid.getColumnByKey("position")
                .setHeader("Должность");
        allEmpGrid.getColumnByKey("comment")
                .setHeader("Комментарий");

        return allEmpGrid;

    };
}
