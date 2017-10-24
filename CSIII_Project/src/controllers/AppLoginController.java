package controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.Main;
import database.DbQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import logInHandling.LoginHandler;

public class AppLoginController implements Initializable {

	/*
	 * *****************************************************************************
	 * 
	 * VARIABLES
	 * 
	 *******************************************************************************/

	// Declaration of FXML Components
	@FXML
	JFXPasswordField passwordField;
	@FXML
	JFXTextField usernameField;
	@FXML
	JFXButton submitButton;
	@FXML
	Label internetStatus;

	// Holds value to determine if Internet is available from Initialization, Class
	// Variable
	boolean isOnline;
	boolean isValidated;

	@FXML
	private MenuBar menuBar = new MenuBar();
	@FXML
	Menu setup = new Menu("Configuracion");
	@FXML
	MenuItem masterSetup = new MenuItem("Configuracion Maestra"); // For triggering the setup tab.

	/*
	 * *****************************************************************************
	 * 
	 * ACTION EVENTS
	 * 
	 *******************************************************************************/

	public void masterSetupPressed() {
		Main.showMasterSetup();
	}

	// Method triggered when "submit" button is pressed.
	public void submitPressed(ActionEvent Event) throws IOException {
		logIn();
	}

	/*
	 * *****************************************************************************
	 * 
	 * INHERITED METHODS
	 * 
	 *******************************************************************************/

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Enables the use of the system menu bar
		menuBar.setUseSystemMenuBar(true);

		passwordField.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				try {
					logIn();
				} catch (IOException e) {
					System.out.println("Log In Failed!");
					e.printStackTrace();
				}
			}
		});

		// When running the login window, the system makes sure that the Internet is
		// available and displays
		// that accordingly
		internetStatus.setText("Conectando...");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Conectando...");
		alert.setHeaderText(null);
		alert.setContentText("Porfavor Espera...");
		if (netIsAvailable() == true) {
			alert.close();
		}

		internetStatus.setText("Desconectado");
		if (netIsAvailable() == true) {
			internetStatus.setText("Conectado");
			isOnline = true;
		} else if (netIsAvailable() == false) {
			Alert alert2 = new Alert(AlertType.WARNING);
			alert2.setTitle("Sin Conexión a Internet");
			alert2.setHeaderText(null);
			alert2.setContentText("Conectate al Internet y vuelve a intentar, esta funcion aún no esta disponible");
			alert2.showAndWait();
			internetStatus.setText("Deconectado");
			isOnline = false;
			System.exit(0);
		}

	}

	private void logIn() throws IOException  {
		String user = usernameField.getText();
		String password = passwordField.getText();

		// Depending on the connection status, the system will run either of the
		// following code
		if (isOnline == true) { // If Online
			password = LoginHandler.cryptWithMD5(password);

			boolean isValid = DbQuery.query("select * from UsersAndPasswordsLogin WHERE user= \"" + user
					+ "\" AND pass = \"" + password + "\";");

			if (isValid == true) {
				// Alert alert = new Alert(AlertType.INFORMATION);
				// alert.setTitle("Operacion Exitosa");
				// alert.setHeaderText(null);
				// alert.setContentText("Bienvenido!!");
				// alert.showAndWait();
				isValidated = true;
				Parent mainDashboard = FXMLLoader.load(getClass().getResource("/application/MainDashboardGUI.fxml"));
				Scene dashboardScene = new Scene(mainDashboard);
				Main.setScene(dashboardScene);
				Main.setStageTitle("Dashboard");

			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Información Invalida");
				alert.setHeaderText(null);
				alert.setContentText("Combinación de Usuario y contraseña invalida");
				alert.showAndWait();
				passwordField.setText("");
				usernameField.setText("");
			}
		} else if (isOnline == false) { // If there is no Internet connection
			// password = PasswordHandling.cryptWithMD5(password);
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Sin Conexión a Internet");
			alert.setHeaderText(null);
			alert.setContentText("Conectate al Internet y vuelve a intentar, esta funcion aún no esta disponible");
			alert.showAndWait();
		}

	}

	/*
	 * *****************************************************************************
	 * 
	 * METHODS
	 * 
	 *******************************************************************************/

	/**
	 * This method verifies that there is an active Internet connection, returning
	 * true if ping to google.com was successful, and false if otherwise
	 * 
	 * @return boolean true if connection is active, false if inactive.
	 */
	private static boolean netIsAvailable() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return false;
		}
	}

}
