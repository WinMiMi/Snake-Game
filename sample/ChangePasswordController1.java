package sample;

import java.awt.Desktop;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.UserDAO;

public class ChangePasswordController1 {

	@FXML
	private Button btnContinue;

	@FXML
	private Label lblStatus;

	@FXML
	private Button btnHome;

	@FXML
	private TextField tfUsername;

	private final UserDAO userDAO = new UserDAO();

	private Connection connection;
	private PreparedStatement pStmt;
	private ResultSet rs;

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
	void processContinue(ActionEvent event) {
		String enteredUsername = tfUsername.getText().trim();

		if (usernameExistsInDatabase(enteredUsername)) {
			lblStatus.setText("Username is valid.");

			ChangeSceneController changescene = ChangeSceneController.getInstance();
			changescene.changeScene("ChangePasswordUI2.fxml", event, "ChangePassword UI2");

		} else {
			lblStatus.setText("Invalid username!");

		}

	}

	public boolean usernameExistsInDatabase(String username) {
		try {
			connection = DBConnection.getConnection();
			pStmt = connection.prepareStatement("select * from users where username = ?");
			pStmt.setString(1, username);
			rs = pStmt.executeQuery();

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@FXML
	void processgotoHome(ActionEvent event) {
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HomeUI.fxml", event, "Home UI");

	}

}
