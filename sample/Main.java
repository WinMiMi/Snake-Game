package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main extends Application {
	private AudioClip mouseClickSound;
	public static MediaPlayer backgroundMusicPlayer;

	@Override
	public void start(Stage primaryStage) {
		String mouseClick = "/audio/mouse_click.mp3";
		mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
		mouseClickSound.play();

		String backgroundMusicFile = "/audio/background_music1.mp3";
		Media backgroundMusic = new Media(getClass().getResource(backgroundMusicFile).toExternalForm());
		backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
		backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		backgroundMusicPlayer.play();
		try {

			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("MainUI.fxml"));
			Scene scene = new Scene(root);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
