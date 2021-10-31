package de.paetz.feuerwehr.informationdashboard.security;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import de.paetz.feuerwehr.informationdashboard.ui.login.LoginView;
import de.paetz.feuerwehr.informationdashboard.ui.raumkalender.RaumKalenderView;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component


public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    List<Class<?>> allowedViews= Arrays.asList(LoginView.class, RaumKalenderView.class);
    @Override
    public void serviceInit(ServiceInitEvent event) {


        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(BeforeEnterEvent event) {


        if (!allowedViews.contains(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }
}