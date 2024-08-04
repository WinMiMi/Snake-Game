package sample;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import model.History;
import model.HistoryDAO;
import model.Mode;
import model.User;

public class HistoryNormalController implements Initializable {

	@FXML
	private Button btnHome;

	@FXML
	private Label lblBestTime;

	@FXML
	private Label lblHighestTotalScore;

	@FXML
	private Label lblLatestScore;

	@FXML
	private Label lblTitle;

	@FXML
	private Label lblTotalGamePlayed;

	private final HistoryDAO historyDAO = new HistoryDAO();

	public static User user;
	public static Mode normalmode;
	// public static String historyMode;
	private AudioClip mouseClickSound;
	public static History history;

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
		System.out.println("Home UI");
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HomeUI.fxml", event, "History UI");

	}

	@FXML
	void processViewDetails(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("DetailUI.fxml", event, "Detail UI");
	}

	@SuppressWarnings("unused")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		user = MainController.user;
		normalmode = HistoryController.normalmode;
		System.out.println("In the normal history : user from Game" + user);

		ObservableList<History> allHistory = historyDAO.getAllScoresByUserIdAndMode(user.getId(),
				normalmode.toString());
		System.out.println(allHistory.size());
		System.out.println("Normal History" + normalmode);

		for (Iterator<History> iterator = allHistory.iterator(); iterator.hasNext();) {
			history = (History) iterator.next();
			System.out.println(history);
		}

		lblHighestTotalScore.setText(String.valueOf(historyDAO.getHighestScore(user.getId(), normalmode.toString())));
		lblTotalGamePlayed.setText(String.valueOf(historyDAO.getTotalPlayedGame(user.getId(), normalmode.toString())));
		if (history != null) {
			lblLatestScore.setText(String.valueOf(history.getTotal_score()));
			System.out.println("Latest score : " + history.getTotal_score());
		} else {
			System.out.println("Latest score : 1");
			lblLatestScore.setText("0");

		}

		LocalTime besttime = historyDAO.getBestTime(user.getId(), normalmode.toString());
		lblBestTime.setText(besttime.format(DateTimeFormatter.ofPattern("mm:ss")));

	}
}
