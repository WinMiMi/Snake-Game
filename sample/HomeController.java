package sample;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import model.User;

public class HomeController implements Initializable {

	@FXML
	private Button btnExit;

	@FXML
	private Button btnHelp;

	@FXML
	private Button btnHistory;

	@FXML
	private Button btnPlay;

	@FXML
	private Button btnSettings;

	@FXML
	private Label lblStatus;
	public static User user;

	private AudioClip mouseClickSound;


	public void showWelcomeStatus(String username) {
		lblStatus.setText(username.toUpperCase() + "!");

	}

	@FXML
	private Button btnGmail;

	@FXML
	void processGmail(ActionEvent event) {
		String gmailUrl = "https://mail.google.com/mail/u/0/?view=cm&fs=1&tf=1&to=cutosnakegame@gmail.com";

		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();

				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(new URI(gmailUrl));
				} else {
					System.out.println("Desktop browsing is not supported.");
				}
			} else {
				System.out.println("Desktop is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void processExit(ActionEvent event) {

		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();

		Platform.runLater(() -> {
			cleanupBeforeExit();
			System.out.println("Exiting application...");
			Platform.exit();
		});
	}

	@FXML
	void processSettings(ActionEvent event) {

		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("SettingsUI.fxml", event, "Settings UI");


	}

	private void cleanupBeforeExit() {
		System.out.println("Performing cleanup before exit..");

	}

	public void processHelp(ActionEvent event) {

		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();

		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HelpUI.fxml", event, "Settings UI");

	}

	@FXML
	void processgotoPlayGame(ActionEvent event) {
		
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("Customization.fxml", event, "Customization UI");

	}

	@FXML
	public void processgotoHistory(ActionEvent event) {

		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HistoryUI.fxml", event, "History UI");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		user = MainController.user;
		System.out.println("In Home Controller : User from main controller" + user);
	}

}
