package ui.menuBarsForm;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@Route(value = EmployeesForm.ROUTE)
//@Theme(value = Material.class, variant = Material.DARK)
public class EmployeesForm extends Div {

    public static final String ROUTE = "EmployeesForm";
    public static final String TITLE = "EmployeesForm";


    public EmployeesForm() {
        //header
        HorizontalLayout headerLabel = new HorizontalLayout();
        headerLabel.setMargin(true);
        headerLabel.setPadding(true);

        Header header = new Header();
        header.setText("Employee");
        header.getStyle().set("color", "#9370DB");

        Label info = new Label("Please, choose employee");

        headerLabel.add(header);
        headerLabel.add(info);

        add(headerLabel);

        add(info);
    }
}
