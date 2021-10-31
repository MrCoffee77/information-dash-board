package de.paetz.feuerwehr.informationdashboard.ui.calenderconfig;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.paetz.feuerwehr.informationdashboard.model.CalendarConfig;
import de.paetz.feuerwehr.informationdashboard.services.configuration.CalendarService;
import de.paetz.feuerwehr.informationdashboard.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.net.URI;
import java.util.List;

@PageTitle("Raum Kalender Konfiguration")
@Route(value = "editCal", layout = MainLayout.class)
public class CalendarConfigurationView extends Div implements AfterNavigationObserver {

    @Autowired
    CalendarService calendarService;

    Grid<CalendarConfig> grid = new Grid<>();


    public CalendarConfigurationView() {
        addClassName("calendar-configuration-view");
        setSizeFull();


        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);
        add(createNewCard(),grid);
    }

    private HorizontalLayout createNewCard() {
        CalendarConfig calendarConfig = new CalendarConfig(null, null, null, null);
        HorizontalLayout actions=new HorizontalLayout();
        Icon addIcon = VaadinIcon.PLUS.create();
        addIcon.addClassName("icon");
        addIcon.addClickListener(event ->  {
            calendarService.addCalendar(calendarConfig);
            refreshGrid();
        });
        actions.add(addIcon);
        HorizontalLayout card = createCardBase(calendarConfig, actions);
        return card;
    }


    private HorizontalLayout createCardBase(CalendarConfig calendarConfig, HorizontalLayout actions) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

   /*     Image image = new Image();
        image.setSrc(raumbelegung.getImage());*/
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        TextField calendarName=new TextField();
        calendarName.setPlaceholder("Raumname");

        if (calendarConfig.getName()!=null) {
            calendarName.setValue(calendarConfig.getName());
        }
        calendarName.addValueChangeListener(event -> calendarConfig.setName(event.getValue()));
        Span name = new Span(new Label("Name:"),calendarName);
        name.addClassName("name");

        TextField calendarAddress=new TextField();
        calendarAddress.setPlaceholder("Url");
        if (calendarConfig.getAddress()!=null) {
            calendarAddress.setValue(calendarConfig.getAddress().toString());
        }

        calendarAddress.addValueChangeListener(event -> calendarConfig.setAddress(URI.create(event.getValue())));
        Span date = new Span(new Label("Url:"),calendarAddress);
        date.addClassName("date");
        header.add(name, date);


        HorizontalLayout userPass = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        TextField calendarUser=new TextField();
        calendarUser.setPlaceholder("User");
        if (calendarConfig.getUser()!=null) {
            calendarUser.setValue(calendarConfig.getUser());
        }
        calendarUser.addValueChangeListener(event -> calendarConfig.setUser(event.getValue()));
        Span user = new Span(calendarUser);
        user.addClassName("User");

        PasswordField calendarPassword=new PasswordField();
        calendarPassword.setPlaceholder("Password");

        if (calendarConfig.getPassword()!=null) {
            calendarPassword.setValue(calendarConfig.getPassword());
        }
        calendarPassword.addValueChangeListener(event -> calendarConfig.setPassword(event.getValue()));
        Span pass = new Span(calendarPassword);
        pass.addClassName("date");
        userPass.add(user, pass);
        description.add(header, userPass,actions);
        card.add(description);
        return card;

    }

    private HorizontalLayout createCard(CalendarConfig calendarConfig) {

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon saveIcon = VaadinIcon.UPLOAD.create();
        saveIcon.addClassName("icon");
        String calName=calendarConfig.getName();
        saveIcon.addClickListener(event ->  {


            calendarService.updateCalendar(calName,calendarConfig);
            refreshGrid();
        });
        Icon deleteIcon = VaadinIcon.DEL.create();
        deleteIcon.addClassName("icon");
        deleteIcon.addClickListener(event -> {
           calendarService.deleteCalendar(calName);
            refreshGrid();
        });
//        Span likes = new Span(raumbelegung.getLikes());
//        likes.addClassName("likes");
//        Icon commentIcon = VaadinIcon.COMMENT.create();
//        commentIcon.addClassName("icon");
//        Span comments = new Span(raumbelegung.getComments());
//        comments.addClassName("comments");
//        Icon shareIcon = VaadinIcon.CONNECT.create();
//        shareIcon.addClassName("icon");
//        Span shares = new Span(raumbelegung.getShares());
//        shares.addClassName("shares");

        actions.add(saveIcon, deleteIcon);

        return createCardBase(calendarConfig,actions);
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        List<CalendarConfig> calendars = calendarService.getAllCalendars();
        grid.setItems(calendars);

    }

    private void refreshGrid() {
        List<CalendarConfig> calendars = calendarService.getAllCalendars();
        grid.setItems(calendars);

    }
}
