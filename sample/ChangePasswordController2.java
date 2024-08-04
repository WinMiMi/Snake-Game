package sample;

import java.awt.Desktop;
import java.net.URI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import model.UserDAO;

public class ChangePasswordController2 {

	@FXML
	private Button btnConfirm;

	@FXML
	private Label lblStatus;

	@FXML
	private Button btnHome;

	@FXML
	private PasswordField pfPassword1;

	@FXML
	private PasswordField pfPassword2;

	private final UserDAO userDAO = new UserDAO();

	@FXML
	void processConfirm(ActionEvent event) {
		String newPassword1 = pfPassword1.getText();
		String newPassword2 = pfPassword2.getText();

		if (newPassword1.equals(newPassword2)) {
			String authenticatedUsername = AuthenticationService.getCurrentUser().getUsername();

			boolean passwordChanged = userDAO.changePassword(authenticatedUsername, newPassword2);

			if (passwordChanged) {
				lblStatus.setText("Password changed successfully!");
				ChangeSceneController changescene = ChangeSceneController.getInstance();
				changescene.changeScene("HomeUI.fxml", event, "Home UI");

			} else {
				lblStatus.setText("Failed to change password. Please try again.");
			}
		} else {
			lblStatus.setText("Passwords do not match. Please enter the same password in both fields.");
		}

	}

	@FXML
	void processgotoHome(ActionEvent event) {
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HomeUI.fxml", event, "Home UI");

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

}
