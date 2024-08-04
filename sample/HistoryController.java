package sample;

import java.awt.Desktop;
import java.net.URI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import model.Mode;

public class HistoryController {

	@FXML
	private Button btnHome;

	@FXML
	private Label lblStatus;

	@FXML
	private Label lblTitle;

	private AudioClip mouseClickSound;
	public static Mode normalmode;
	public static Mode endlessMode;

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
	void processgotoHome(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		System.out.println("History UI");
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HomeUI.fxml", event, "History UI");
	}

	@FXML
	void processgotoNormal(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		normalmode = normalmode.NORMAL;
		System.out.println("Normal History UI");

		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("NormalHistoryUI.fxml", event, "History Normal UI");
	}

	@FXML
	void processgotoendeless(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		endlessMode = endlessMode.ENDLESS;
		System.out.println("Endless History UI");
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("EndlessHistoryUI.fxml", event, "History Endless UI");
	}

	@FXML
	void processViewDetails(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("DetailUI.fxml", event, "Detail UI");
	}

}
