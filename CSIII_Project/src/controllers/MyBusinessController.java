package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;

import application.Main;
import database.ClientQuery;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class MyBusinessController implements Initializable {

	/*
	 * 
	 * 
	 * 
	 * *****************************************************************************
	 * 
	 * VARIABLES
	 * 
	 *******************************************************************************/

	/**
	 * The ObservableList in charge of holding the objects that were in the
	 * ArrayList named "clientList".
	 * 
	 * @name JHYH
	 * 
	 */
	static ObservableList<Client> clientData;

	/**
	 * The ArrayList in charge of getting the objects received from the SQL
	 * Database.
	 */
	static ArrayList<Client> clientList;

	/**
	 * The TableView that will house the Client Objects received from the SQL
	 * Database.
	 */
	@FXML
	private TableView<Client> clientTable = new TableView<Client>();

	TableColumn<Client, String> codigoCliente;

	TableColumn<Client, String> nombreCol;

	TableColumn<Client, String> apellidoCol;

	TableColumn<Client, String> telefonoCol;

	TableColumn<Client, String> direccionCol;

	TableColumn<Client, Double> balanceCol;

	@FXML
	private Button addUser;

	@FXML
	private Button eraseUser;

	@FXML
	private Button updateData;

	@FXML
	private Button modUser;

	@FXML
	private Button saveData;

	@FXML
	private JFXDrawer drawer;
	@FXML
	private JFXHamburger hamburgerMain;

	private static boolean wasModified = false;

	private static ArrayList<Integer> rowsModified = new ArrayList<Integer>();

	public static ConcurrentProcesses b;

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * *****************************************************************************
	 * 
	 * ACTION EVENTS
	 * 
	 *******************************************************************************/

	@FXML
	void addPressed(ActionEvent event) {
		Main.showAddNewCustomer();
	}

	@FXML
	void modifyPressed(ActionEvent event) {

	}

	@FXML
	void erasePressed(ActionEvent event) {

	}

	@FXML
	void savePressed(ActionEvent event) {
		saveTable();
	}

	@FXML
	void updatePressed(ActionEvent event) {
		try {
			System.out.println("Querying MyBusinessClientList");
			clientData.clear();
			clientList = ClientQuery.query("select * from MyBusinessClientList;");
			for (int i = 0; i < clientList.size(); i++) {
				clientData.add(clientList.get(i));
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	/*
	 * *****************************************************************************
	 * 
	 * INITIALIZE
	 * 
	 *******************************************************************************/

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		clientData = FXCollections.observableArrayList();

		try {
			System.out.println("Querying MyBusinessClientList");
			clientList = ClientQuery.query("select * from MyBusinessClientList;");
			for (int i = 0; i < clientList.size(); i++) {
				clientData.add(clientList.get(i));
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		createTableColumns();

	}

	/*
	 * *****************************************************************************
	 * 
	 * Table Methods
	 * 
	 *******************************************************************************/

	/**
	 * This method initializes all table columns.
	 * 
	 * @author Jaime Hisao Y
	 * @since 2017-10-07
	 */
	@SuppressWarnings("unchecked")
	private void createTableColumns() {

		codigoCliente = new TableColumn<Client, String>("ID");
		codigoCliente.setCellValueFactory(new PropertyValueFactory<Client, String>("codigo"));
		codigoCliente.setMinWidth(45);

		nombreCol = new TableColumn<Client, String>("Nombre");
		nombreCol.setCellValueFactory(new PropertyValueFactory<Client, String>("fName"));
		nombreCol.setMinWidth(100);
		nombreCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nombreCol.setOnEditCommit(new EventHandler<CellEditEvent<Client, String>>() {
			public void handle(CellEditEvent<Client, String> t) {
				((Client) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
				ConcurrentProcesses.wasModifiedMyBusiness();
				rowsModified.add(t.getTableView().getItems().get(t.getTablePosition().getRow()).codigoProperty().get());
			}
		});

		apellidoCol = new TableColumn<Client, String>("Apellido");
		apellidoCol.setCellValueFactory(new PropertyValueFactory<Client, String>("lName"));
		apellidoCol.setMinWidth(100);
		apellidoCol.setCellFactory(TextFieldTableCell.forTableColumn());
		apellidoCol.setOnEditCommit(new EventHandler<CellEditEvent<Client, String>>() {
			public void handle(CellEditEvent<Client, String> t) {
				((Client) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastName(t.getNewValue());
				ConcurrentProcesses.wasModifiedMyBusiness();
				rowsModified.add(t.getTableView().getItems().get(t.getTablePosition().getRow()).codigoProperty().get());
			}
		});

		telefonoCol = new TableColumn<Client, String>("Telefono");
		telefonoCol.setCellValueFactory(new PropertyValueFactory<Client, String>("phoneNumber"));
		telefonoCol.setMinWidth(100);
		telefonoCol.setCellFactory(TextFieldTableCell.forTableColumn());
		telefonoCol.setOnEditCommit(new EventHandler<CellEditEvent<Client, String>>() {
			public void handle(CellEditEvent<Client, String> t) {
				((Client) t.getTableView().getItems().get(t.getTablePosition().getRow()))
				 		.setPhoneNumber(t.getNewValue());
				ConcurrentProcesses.wasModifiedMyBusiness();
		  		rowsModified.add(t.getTableView().getItems().get(t.getTablePosition().getRow()).codigoProperty().get());
			}
		});
		

		direccionCol = new TableColumn<Client, String>("Direccion");
		direccionCol.setCellValueFactory(new PropertyValueFactory<Client, String>("address"));
		direccionCol.setMinWidth(250);
		direccionCol.setCellFactory(TextFieldTableCell.forTableColumn());
		direccionCol.setOnEditCommit(new EventHandler<CellEditEvent<Client, String>>() {
			public void handle(CellEditEvent<Client, String> t) {
				((Client) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress(t.getNewValue());
				ConcurrentProcesses.wasModifiedMyBusiness();
				rowsModified.add(t.getTableView().getItems().get(t.getTablePosition().getRow()).codigoProperty().get());
			}
		});

		balanceCol = new TableColumn<Client, Double>("Balance");
		balanceCol.setCellValueFactory(new PropertyValueFactory<Client, Double>("balance"));
		balanceCol.setMinWidth(100);

		clientTable.setEditable(true);
		clientTable.setItems(clientData);
		clientTable.getColumns().addAll(codigoCliente, nombreCol, apellidoCol, telefonoCol, direccionCol, balanceCol);

	}

	/*
	 * *****************************************************************************
	 * 
	 * SQL Table Methods
	 * 
	 *******************************************************************************/

	public static void saveTable() {

		for (int i = 0; i < clientList.size(); i++) {

			Client currentClient = clientList.get(i);

			String firstName = currentClient.fNameProperty().get();
			String lastName = currentClient.lNameProperty().get();
			String address = currentClient.addressProperty().get();
			String phoneNumber = currentClient.phoneNumberProperty().get();
			double balance = currentClient.balanceProperty().get();
			int code = currentClient.codigoProperty().get();

			// Prepares the Strings with SQL Syntax
			firstName = "\"" + firstName + "\"";
			lastName = "\"" + lastName + "\"";
			address = "\"" + address + "\"";
			phoneNumber = "\"" + phoneNumber + "\"";
			String balance2 = "\"" + balance + "\"";
			String code2 = "\"" + code + "\"";

			ClientQuery.Insert("UPDATE MyBusinessClientList SET name = " + firstName + "," + "lastName = " + lastName
					+ "," + "address = " + address + "," + "phoneNumber = " + phoneNumber + "," + "balance = "
					+ balance2 + "WHERE codigo = " + code2 + ";");

		}

		Alert databaseUpdatedAlert = new Alert(AlertType.INFORMATION);
		databaseUpdatedAlert.setTitle("Base Actualizada");
		databaseUpdatedAlert.setHeaderText(null);
		databaseUpdatedAlert.setContentText("Base de Datos Actualizada Exitosamente!");
		databaseUpdatedAlert.showAndWait();
		wasModified = false;

	}

	public static void saveTableThread() {

		for (int i = 0; i < clientList.size(); i++) {

			Client currentClient = clientList.get(i);

			String firstName = currentClient.fNameProperty().get();
			String lastName = currentClient.lNameProperty().get();
			String address = currentClient.addressProperty().get();
			String phoneNumber = currentClient.phoneNumberProperty().get();
			double balance = currentClient.balanceProperty().get();
			int code = currentClient.codigoProperty().get();

			// Prepares the Strings with SQL Syntax
			firstName = "\"" + firstName + "\"";
			lastName = "\"" + lastName + "\"";
			address = "\"" + address + "\"";
			phoneNumber = "\"" + phoneNumber + "\"";
			String balance2 = "\"" + balance + "\"";
			String code2 = "\"" + code + "\"";

			ClientQuery.Insert("UPDATE MyBusinessClientList SET name = " + firstName + "," + "lastName = " + lastName
					+ "," + "address = " + address + "," + "phoneNumber = " + phoneNumber + "," + "balance = "
					+ balance2 + "WHERE codigo = " + code2 + ";");

		}

	}

	public static void updateModifiedRow() {

		for (int z = 0; z < clientList.size(); z++) {

			for (int i = 0; i < rowsModified.size(); i++) {

				if (clientList.get(z).codigoProperty().get() == rowsModified.get(i)) {

					Client currentClient = clientList.get(z);

					String firstName = currentClient.fNameProperty().get();
					String lastName = currentClient.lNameProperty().get();
					String address = currentClient.addressProperty().get();
					String phoneNumber = currentClient.phoneNumberProperty().get();
					double balance = currentClient.balanceProperty().get();
					int code = currentClient.codigoProperty().get();

					// Prepares the Strings with SQL Syntax
					firstName = "\"" + firstName + "\"";
					lastName = "\"" + lastName + "\"";
					address = "\"" + address + "\"";
					phoneNumber = "\"" + phoneNumber + "\"";
					String balance2 = "\"" + balance + "\"";
					String code2 = "\"" + code + "\"";

					ClientQuery.Insert("UPDATE MyBusinessClientList SET name = " + firstName + "," + "lastName = "
							+ lastName + "," + "address = " + address + "," + "phoneNumber = " + phoneNumber + ","
							+ "balance = " + balance2 + "WHERE codigo = " + code2 + ";");
					try {
						rowsModified.remove(i);
					} catch (IndexOutOfBoundsException e) {
						System.out.println("Error when removing Modified Row Warning");
					}
				}

			}
		}

	}

	public static void addNewCustomer(Client clientCreated) {

		String firstName = clientCreated.fNameProperty().get();
		String lastName = clientCreated.lNameProperty().get();
		String address = clientCreated.addressProperty().get();
		String phoneNumber = clientCreated.phoneNumberProperty().get();
		double balance = clientCreated.balanceProperty().get();
		int code = clientCreated.codigoProperty().get();

		// Prepares the Strings with SQL Syntax
		firstName = "\"" + firstName + "\"";
		lastName = "\"" + lastName + "\"";
		address = "\"" + address + "\"";
		phoneNumber = "\"" + phoneNumber + "\"";
		String balance2 = "\"" + balance + "\"";
		String code2 = "\"" + code + "\"";

		ClientQuery.Insert(
				"INSERT INTO MyBusinessClientList (name, lastName, address, phoneNumber, balance, codigo) VALUES ( "
						+ firstName + "," + lastName + "," + address + "," + phoneNumber + "," + balance2 + "," + code2
						+ ");");

	}

	/*
	 * *****************************************************************************
	 * 
	 * Client Methods
	 * 
	 *******************************************************************************/

	public static int generateClientCode() {
		int largestClientCode = 0;
		for (int i = 0; i < clientList.size(); i++) {
			if (clientList.get(i).codigoProperty().get() > largestClientCode) {
				largestClientCode = clientList.get(i).codigoProperty().get();
			}
		}

		largestClientCode++;
		return largestClientCode;
	}

	static void addToClientList(Client e) {
		clientList.add(e);
	}

	/*
	 * *****************************************************************************
	 * 
	 * INTERIOR CLASSES
	 * 
	 *******************************************************************************/

	/**
	 * The Client Class houses the data as Client Objects.
	 * 
	 * @author Jaime Hisao Yesaki Hinojosa
	 * @since 2017-10-06
	 * @version 2.1
	 *
	 */
	public static class Client {

		private SimpleStringProperty firstName;
		private SimpleStringProperty lastName;
		private SimpleStringProperty address;
		private SimpleStringProperty phoneNumber;
		private SimpleDoubleProperty balance;
		private final SimpleIntegerProperty code;

		/**
		 * Constructor for the Client Class, organizes the objects received from the
		 * database.
		 * 
		 * @param fName
		 *            the first name parameter
		 * @param lName
		 *            the last name parameter
		 * @param address
		 *            the address parameter
		 * @param phoneNumber
		 *            phoneNumber parameter
		 * @param balance
		 *            the balance parameter
		 * @param id
		 *            the id parameter
		 * @since 2017-14-17
		 * @author JHYH
		 */
		public Client(String fName, String lName, String address, String phoneNumber, Double balance, int id) {
			this.firstName = new SimpleStringProperty(fName);
			this.lastName = new SimpleStringProperty(lName);
			this.address = new SimpleStringProperty(address);
			this.phoneNumber = new SimpleStringProperty(phoneNumber);
			this.balance = new SimpleDoubleProperty(balance);
			this.code = new SimpleIntegerProperty(id);
		}

		public void setPhoneNumber(String newValue) {
			this.phoneNumber = new SimpleStringProperty(newValue);

		}

		public void setLastName(String newValue) {
			this.lastName = new SimpleStringProperty(newValue);

		}

		public void setAddress(String newValue) {
			this.address = new SimpleStringProperty(newValue);

		}

		public void setFirstName(String newValue) {
			this.firstName = new SimpleStringProperty(newValue);

		}

		public void setBalance(Double newValue) {
			this.balance = new SimpleDoubleProperty(newValue);
		}

		// Getters need to say Property instead of get so that just the Strings are
		// displayed
		public SimpleStringProperty fNameProperty() {
			return firstName;
		}

		public SimpleStringProperty lNameProperty() {
			return lastName;
		}

		public SimpleStringProperty addressProperty() {
			return address;
		}

		public SimpleStringProperty phoneNumberProperty() {
			return phoneNumber;
		}

		public SimpleDoubleProperty balanceProperty() {
			return balance;
		}

		public SimpleIntegerProperty codigoProperty() {
			return code;
		}
	}

	public static void addToObservableList(Client newClient) {
		clientData.add(newClient);

	}

	public static boolean isWasModified() {
		return wasModified;
	}

	public static void setWasModified(boolean wasModified2) {
		wasModified = wasModified2;
	}

}