package ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;


@Route(value = MyMainForm.ROUTE, layout = MainLayout.class)
public class MyMainForm extends HorizontalLayout {

    public static final String ROUTE = "MyMainFormRoute";
    public static final String TITLE = "MyMainForm";

    VerticalLayout headersLayout = new VerticalLayout();
    HorizontalLayout commonStatisticLayout1 = new HorizontalLayout();
    VerticalLayout cm1 = new VerticalLayout();
    VerticalLayout cm2 = new VerticalLayout();

    HorizontalLayout statisticLayoutForWeek = new HorizontalLayout();
    VerticalLayout slW1 = new VerticalLayout();
    VerticalLayout slW2 = new VerticalLayout();
    VerticalLayout slW3 = new VerticalLayout();
    VerticalLayout slW4 = new VerticalLayout();

    HorizontalLayout statisticLayoutForNextWeek = new HorizontalLayout();


    public MyMainForm() {


        //margin, padding
        headersLayout.setPadding(true);
        headersLayout.setMargin(true);
        commonStatisticLayout1.setPadding(true);
        commonStatisticLayout1.setMargin(true);
        cm1.setPadding(true);
        cm1.setMargin(true);
        cm2.setPadding(true);
        cm2.setMargin(true);



        //headers
        Header statisticHeader = new Header();
        statisticHeader.setText("Статистика");

        Label labelCommonStatistic = new Label();
        labelCommonStatistic.setText("Отсутствуют");

        Header weekAbsence = new Header();
        weekAbsence.setText("на этой неделе");
        weekAbsence.getStyle().set("color", "#E0FFFF");
        weekAbsence.setWidth("200px");

//        Header dayAbsenceCount = new Header();
        Button dayAbsenceCount = new Button();
        dayAbsenceCount.setText("12");
        dayAbsenceCount.setWidth("80px");
        dayAbsenceCount.setHeight("30px");
        dayAbsenceCount.getStyle().set("border", "1px solid #1E90FF");
        dayAbsenceCount.getStyle().set("color", "#1E90FF");
        dayAbsenceCount.getStyle().set("font-size", "x-large");

        Header weekAbsence2 = new Header();
        weekAbsence2.setText("сегодня");
        weekAbsence2.getStyle().set("color", "#E0FFFF");

        Button dayAbsenceCount2 = new Button();
        dayAbsenceCount2.setText("2");
        dayAbsenceCount2.setWidth("80px");
        dayAbsenceCount2.setHeight("30px");
        dayAbsenceCount2.getStyle().set("border", "1px solid #9E9E9E");
        dayAbsenceCount2.getStyle().set("color", "#1E90FF");
        dayAbsenceCount2.getStyle().set("font-size", "x-large");

        cm1.add(weekAbsence);
        cm1.add(dayAbsenceCount);

        cm2.add(weekAbsence2);
        cm2.add(dayAbsenceCount2);

        commonStatisticLayout1.add(cm1, cm2);

        headersLayout.add(statisticHeader, labelCommonStatistic, commonStatisticLayout1);

        add(headersLayout);



    }
}
