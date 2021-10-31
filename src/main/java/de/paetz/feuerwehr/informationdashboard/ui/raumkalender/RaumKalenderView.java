package de.paetz.feuerwehr.informationdashboard.ui.raumkalender;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.paetz.feuerwehr.informationdashboard.model.Raumbelegung;
import de.paetz.feuerwehr.informationdashboard.services.calender.CalenderReadService;
import de.paetz.feuerwehr.informationdashboard.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@PageTitle("Raum Kalender")
@Route(value = "showCal", layout = MainLayout.class)
public class RaumKalenderView extends Div implements AfterNavigationObserver {

    @Autowired
    CalenderReadService readService;

    Grid<Raumbelegung> grid = new Grid<>();

    public RaumKalenderView() {
        addClassName("raum-kalender-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);
        add(grid);
    }

    private HorizontalLayout createCard(Raumbelegung raumbelegung) {
        DateTimeFormatter df= DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
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

        Span name = new Span(raumbelegung.raum());
        name.addClassName("name");
        VerticalLayout nameLayout=new VerticalLayout();
        Span post = new Span(raumbelegung.titel());
        post.addClassName("post");
        nameLayout.add(name,post);
        VerticalLayout timeLayout=new VerticalLayout();
        Span date = new Span(raumbelegung.start().format(df));
        date.addClassName("date");
        Span dateEnd = new Span(raumbelegung.end().format(df));
        dateEnd.addClassName("date");
        timeLayout.add(date,dateEnd);
        header.add(timeLayout,nameLayout );


//        HorizontalLayout actions = new HorizontalLayout();
//        actions.addClassName("actions");
//        actions.setSpacing(false);
//        actions.getThemeList().add("spacing-s");

//        Icon likeIcon = VaadinIcon.HEART.create();
//        likeIcon.addClassName("icon");
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

//        actions.add(likeIcon, likes, commentIcon, comments, shareIcon, shares);

        description.add(header);
        card.add(description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {



        grid.setItems(readService.getBlockedTimes());

    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void refreshItems() {

        getUI().ifPresent(ui -> ui.access(() -> grid.setItems(readService.getBlockedTimes())));

    }



}