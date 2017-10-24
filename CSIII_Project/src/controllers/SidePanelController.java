package controllers;

import com.jfoenix.controls.JFXButton;

import application.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SidePanelController implements Initializable {

	@FXML
	private JFXButton b1;
	@FXML
	private JFXButton b2;
	@FXML
	private JFXButton b3;
	@FXML
	private JFXButton b4;

	// Scenes for the Tabs to be used.
	// My Business Tab
	Parent myBusinessParent;
	static Scene myBusinessScene;

	// My Finances Tab
	Parent myFinances;
	static Scene myFinancesScene;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			myBusinessParent = FXMLLoader.load(getClass().getResource("/application/MyBusinessGUI.fxml"));
			myBusinessScene = new Scene(myBusinessParent);
			// My Finances Scene
			myFinances = FXMLLoader.load(getClass().getResource("/application/MyFinanceGUI.fxml"));
			myFinancesScene = new Scene(myFinances);
			// myBusinessScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		} catch (IOException e) {
			// to catch any exception
			e.printStackTrace();
		}

	}

	@FXML
	private void businessPressed(ActionEvent event) {
		Main.setScene(myBusinessScene);
		Main.setStageTitle("Mi Negocio");
	}

	@FXML
	private void myFinancePressed() {
		Main.setScene(myFinancesScene);
		Main.setStageTitle("Mis Finanzas");
	}

	@FXML
	private void exit(ActionEvent event) {
		System.exit(0);
	}

}
