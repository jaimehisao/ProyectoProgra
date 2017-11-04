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
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
		} catch (NullPointerException e) {
			e.printStackTrace();
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

	}

	@SuppressWarnings("unused")
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

	// TODO: Remove suppresed warnings once finished
	/**
	 * This class is responsible for creating the Latest Transactions, which will be
	 * shown in the main Dashboard.
	 * 
	 * @author JHYH
	 * @since 2017-11-4
	 * @version 1.3
	 */
	@SuppressWarnings("unused")
	private class LatestTransactions {

		private int transactionId;
		private String transactionName;
		private String description;

		/**
		 * Constructor for the LatestTransactions Class, this will create the
		 * transactions as they are imported from the SQL database using the appropriate
		 * methods.
		 * 
		 * @param tId
		 *            the Transaction ID Number
		 * @param tranName
		 *            the Transaction Name
		 * @param tDesc
		 *            the Transaction Description
		 */
		public LatestTransactions(int tId, String tranName, String tDesc) {
			this.setTransactionId(tId);
			this.setTransactionName(tranName);
			this.setDescription(tDesc);
		}

		public int getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(int transactionId) {
			this.transactionId = transactionId;
		}

		public String getTransactionName() {
			return transactionName;
		}

		public void setTransactionName(String transactionName) {
			this.transactionName = transactionName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	// TODO: Remove suppressed warnings once finished

	/**
	 * This class is responsible for creating the Pending orders for MyBusiness,
	 * this will show which orders are pending to be delivered and show there
	 * parameters as well.
	 * 
	 * @author JYHJ
	 * @since 2017-11-4
	 * @version 1.5
	 *
	 */
	@SuppressWarnings("unused")
	private class PendingOrders {

		private int orderId;
		private String customerName;
		private String orderDesc;
		private double orderAmount;

		/**
		 * The Constructor for the Pending Orders Class, this class creates the Objects
		 * when queried from the Database.
		 * 
		 * @param oId the Order ID, equivalent to the Transaction ID, as an Integer
		 * @param customerName the name of the Customer that ordered the product, in String
		 * @param orderDesc the description of the Order, in String
		 * @param orderAmnt the amount of the order, in Double
		 */
		public PendingOrders(int oId, String customerName, String orderDesc, double orderAmnt) {
			this.orderId = oId;
			this.customerName = customerName;
			this.orderDesc = orderDesc;
			this.orderAmount = orderAmnt;
		}

	}

}
