package application;

import controllers.ConcurrentProcesses;
import controllers.MyBusinessController;
import controllers.MyFinanceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * @author Jaime Hisao Yesaki Hinojosa
 * @since 2017-9-18
 *
 */

public class Main extends Application {

	/*
	 * *****************************************************************************
	 * 
	 * VARIABLES
	 * 
	 *******************************************************************************/

	// Stage 2 - For PopUp Windows
	public static Stage popupStage;

	// Primary Stage to be enabled in other methods
	static Stage thisStage;

	// The Main Dashboard Component
	Parent mainDashboard;
	public static Scene dashboardScene;

	// Login Component
	Parent loginScreen;
	Scene loginScene;

	// New User Component
	Parent newUser;
	static Scene newUserScene;

	// New Customer Component
	Parent newCustomer;
	static Scene newCustomerScene;

	// Master Setup Component
	Parent masterConfig;
	static Scene masterConfigScene;

	// Exportable Primary Stage
	Stage exportableStage;

	@Override
	public void start(Stage primaryStage) {

		exportableStage = primaryStage;
		// primaryStage.setOnCloseRequest(e -> Platform.exit());

		// Makes PrimaryStage available in other methods
		thisStage = primaryStage;

		try {
			// Load Login FXML, Scene and apply CSS
			loginScreen = FXMLLoader.load(getClass().getResource("/application/AppLoginGUI.fxml"));
			loginScene = new Scene(loginScreen);
			loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// // Load Dashboard FXML, Scene and apply CSS
			// mainDashboard =
			// FXMLLoader.load(getClass().getResource("/application/MainDashboardGUI.fxml"));
			// dashboardScene = new Scene(mainDashboard);
			// dashboardScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Load New User
			newUser = FXMLLoader.load(getClass().getResource("/application/NewUserSetupGUI.fxml"));
			newUserScene = new Scene(newUser);

			// Master Config
			masterConfig = FXMLLoader.load(getClass().getResource("/application/MasterSetupGUI.fxml"));
			masterConfigScene = new Scene(masterConfig);

			// New Customer Popup
			newCustomer = FXMLLoader.load(getClass().getResource("/application/AddNewCustomer.fxml"));
			newCustomerScene = new Scene(newCustomer);

			// Set Scene on the Primary Stage
			primaryStage.setScene(loginScene);
			thisStage.setTitle("Login");
			primaryStage.show();

			// make another stage for scene2 -- SQL SETUP
			popupStage = new Stage();
			popupStage.setScene(newUserScene);
			// tell stage it is meant to pop-up
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Pop up window");

			ConcurrentProcesses b = new ConcurrentProcesses();
			Thread t = new Thread(b);
			t.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * *****************************************************************************
	 * 
	 * METHODS
	 * 
	 *******************************************************************************/

	public Stage getExportableStage() {
		return exportableStage;
	}

	/**
	 * Method linking Main with any class that requires to change the Scene in the
	 * Main Stage and centers the Stage on the screen
	 * 
	 * @param scene
	 *            the scene to be changed
	 * @author Jaime Hisao Yesaki Hinojosa
	 */
	public static void setScene(Scene scene) {
		thisStage.setScene(scene);
		// Sets the Stage in the middle of the screen
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		thisStage.setX((primScreenBounds.getWidth() - thisStage.getWidth()) / 2);
		thisStage.setY((primScreenBounds.getHeight() - thisStage.getHeight()) / 2);
	}

	/**
	 * @param title
	 *            a String to set the title of the current stage.
	 */
	public static void setStageTitle(String title) {
		thisStage.setTitle(title);
	}

	public static void showUserPopup() {
		popupStage.setScene(newUserScene);
		popupStage.setTitle("Crear Nuevo Usuario");
		popupStage.showAndWait();
	}

	public static void hideUserPopup() {
		popupStage.close();
	}

	public static void showMasterSetup() {
		popupStage.setScene(masterConfigScene);
		popupStage.setTitle("Configuracion Maestra");
		popupStage.showAndWait();
	}

	public static void hideMasterSetup() {
		popupStage.close();
	}

	public static void showAddNewCustomer() {
		popupStage.setScene(newCustomerScene);
		popupStage.setTitle("Agregar Nuevo Cliente");
		popupStage.showAndWait();
	}

	public static void hideAddNewCustomer() {
		popupStage.close();
	}

	@Override
	public void stop() {
		System.out.println("Stage is closing");
		ConcurrentProcesses.stopProcess();
		if (MyBusinessController.isWasModified() || MyFinanceController.isWasModified()) {
			MyBusinessController.updateModifiedRow();
			MyFinanceController.updateModifiedRow();
		}
		ConcurrentProcesses.stopProcess();
	}

	/*
	 * *****************************************************************************
	 * 
	 * MAIN
	 * 
	 *******************************************************************************/

	public static void main(String[] args) {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "HomeOffice");
		launch(args);
	}
}
