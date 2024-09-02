package sample;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.media.AudioClip;

public class SignupController {

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnSignup;

	@FXML
	private PasswordField pfPassword;

	@FXML
	private TextField tfEmail;

	@FXML
	private RadioButton rbFemale;

	@FXML
	private RadioButton rbMale;

	@FXML
	private TextField tfUsername;

	private String gender = "";

	private AudioClip mouseClickSound;


	private ToggleGroup genderToggleGroup;

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
	public void initialize() {
		genderToggleGroup = new ToggleGroup();

		rbMale.setToggleGroup(genderToggleGroup);
		rbFemale.setToggleGroup(genderToggleGroup);
	}

	@FXML
	void processGender(ActionEvent event) {
		RadioButton selectedRadioButton = (RadioButton) genderToggleGroup.getSelectedToggle();

		if (selectedRadioButton != null) {
			gender = selectedRadioButton.getText();
		}
	}

	@FXML
	void processSignup(ActionEvent event) throws SQLException {

		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();

		Connection connection = null;
		PreparedStatement psInsert = null;
		PreparedStatement psCheckUserExists = null;
		ResultSet resultSet = null;

		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakegame", "root", "1234");
		psCheckUserExists = connection.prepareStatement("select * from users where username = ?");
		psCheckUserExists.setString(1, tfUsername.getText().trim());
		resultSet = psCheckUserExists.executeQuery();

		if (resultSet.isBeforeFirst()) {
			System.out.println("user have already existed!");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("You cannot use that username");
			alert.show();
			ChangeSceneController changescene = ChangeSceneController.getInstance();
			changescene.changeScene("Signup.fxml", event, "Signup UI");

		} else {
			psInsert = connection
					.prepareStatement("insert into users(username, password, email, gender) values(?,?,?,?) ");
			psInsert.setString(1, tfUsername.getText().trim());
			psInsert.setString(2, pfPassword.getText().trim());
			psInsert.setString(3, tfEmail.getText().trim());
			psInsert.setString(4, gender);
			psInsert.executeUpdate();

		}
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("MainUI.fxml", event, "Main UI");

	}

	@FXML
	void processgotoLogin(ActionEvent event) throws IOException {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("MainUI.fxml", event, "Main UI");

	}

}
