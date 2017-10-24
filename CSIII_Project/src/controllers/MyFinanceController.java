package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import database.ClientQuery;
import database.PersonalFinancialQuery;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MyFinanceController implements Initializable {

	static ArrayList<Transaction> transactions;

	static ArrayList<Integer> rowsModified = new ArrayList<Integer>();

	static ObservableList<Transaction> data;

	private static boolean wasModified = false;

	@FXML
	private TableView<Transaction> financeTable = new TableView<Transaction>();

	private TableColumn<Transaction, Integer> idCol;
	private TableColumn<Transaction, String> conceptCol;
	private TableColumn<Transaction, Double> debitCol;
	private TableColumn<Transaction, Double> creditCol;
	private TableColumn<Transaction, String> notesCol;

	public static ConcurrentProcesses b;

	@FXML
	JFXDrawer drawer;
	@FXML
	JFXHamburger hamburgerMain;

	@Override
	public void initialize(URL location, ResourceBundle resources) {


//		HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburgerMain);
//		transition.setRate(-1);
//		hamburgerMain.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
//			transition.setRate(transition.getRate() * -1);
//			transition.play();
//
//			if (drawer.isShown()) {
//				drawer.close();
//			} else {
//				drawer.open();
//			}
//
//		});

		financeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		data = FXCollections.observableArrayList();

		try {
			System.out.println("Querying MyPersonalFinances");
			transactions = PersonalFinancialQuery.query("select * from MyPersonalFinances;");
			for (int i = 0; i < transactions.size(); i++) {
				data.add(transactions.get(i));
				System.out.println("Retrieved ID and Added: " + transactions.get(i).transactionIdProperty().get());
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		constructTableColumns();

	}

	/**
	 * Generates an Integer Value that will be assigned to a new Transaction.
	 * 
	 * @return returns an Integer that corresponds to the next Transaction ID Value
	 *         on the ArrayList
	 * @since 2017-10-07
	 * @author Jaime Hisao Yesaki H
	 */
	public static int generateTransactionId() {
		int largestId = 0;
		for (int i = 0; i < transactions.size(); i++) {
			if (transactions.get(i).transactionIdProperty().get() > largestId) {
				largestId = transactions.get(i).transactionIdProperty().get();
			}
			largestId++;
		}
		return largestId;
	}

	static void addTransactionList(Transaction e) {
		transactions.add(e);
	}

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
	 * *****************************************************************************
	 * 
	 * SQL Table Methods
	 * 
	 *******************************************************************************/

	/**
	 * Receives a Transaction Object and adds it to the SQL Database
	 * 
	 * @param newTransaction
	 *            receives a Transaction Object.
	 * @since 2017-10-07
	 * @author Jaime Hisao Yesaki H
	 */
	public static void addNewCustomer(Transaction newTransaction) {

		int id = newTransaction.transactionIdProperty().get();
		String concept = newTransaction.conceptProperty().get();
		double debit = newTransaction.debitProperty().get();
		double credit = newTransaction.creditProperty().get();
		String notes = newTransaction.notesProperty().get();

		// Prepares the Strings with SQL Syntax
		String id1 = "\"" + id + "\"";
		concept = "\"" + concept + "\"";
		String debit1 = "\"" + debit + "\"";
		String credit1 = "\"" + credit + "\"";
		notes = "\"" + notes + "\"";

		ClientQuery.Insert("INSERT INTO MyPersonalFinances (transactionId, concept, debit, credit, notes) VALUES ( "
				+ id1 + "," + concept + "," + debit1 + "," + credit1 + "," + notes + ");");

	}

	/**
	 * If there was any change to the database, the method updates the SQL Database,
	 * it uses the ID as a key to match objects with the database. It uses data from
	 * the local Transactions ArrayList. Then Issues an Alert.
	 * 
	 * @since 2017-10-7
	 * @author Jaime Hisao Yesaki Hinojosa
	 */
	public static void updateTableChanges() {

		for (int i = 0; i < transactions.size(); i++) {

			// The Object being updated
			Transaction currentTransaction = transactions.get(i);

			// Parameters for the object being inserted
			int id = currentTransaction.transactionIdProperty().get();
			String concept = currentTransaction.conceptProperty().get();
			double debit = currentTransaction.debitProperty().get();
			double credit = currentTransaction.creditProperty().get();
			String notes = currentTransaction.notesProperty().get();

			// Prepares the Strings with SQL Syntax
			String id1 = "\"" + id + "\"";
			concept = "\"" + concept + "\"";
			String debit1 = "\"" + debit + "\"";
			String credit1 = "\"" + credit + "\"";
			notes = "\"" + notes + "\"";

			// Sends the Insert Instruction to the SQL Server and catches any exception in
			// case the Insert sends one.
			try {
				ClientQuery.Insert("UPDATE MyPersonalFinances SET concept = " + concept + "," + "debit = " + debit1
						+ "," + "credit = " + credit1 + "," + "notes = " + notes + "WHERE transactionId = " + id1
						+ ";");
			} catch (Exception e) {
				Alert failureToUpdate = new Alert(AlertType.WARNING);
				failureToUpdate.setTitle("ERROR");
				failureToUpdate.setHeaderText(null);
				failureToUpdate.setContentText("No se pudo actualizar la base de datos.");
				failureToUpdate.showAndWait();
			}
		}
		Alert databaseUpdatedAlert = new Alert(AlertType.INFORMATION);
		databaseUpdatedAlert.setTitle("Base Actualizada");
		databaseUpdatedAlert.setHeaderText(null);
		databaseUpdatedAlert.setContentText("Base de Datos Actualizada Exitosamente!");
		databaseUpdatedAlert.showAndWait();
	}

	public static void updateModifiedRow() {

		for (int z = 0; z < transactions.size(); z++) {

			for (int i = 0; i < rowsModified.size(); i++) {

				if (transactions.get(z).transactionIdProperty().get() == rowsModified.get(i)) {

					Transaction currentTransaction = transactions.get(z);

					int transactionId = currentTransaction.transactionIdProperty().get();
					String concept = currentTransaction.conceptProperty().get();
					double debit = currentTransaction.debitProperty().get();
					double credit = currentTransaction.creditProperty().get();
					String notes = currentTransaction.notesProperty().get();

					// Prepares the Strings with SQL Syntax
					String transactionId2 = "\"" + transactionId + "\"";
					concept = "\"" + concept + "\"";
					String debit2 = "\"" + debit + "\"";
					String credit2 = "\"" + credit + "\"";
					notes = "\"" + notes + "\"";

					ClientQuery.Insert("UPDATE MyPersonalFinances SET concept = " + concept + "," + "debit = " + debit2
							+ "," + "credit = " + credit2 + "," + "notes = " + notes + "WHERE transactionId = "
							+ transactionId2 + ";");

					try {
						rowsModified.remove(i);
					} catch (IndexOutOfBoundsException e) {
						System.out.println("Error when removing Modified Row Warning");
					}

				}

			}
		}

	}

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
	 * *****************************************************************************
	 * 
	 * TABLE METHODS
	 * 
	 *******************************************************************************/

	@SuppressWarnings("unchecked")
	private void constructTableColumns() {

		idCol = new TableColumn<Transaction, Integer>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("transactionId"));
		idCol.setMinWidth(10);

		conceptCol = new TableColumn<Transaction, String>("Concepto");
		conceptCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("concept"));
		conceptCol.setMinWidth(150);
		conceptCol.setOnEditCommit(new EventHandler<CellEditEvent<Transaction, String>>() {
			public void handle(CellEditEvent<Transaction, String> t) {
				((Transaction) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setConcept(t.getNewValue());
				ConcurrentProcesses.wasModifiedMyFinance();
				rowsModified.add(
						t.getTableView().getItems().get(t.getTablePosition().getRow()).transactionIdProperty().get());
			}
		});

		debitCol = new TableColumn<Transaction, Double>("Debito");
		debitCol.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("debit"));
		debitCol.setMinWidth(30);
		debitCol.setOnEditCommit(new EventHandler<CellEditEvent<Transaction, Double>>() {
			public void handle(CellEditEvent<Transaction, Double> t) {
				((Transaction) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setDebit(t.getNewValue());
				ConcurrentProcesses.wasModifiedMyFinance();
				rowsModified.add(
						t.getTableView().getItems().get(t.getTablePosition().getRow()).transactionIdProperty().get());
			}
		});

		creditCol = new TableColumn<Transaction, Double>("Credito");
		creditCol.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("credit"));
		creditCol.setMinWidth(30);
		creditCol.setOnEditCommit(new EventHandler<CellEditEvent<Transaction, Double>>() {
			public void handle(CellEditEvent<Transaction, Double> t) {
				((Transaction) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setCredit(t.getNewValue());
				ConcurrentProcesses.wasModifiedMyFinance();
				rowsModified.add(
						t.getTableView().getItems().get(t.getTablePosition().getRow()).transactionIdProperty().get());
			}
		});

		notesCol = new TableColumn<Transaction, String>("Notas");
		notesCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("notes"));
		notesCol.setMinWidth(150);
		notesCol.setOnEditCommit(new EventHandler<CellEditEvent<Transaction, String>>() {
			public void handle(CellEditEvent<Transaction, String> t) {
				((Transaction) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setNotes(t.getNewValue());
				ConcurrentProcesses.wasModifiedMyFinance();
				rowsModified.add(
						t.getTableView().getItems().get(t.getTablePosition().getRow()).transactionIdProperty().get());
			}
		});

		financeTable.setEditable(true);
		financeTable.setItems(data);
		financeTable.getColumns().addAll(idCol, conceptCol, debitCol, creditCol, notesCol);
	}

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
	 * 
	 * 
	 * *****************************************************************************
	 * 
	 * INTERIOR CLASSES
	 * 
	 *******************************************************************************/

	/**
	 * This class houses the Transaction object, in charge of holding the
	 * Transaction data, which includes ID, Concept, Debit, Credit and Notes.
	 * 
	 * @author Jaime Hisao Yesaki H
	 * @since 2017-10-07
	 *
	 */
	public static class Transaction {

		private SimpleIntegerProperty transactionId;
		private SimpleStringProperty concept;
		private SimpleDoubleProperty debit;
		private SimpleDoubleProperty credit;
		private SimpleStringProperty notes;

		/**
		 * Constructor for Transaction
		 * 
		 * @param tId
		 *            transaction ID, Integer
		 * @param concept1
		 *            the concept, String
		 * @param debit1
		 *            the debit amount of the transaction, Double
		 * @param credit1
		 *            the credit amount of the transaction, Double
		 * @param notes1
		 *            the notes of the transaction, String
		 * 
		 * @author Jaime Hisao Yesaki Hinojosa
		 * @since 2017-10-07
		 */
		public Transaction(int tId, String concept1, Double debit1, Double credit1, String notes1) {
			this.transactionId = new SimpleIntegerProperty(tId);
			this.concept = new SimpleStringProperty(concept1);
			this.debit = new SimpleDoubleProperty(debit1);
			this.credit = new SimpleDoubleProperty(credit1);
			this.notes = new SimpleStringProperty(notes1);
		}

		public void setDebit(Double double1) {
			// double newDouble = Double.parseDouble(double1);
			this.debit = new SimpleDoubleProperty(double1);
		}

		public void setCredit(Double double1) {
			// double newDouble = Double.parseDouble(double1);
			this.credit = new SimpleDoubleProperty(double1);
		}

		public void setNotes(String newValue) {
			this.notes = new SimpleStringProperty(newValue);
		}

		public void setConcept(String newValue) {
			this.concept = new SimpleStringProperty(newValue);
		}

		public SimpleIntegerProperty transactionIdProperty() {
			return transactionId;
		}

		public SimpleStringProperty conceptProperty() {
			return concept;
		}

		public SimpleStringProperty getConcept() {
			return concept;
		}

		public SimpleDoubleProperty debitProperty() {
			return debit;
		}

		public SimpleDoubleProperty creditProperty() {
			return credit;
		}

		public SimpleStringProperty notesProperty() {
			return notes;
		}

	}

	public static boolean isWasModified() {
		return wasModified;
	}

	public static void setWasModified(boolean wasModified2) {
		wasModified = wasModified2;
	}

}
