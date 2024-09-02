package sample;

import java.awt.Desktop;
import java.net.URI;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import model.UserDAO;

public class DeleteAccountController2 {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnGmail;

    @FXML
    private Button btnHome;

    @FXML
    private Label lblStatus;
    
    private final UserDAO userDAO = new UserDAO();

	private AudioClip mouseClickSound;


    @FXML
    void processDelete(ActionEvent event) {
    	String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
    	String currentUsername = AuthenticationService.getCurrentUser().getUsername();
        boolean accountDeleted = userDAO.deleteAccount(currentUsername);

        if (accountDeleted) {
            lblStatus.setText("Account deleted successfully!");
            lblStatus.setStyle("-fx-text-fill: green;");
            PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
            pause.setOnFinished(e -> {
                // Continue with your logic or scene change here
                // For example, go back to the home screen
        	ChangeSceneController changescene = ChangeSceneController.getInstance();
    		changescene.changeScene("SignupUI.fxml", event, "Signup UI");
            });
            pause.play();
    		
        } else {
            lblStatus.setText("Failed to delete account. Please try again.");
            lblStatus.setStyle("-fx-text-fill: red;");
        }
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
	void processgotoHome(ActionEvent event) {
    	String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
		ChangeSceneController changescene = ChangeSceneController.getInstance();
		changescene.changeScene("HomeUI.fxml", event, "Home UI");

	}



}
