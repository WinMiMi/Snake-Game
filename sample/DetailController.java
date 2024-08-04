package sample;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.AudioClip;
import model.History;
import model.HistoryDAO;
import model.User;

public class DetailController implements Initializable {

	@FXML
	private Button btnHistory;

	@FXML
	private Button btnHome;

	@FXML
	private TableView<History> detailTable;

	@FXML
	private TableColumn<History, LocalTime> duration;

	@FXML
	private TableColumn<History, LocalTime> end_time;

	@FXML
	private TableColumn<History, String> mode;

	@FXML
	private Label lblStatus;

	@FXML
	private Label lblTitle;

	@FXML
	private TableColumn<History, LocalTime> start_time;

	@FXML
	private TableColumn<History, Integer> total_fruit_eaten;

	@FXML
	private TableColumn<History, Integer> total_score;

	@FXML
	private TableColumn<History, Long> index;

	private HistoryDAO historyDAO = new HistoryDAO();
	public User user;
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
	void processgotoHistory(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HistoryUI.fxml", event, "History UI");
	}

	@FXML
	void processgotoHome(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HomeUI.fxml", event, "Home UI");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		user = MainController.user;

		ObservableList<History> allHistory = (ObservableList<History>) historyDAO.getAllScoresByUserId(user.getId());
		for (Iterator<History> iterator = allHistory.iterator(); iterator.hasNext();) {
			history = (History) iterator.next();
			System.out.println("In detail controller" + history);
		}
		if (user != null) {
			lblStatus.setText("Detail History of " + user.getUsername().toUpperCase() + "!");
		} else {
			System.out.println("Detail History of unknown player");
		}

		index.setCellValueFactory(cellData -> {
			long index = detailTable.getItems().indexOf(cellData.getValue());
			return new SimpleLongProperty(index + 1).asObject();
		});

		total_score.setCellValueFactory(new PropertyValueFactory<>("total_score"));
		total_fruit_eaten.setCellValueFactory(new PropertyValueFactory<>("total_fruit_eaten"));
		start_time.setCellValueFactory(new PropertyValueFactory<>("start_time"));
		end_time.setCellValueFactory(new PropertyValueFactory<>("end_time"));
		mode.setCellValueFactory(new PropertyValueFactory<>("mode"));
		duration.setCellValueFactory(cellData -> {
			ObservableList<LocalTime> timeIntervals = historyDAO.getTimeInterval(user.getId());
			int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());

			if (rowIndex >= 0 && rowIndex < timeIntervals.size()) {
				LocalTime timeInterval = timeIntervals.get(rowIndex);
				return new SimpleObjectProperty<>(timeInterval);
			} else {
				return new SimpleObjectProperty<>(null);
			}
		});

		detailTable.setItems(historyDAO.getAllScoresByUserId(user.getId()));
	}

}
