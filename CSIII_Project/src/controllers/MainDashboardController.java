package controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import application.Main;
import controllers.MyBusinessController.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MainDashboardController implements Initializable {

	/*
	 * *****************************************************************************
	 * 
	 * VARIABLES
	 * 
	 *******************************************************************************/

	@FXML
	private static JFXDrawer drawer;
	@FXML
	private JFXHamburger hamburgerMain;
	@FXML
	private MenuBar menuBar = new MenuBar();

	@FXML
	private TableView<PendingOrders> pendingOrdersTable = new TableView<PendingOrders>();;

	@FXML
	private TableView<ToDo> todoTable = new TableView<ToDo>();;

	@FXML
	private TableView<LatestTransactions> lastTransactionsTable = new TableView<LatestTransactions>();;

	@FXML
	private Label myAsssetsLabel;

	// Create Menus
	@FXML
	Menu fileMenu = new Menu("File");
	@FXML
	Menu editMenu = new Menu("Edit");
	@FXML
	Menu windowMenu = new Menu("Window");
	@FXML
	Menu helpMenu = new Menu("Help");
	@FXML
	MenuItem newUserSetup = new MenuItem("Crear Nuevo Usuario"); // For triggering the setup tab.
	@FXML
	MenuItem logOut = new MenuItem("Log Out"); // For triggering the setup tab.

	

	/*
	 * *****************************************************************************
	 * 
	 * METHODS
	 * 
	 *******************************************************************************/

	// SHOWS USER SETUP MENU
	public void addNewUserPressed(ActionEvent Event) {
		Main.showUserPopup();
	}

	// Saves all of the User's data and then logs the user out of the session and
	// back into the login screen.
	public void logOutPressed(ActionEvent Event) {

	}

	/*
	 * *****************************************************************************
	 * 
	 * INITIALIZE
	 * 
	 *******************************************************************************/

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*
		 * *****************************************************************************
		 * 
		 * PROPERTY MODIFIERS
		 * 
		 *******************************************************************************/

		menuBar.setUseSystemMenuBar(true);

		/*
		 * *****************************************************************************
		 * 
		 * TRANSITIONS AND ACTION LISTENERS WITHIN INITIALIZE
		 * 
		 *******************************************************************************/

		VBox box;
		try {
			box = FXMLLoader.load(getClass().getResource("/controllers/DrawerContent.fxml"));
			drawer.setSidePane(box);
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburgerMain);
		transition.setRate(-1);
		hamburgerMain.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();

			if (drawer.isShown()) {
				drawer.close();
			} else {
				drawer.open();
			}

		});

		createAllTables();

	}

	private void createAllTables() {
		// TODO Auto-generated method stub

	}

	private class ToDo {

		private SimpleStringProperty taskName;
		private Date dueDate;

		public SimpleStringProperty taskNameProperty() {
			return taskName;
		}

		public void setTaskName(SimpleStringProperty taskName) {
			this.taskName = taskName;
		}

		public Date dueDateProperty() {
			return dueDate;
		}

		public void setDueDate(String dueDate) {
			try {
				SimpleDateFormat originalFormat = new SimpleDateFormat("ddMMyyyy");
				this.dueDate = originalFormat.parse(dueDate.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public ToDo(String tName, Integer cDate) {
			this.taskName = new SimpleStringProperty(tName);
			try {
				SimpleDateFormat originalFormat = new SimpleDateFormat("ddMMyyyy");
				this.dueDate = originalFormat.parse(cDate.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private class LatestTransactions {

		public LatestTransactions() {

		}

	}

	private class PendingOrders {

		public PendingOrders() {

		}

	}

}
