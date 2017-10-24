package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import controllers.MyBusinessController.Client;
import controllers.MyBusinessController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class AddNewCustomerController implements Initializable {

	@FXML
	private TextField phoneTextField;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField lastNameTextField;

	@FXML
	private TextField addressTextField;

	@FXML
	private Button saveButton;

	@FXML
	private Button emptyButton;

	@FXML
	void savePressed(ActionEvent event) {
		String name = nameTextField.getText();
		String lastName = lastNameTextField.getText();
		String phoneNumber = phoneTextField.getText();
		String address = addressTextField.getText();
		Double balance = 0.00;
		int clientCode = MyBusinessController.generateClientCode();

		Client newClient = new Client(name, lastName, address, phoneNumber, balance, clientCode);
		MyBusinessController.addToClientList(newClient);
		MyBusinessController.addToObservableList(newClient);

		nameTextField.setText("");
		lastNameTextField.setText("");
		phoneTextField.setText("");
		addressTextField.setText("");
		
		MyBusinessController.addNewCustomer(newClient);

		Alert databaseUpdatedAlert = new Alert(AlertType.INFORMATION);
		databaseUpdatedAlert.setTitle("Cliente Creado!");
		databaseUpdatedAlert.setHeaderText(null);
		databaseUpdatedAlert
				.setContentText("El cliente fue creado exitosamente con el siguiente codigo de cliente: " + clientCode);
		databaseUpdatedAlert.showAndWait();

		Main.hideAddNewCustomer();

	}

	@FXML
	void emptyPressed(ActionEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
