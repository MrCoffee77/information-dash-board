package de.paetz.feuerwehr.informationdashboard;

import com.vaadin.flow.component.dependency.NpmPackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class InformationDashBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(InformationDashBoardApplication.class, args);
	}

}
