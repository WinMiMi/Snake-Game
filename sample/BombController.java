package sample;

import java.awt.Point;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import model.Mode;

public class BombController {

	public static final String[] BOMB_IMAGE = new String[] { "/img/ic_bomb.png" };

	public static Image bombImage;
	public static int bombX;
	public static int bombY;

	public static Mode mode;

	public static MediaPlayer backgroundMusicPlayer;
	public static AudioClip explosionSound;
	public static AudioClip gameOverSound;
	public static AudioClip laserSound;
	public static AudioClip mouseClickSound;
	public static String laserShotFile;

	public static void generateBomb() {
		mode = CustomizationController.mode;
		switch (mode) {
		case NORMAL: {
			start: while (true) {

				bombX = (int) (Math.random() * Game.ROWS);
				bombY = (int) (Math.random() * Game.COLUMNS);

				if (bombX == FoodsController.foodX && bombY == FoodsController.foodY) {
					continue start;
				}

				for (Point snake : Game.snakeBody) {
					if (snake.getX() == bombX && snake.getY() == bombY) {
						continue start;
					}
				}
				bombImage = new Image(BOMB_IMAGE[(int) (Math.random() * BOMB_IMAGE.length)]);
				break;
			}
		}
			break;
		case ENDLESS:
			break;
		}
	}

	public static void drawBomb(GraphicsContext gc) {
		mode = CustomizationController.mode;
		switch (mode) {
		case NORMAL:
			gc.drawImage(bombImage, bombX * Game.SQUARE_SIZE, bombY * Game.SQUARE_SIZE, Game.SQUARE_SIZE,
					Game.SQUARE_SIZE);
			break;
		case ENDLESS:
			break;
		}
	}

	public void eatBomb() {
		mode = CustomizationController.mode;
		switch (mode) {
		case NORMAL: {
			if (Game.snakeHead.getX() == bombX && Game.snakeHead.getY() == bombY) {

				generateBomb();
				Game.score -= 5;
				Game.lives -= 1;
				FoodsController.generateFood();

				// eat dynamite sound
				String explosionSoundFile = "/audio/eat_dynamite_audio.mp3";
				explosionSound = new AudioClip(getClass().getResource(explosionSoundFile).toExternalForm());
				explosionSound.play();
				Game.backgroundMusicPlayer.setVolume(0.5);
			}
			Game.backgroundMusicPlayer.setVolume(1);
		}
			break;
		case ENDLESS:
			break;
		}
	}
}
