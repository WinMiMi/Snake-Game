package sample;

import java.awt.Desktop;

import java.net.URI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import model.UserDAO;

public class SettingsController {

	@FXML
	private Button btnGmail;

	@FXML
	private Button btnDeleteAccount;

	@FXML
	private Button btnGotoHome;

	@FXML
	private Button btnLogout;

	@FXML
	private Button btnChangePassword;

	private AudioClip mouseClickSound;

	private final UserDAO userDAO = new UserDAO();

	private String username;

	public void setUsername(String username) {
		this.username = username;
	}

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
	public void processChangePassword(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("ChangePasswordUI1.fxml", event, "ChangePassword UI1");

	}
	
	@FXML
	public void processDeleteAccount(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("DeleteAccountUI1.fxml", event, "DeleteAccount UI");
		
	}


	@FXML
	void processGotoHome(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HomeUI.fxml", event, "Home UI");

	}

	@FXML
	void processLogout(ActionEvent event) {

		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("MainUI.fxml", event, "Main UI");

	}

}
