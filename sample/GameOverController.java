package sample;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import model.HistoryDAO;

public class GameOverController implements Initializable {

	@FXML
	private Button btnExit;

	@FXML
	private Button btnHome;

	@FXML
	private Button btnHistory;

	@FXML
	private Button btnRestart;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblTitle;

	@FXML
	private Label lblTotalFruitEaten;

	@FXML
	private Label lblTotalScore;

	private final HistoryDAO historyDAO = new HistoryDAO();

	private AudioClip mouseClickSound;

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

		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HomeUI.fxml", event, "Home UI");

	}

	@FXML
	void processgotoGameRestart(ActionEvent event) {

		Game game = new Game(false);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		try {
			game.gameOver = false;
			game.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void processgotoHistory(ActionEvent event) {

		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HistoryUI.fxml", event, "History UI");
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

	private void cleanupBeforeExit() {
		System.out.println("Performing cleanup before exit..");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		lblTotalFruitEaten.setText(String.valueOf(Game.totalfruitEaten));
		lblTotalScore.setText(String.valueOf(Game.score));
		lblTime.setText(Game.formattedTime);

	}

}
