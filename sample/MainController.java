package sample;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.User;
import model.UserDAO;
import model.UserMode;

public class MainController implements Initializable {

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnSignup;

	@FXML
	private PasswordField pfPassword;

	@FXML
	private TextField tfUsername;

	@FXML
	private CheckBox cbRememberMe;

	@FXML
	private Label lblStatus;

	@FXML
	private Button btnGuess;

	public static User user;
	public static UserMode guest;

	private final UserDAO userDAO = new UserDAO();

	private AudioClip mouseClickSound;

	@FXML
	void processLogin(ActionEvent event) throws IOException {

		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();

		String username = tfUsername.getText().trim();
		String password = pfPassword.getText().trim();

		boolean loginStatus;
		loginStatus = userDAO.validate(username, password);

		if (loginStatus) {
			lblStatus.setTextFill(Paint.valueOf("green"));
			lblStatus.setText("Your login access is granted");
			user = userDAO.getUserByUsername(username);
			AuthenticationService.setCurrentUser(username);
			if (cbRememberMe.isSelected()) {
				userDAO.rememberMeToFile(username, password);
			}
			if (user == null) {
				return;
			}
			ChangeSceneController changescene = ChangeSceneController.getInstance();
			changescene.changeScene("HomeUI.fxml", event, "Home UI", username);
			// changescene.changeScene("HomeUI.fxml", event, "Home UI");

		} else {
			lblStatus.setTextFill(Color.RED);
			lblStatus.setText("Username or password is incorrect!");

		}

	}

	@FXML
	void processRememberMe(ActionEvent event) {
		if (cbRememberMe.isSelected()) {
			user = MainController.user;
			userDAO.rememberMeToFile(user.getUsername(), user.getPassword());
		}

	}

	@FXML
	void processgotoSignup(ActionEvent event) throws IOException {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("SignupUI.fxml", event, "Signup UI");

	}

	@FXML
	void processGuess(ActionEvent event) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		guest = UserMode.GUEST;
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("Customization.fxml", event, "Customization UI");
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		String[] storedCredentials = userDAO.getStoredCredentials();
		if (storedCredentials.length == 2) {
			tfUsername.setText(storedCredentials[0]);
			pfPassword.setText(storedCredentials[1]);
			cbRememberMe.setSelected(true);

		}

	}

}
