package ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import ui.menuBarsForm.EmployeesForm;


public class MainLayout extends AppLayout {

    public MainLayout() {

        addToNavbar(new DrawerToggle());
        addToNavbar(new H2("Title"));

        VerticalLayout menuBar = new VerticalLayout();
//        menuBar.add(new RouterLink(MainForm.TITLE, MainForm.class));
//        menuBar.add(new RouterLink(HorizontalForm.TITLE, HorizontalForm.class));
//        menuBar.add(new RouterLink(EmployeeForm.TITLE, EmployeeForm.class));

        RouterLink emplFormlink = new RouterLink(EmployeesForm.TITLE, EmployeesForm.class);
        emplFormlink.setText("Employees");
        menuBar.add(emplFormlink);


//        RouterLink link = new RouterLink(VacationForm.TITLE, VacationForm.class);
//        link.setText("Vacations");
//        menuBar.add(link);



        Integer l = 555;

//        RouterLink CurrentEmployeeFormlink = new RouterLink(CurrentEmployeeForm.TITLE, CurrentEmployeeForm.class, l);
//        link.setText("CurrentEmployee");
//        menuBar.add(CurrentEmployeeFormlink);

        addToDrawer(menuBar);

    }
}
