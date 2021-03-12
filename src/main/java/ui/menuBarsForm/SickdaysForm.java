package ui.menuBarsForm;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import dao.exception.DataStorageException;
import entities.Sickday;
import entities.Vacation;
import logic.interfaces.SickdayLogic;
import logic.interfaces.VacationLogic;
import ui.MyMainForm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = SickdaysForm.ROUTE)
@Theme(value = Material.class, variant = Material.DARK)
public class SickdaysForm extends Div {

    public static final String ROUTE = "SickdaysForm";
    public static final String TITLE = "SickdaysForm";


    //data
    SickdayLogic sickdayLogic;
    LocalDate today;
    List<Sickday> sickdayList = new ArrayList<>();

    //ui components
    VerticalLayout headerLabel = new VerticalLayout();
    Header text = new Header();
    Header text2 = new Header();
    Header count = new Header();
    Grid<Sickday> sickdayGrid = new Grid<>(Sickday.class,false);
    VerticalLayout bottomLayer = new VerticalLayout();


    public SickdaysForm() {

        try {
            InitialContext initialContext = new InitialContext();
            sickdayLogic = (SickdayLogic) initialContext.lookup("java:module/SickdayLogicImpl");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        //headers
        headerLabel.setMargin(true);
        headerLabel.setPadding(true);

        text.setText("Сегодня");
        text.getStyle().set("color", "#E0FFFF");

        text2.setText("на больничном");
        text2.getStyle().set("color", "#E0FFFF");

        today = LocalDate.now();
        int countOfSickdays = 0;
        try {
            countOfSickdays = sickdayLogic.getCountOfAllSickdaysForPeriod(today,today);
        } catch (DataStorageException e) {
            Notification.show("Возникли проблемы с БД");
        }

        count.setText(String.valueOf(countOfSickdays));
        count.getStyle().set("color", "#1E90FF");

        headerLabel.add(text,text2,count);

        add(headerLabel);


        //year button
        //todo select for year
//        Button yearButton = new Button("Показать всех сотрудников");
//        all.addClickListener(event -> {
//
//            gridEmpl.setVisible(true);
//
//            gridEmpl.setItems(allActiveEmployees);
////            gridEmpl.getColumnByKey("ID")
////                    .setHeader("Идентификатор");
//
//        });
//
//        bottomLayer.add(gridEmpl);


        //grid

        sickdayGrid.setRowsDraggable(true);
        sickdayGrid.setColumnReorderingAllowed(true);
        sickdayGrid.setVisible(true);

        try {
            sickdayList.addAll(sickdayLogic.getAllSickdaysForPeriod
                    (LocalDate.of(today.getYear(),1,1),LocalDate.of(today.getYear(),12,31)));
        } catch (DataStorageException e) {
            Notification.show("Возникли проблемы с БД");
        }

        sickdayGrid.setItems(sickdayList);

        sickdayGrid.addColumn(new ValueProvider<Sickday, String>() {
            @Override
            public String apply(Sickday sickday) {
                String lastName = sickday.getEmployee().getLastName();
                String name = sickday.getEmployee().getName();
                return lastName + " " + name;
            }
        }).setHeader("Сотрудник");

        sickdayGrid.addColumn(new LocalDateRenderer<>(Sickday::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        sickdayGrid.addColumn("comment").setHeader("Комментарий");


        add(sickdayGrid);

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
