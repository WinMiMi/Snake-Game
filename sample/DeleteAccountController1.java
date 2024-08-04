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
import javafx.scene.control.PasswordField;
import javafx.scene.media.AudioClip;

public class DeleteAccountController1 {

    @FXML
    private Button btnContinue;

    @FXML
    private Button btnGmail;

    @FXML
    private Button btnHome;

    @FXML
    private Label lblStatus;

    @FXML
    private PasswordField pfPassword;
    
    private Connection connection;

	private AudioClip mouseClickSound;


    @FXML
    void processContinue(ActionEvent event) {
    	String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();
        String enteredPassword = pfPassword.getText();

        if (passwordMatches(enteredPassword)) {
            
    		ChangeSceneController changescene = ChangeSceneController.getInstance();
    		changescene.changeScene("DeleteAccountUI2.fxml", event, "DeleteAccount UI");
            
        } else {
            // Password is incorrect
        	lblStatus.setVisible(true);
            lblStatus.setText("Password is incorrect!");
            lblStatus.setStyle("-fx-text-fill: red;"); 
        }
    }

    private boolean passwordMatches(String enteredPassword) {
        try {
            connection = DBConnection.getConnection();
            String query = "select * from users where password = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, enteredPassword);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return false;
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
