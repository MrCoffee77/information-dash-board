package de.paetz.feuerwehr.informationdashboard.ui;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("Raum Kalender")
@Route(value = "showCal", layout = MainLayout.class)
public class RaumKalenderView extends Div implements AfterNavigationObserver {

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

        Span name = new Span(new H1(raumbelegung.raum()));
        name.addClassName("name");
        Span date = new Span(raumbelegung.start().toString());
        date.addClassName("date");
        header.add(name, date);

        Span post = new Span(raumbelegung.titel());
        post.addClassName("post");

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

        description.add(header, post);
        card.add(description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {


        List<Raumbelegung> raumbelegungList=new ArrayList<>();
        raumbelegungList.add(new Raumbelegung("Unterricht", LocalDateTime.now(),"Testbelegung"));
        raumbelegungList.add(new Raumbelegung("Bereitschaft", LocalDateTime.now(),"Testbelegung"));
        grid.setItems(raumbelegungList);
    }



}