package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import database.DbQuery;
import database.DbQuery2;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import logInHandling.LoginHandler;

public class AddNewUserController implements Initializable {

	// Declaring FXML objects
	@FXML
	private TextField usernameField;
	@FXML
	private TextField nameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private PasswordField confirmPasswordField;

	public void submitPressed() {
		// Gets information from the user and declares variables
		boolean isCorrect = false;
		String username = usernameField.getText();
		String name = nameField.getText();
		String password1 = passwordField.getText();
		String password2 = confirmPasswordField.getText();
		password1 = LoginHandler.cryptWithMD5(password1);
		password2 = LoginHandler.cryptWithMD5(password2);

		// Setting the default value of isCorrect to True
		isCorrect = true;

		// Boolean that will check if there is information in the fields
		Boolean isFilled = true;

		if (nameField.getText().trim().isEmpty() || usernameField.getText().trim().isEmpty()
				|| passwordField.getText().trim().isEmpty() || confirmPasswordField.getText().trim().isEmpty()) {
			isFilled = false;
			clearPressed();
		}

		// Checks if the passwords entered match, then changes the status of isCorrect
		// to TRUE
		if (password1.equals(password2) != true) {
			isCorrect = false;
			// Alert shown when the passwords entered do not match
			Alert nonMatching = new Alert(AlertType.WARNING);
			nonMatching.setTitle("Las contraseñas no coinciden! Vuelva a intentar!");
			nonMatching.setHeaderText(null);
			nonMatching.setContentText("Las contraseñas no coinciden!");
			nonMatching.showAndWait();
		}

		// If the passwords inputed before match after the Hashing, the program
		// continues.
		if (isFilled) {
			if (isCorrect) {
				ArrayList<String> query = DbQuery2.query("select * from UsersAndPasswordsLogin WHERE user= \""
						+ username + "\" AND pass = \"" + password1 + "\";");

				// Runs only if the User doesn't exists in the query.
				if (query.isEmpty() == true) {
					// Prepares the Strings with SQL Syntax
					username = "\"" + username + "\"";
					password1 = "\"" + password1 + "\"";
					name = "\"" + name + "\"";

					// 0 is admin, 1 is standard

					// Inserts the User into the Database s
					DbQuery.Insert("INSERT INTO UsersAndPasswordsLogin (user, pass, name) VALUES (" + username + ", "
							+ password1 + "," + name + ");");
					// Alert shown when the User was created successfully.
					Alert successfulCreation = new Alert(AlertType.INFORMATION);
					successfulCreation.setTitle("Usuario Creado Exitosamente!");
					successfulCreation.setHeaderText(null);
					successfulCreation.setContentText("Usuario Creado Exitosamente!");
					successfulCreation.showAndWait();
					Main.hideUserPopup();

				} else if (query.isEmpty() == false) { // WORKS
					// Alert shown when the User already existis in the Database.
					Alert existsAlready = new Alert(AlertType.INFORMATION);
					existsAlready.setTitle("El nombre de Usuario ya existe");
					existsAlready.setHeaderText(null);
					existsAlready.setContentText("Cambielo e intente con otro.");
					existsAlready.showAndWait();
				}
			}
		} else {
			Alert emptyFields = new Alert(AlertType.WARNING);
			emptyFields.setTitle("Algunos campos estan vacios");
			emptyFields.setHeaderText(null);
			emptyFields.setContentText("Algunos campos estan vacios, revise la información e intenta de nuevo.");
			emptyFields.showAndWait();
		}

	}

	public void clearPressed() {
		// Clears all fields if "Clear" is pressed.
		usernameField.setText("");
		nameField.setText("");
		passwordField.setText("");
		confirmPasswordField.setText("");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
