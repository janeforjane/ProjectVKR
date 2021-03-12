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
import entities.Absence;
import entities.Sickday;
import logic.interfaces.AbsenceLogic;
import logic.interfaces.SickdayLogic;
import ui.MyMainForm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = AbsencesForm.ROUTE)
@Theme(value = Material.class, variant = Material.DARK)
public class AbsencesForm extends Div {

    public static final String ROUTE = "AbsencesForm";
    public static final String TITLE = "AbsencesForm";

    //data
    AbsenceLogic absenceLogic;
    LocalDate today;
    List<Absence> absenceList = new ArrayList<>();

    //ui components
    VerticalLayout headerLabel = new VerticalLayout();
    Header text = new Header();
    Header text2 = new Header();
    Header count = new Header();
    Grid<Absence> absenceGrid = new Grid<>(Absence.class,false);
    VerticalLayout bottomLayer = new VerticalLayout();


    public AbsencesForm() {

        try {
            InitialContext initialContext = new InitialContext();
            absenceLogic = (AbsenceLogic) initialContext.lookup("java:module/AbsenceLogicImpl");
        } catch (NamingException e) {
            e.printStackTrace();
        }


        //headers
        headerLabel.setMargin(true);
        headerLabel.setPadding(true);

        text.setText("Сегодня");
        text.getStyle().set("color", "#E0FFFF");

        text2.setText("отсутствуют");
        text2.getStyle().set("color", "#E0FFFF");

        today = LocalDate.now();
        int countOfAbsences = 0;

        try {
            absenceList = absenceLogic.getAllActiveOfAbsencesForPeriod
                    (LocalDate.of(today.getYear(),1,1), LocalDate.of(today.getYear(),12,31));
            countOfAbsences = absenceList.size();
        } catch (DataStorageException e) {
            Notification.show("Возникли проблемы с БД");
        }

        count.setText(String.valueOf(countOfAbsences));
        count.getStyle().set("color", "#1E90FF");

        headerLabel.add(text,text2,count);

        add(headerLabel);



        //grid

        absenceGrid.setRowsDraggable(true);
        absenceGrid.setColumnReorderingAllowed(true);
        absenceGrid.setVisible(true);


        absenceGrid.setItems(absenceList);

        absenceGrid.addColumn(new ValueProvider<Absence, String>() {
            @Override
            public String apply(Absence absence) {
                String lastName = absence.getEmployee().getLastName();
                String name = absence.getEmployee().getName();
                return lastName + " " + name;
            }
        }).setHeader("Сотрудник");


        absenceGrid.addColumn(new ValueProvider<Absence, String>() {
            @Override
            public String apply(Absence absence) {
                String reason = "";
                if(absence.getReasonsOfAbsenceBusinessTrip() != null){
                    reason = "Командировка";
                }
                if(absence.getReasonsOfAbsenceOvertime() !=null){
                    reason = "Переработка";
                }

                return reason;
            }
        }).setHeader("Основание для отгула");

        absenceGrid.addColumn(new ValueProvider<Absence, String>() {
            @Override
            public String apply(Absence absence) {
                String dateOfAbsence = "";
                if(absence.getReasonsOfAbsenceBusinessTrip() != null){
                    dateOfAbsence = String.valueOf(absence.getReasonsOfAbsenceBusinessTrip().getDateOfEvent());
                }
                if(absence.getReasonsOfAbsenceOvertime() !=null){
                    dateOfAbsence = String.valueOf(absence.getReasonsOfAbsenceOvertime().getDateOfEvent());
                }
                return dateOfAbsence;
            }
        }).setHeader("Дата основания для отгула");


        absenceGrid.addColumn(new LocalDateRenderer<>(Absence::getDateOfEvent, "dd/MM/yyyy")).setSortable(true)
                .setHeader("Дата");
        absenceGrid.addColumn("comment").setHeader("Комментарий");


        add(absenceGrid);

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
