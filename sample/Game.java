package sample;

import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Mode;
import model.Skin;
import model.Theme;
import model.User;
import model.UserMode;

public class Game extends Application {

	public static final int WIDTH = 600;
	public static final int HEIGHT = WIDTH;
	public static final int ROWS = 20;
	public static final int COLUMNS = ROWS;
	public static final int SQUARE_SIZE = WIDTH / ROWS;
	public static final String[] SNAKEHEAD_IMAGE = new String[] { "/img/skin1.png", "/img/red.png", "/img/skin3.png",
			"/img/skin4.png", "/img/pink.png", "/img/skin11.png", "/img/skin7.png" };
	public static final String[] SNAKEHEAD_CUSTOM = new String[] { "/img/emoji_poison.png", "/img/emoji_smiley.png" };

	private static final int RIGHT = 0;
	private static final int LEFT = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;

	public static GraphicsContext gc;
	public static List<Point> snakeBody = new ArrayList();
	public static Point snakeHead;
	public static Image foodImage;
	public static int foodX;
	public static int foodY;
	public static Image bombImage;
	public static int bombX;
	public static int bombY;
	private static Image lifeImage;
	private static int lifeX;
	private static int lifeY;
	public static Image snakeHeadImage;
	public boolean gameOver;
	private int currentDirection;
	public static int score = 0;
	public static int lives = 3;
	public static int totalfruitEaten = 0;
	public LocalDateTime start;
	public LocalDateTime end;
	public static String formattedTime;
	public static KeyEvent event;
	public Timeline timeline;
	public static Stage primaryStage;
	private static Scene primaryscene;

	public static Button gameOverButton;

	public static MediaPlayer backgroundMusicPlayer;
	public AudioClip explosionSound;
	public AudioClip gameOverSound;
	public AudioClip laserSound;
	public AudioClip mouseClickSound;

	public static User user;
	public static Skin skin;
	public static Theme theme;
	public static Mode mode;
	public static Font customFont;
	public static UserMode guest;

	public Stage GamePlay(Stage stage) {

		primaryStage = stage;

		primaryStage.setTitle("Snake");
		StackPane root = new StackPane();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);

		gameOverButton = new Button("Game Over");

		gameOverButton.setStyle("-fx-background-color: #45FFCA;" + "-fx-text-fill: black;" + "-fx-font-size: 25px;"
				+ "-fx-background-radius: 30;"
				+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

		gameOverButton.setVisible(false);

		root.getChildren().add(canvas);
		root.getChildren().add(gameOverButton);
		primaryscene = new Scene(root);
		primaryStage.setScene(primaryscene);
		primaryStage.show();
		primaryStage.setResizable(false);

		gc = canvas.getGraphicsContext2D();

		drawBackground(gc);

		FoodsController.drawFood(gc);
		BombController.drawBomb(gc);
		drawScore(gc);
		timeDuration();
		return stage;

	}

	public Game(boolean gameOver) {
		super();
		this.gameOver = gameOver;

	}

	@Override
	public void start(Stage stage) throws Exception {

		customFont = Font.loadFont(getClass().getResourceAsStream("/font/VT323-Regular.ttf"), 25);
		start = LocalDateTime.now();
		user = MainController.user;
		skin = CustomizationController.skin;
		theme = CustomizationController.theme;
		mode = CustomizationController.mode;
		
		primaryStage = setPrimaryStage(GamePlay(stage));
		GamePlay(primaryStage);
		primaryscene = primaryStage.getScene();
		primaryscene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				KeyCode code = event.getCode();
				if (code == KeyCode.RIGHT || code == KeyCode.D) {
					if (currentDirection != LEFT) {
						currentDirection = RIGHT;
					}
				} else if (code == KeyCode.LEFT || code == KeyCode.A) {
					if (currentDirection != RIGHT) {
						currentDirection = LEFT;
					}
				} else if (code == KeyCode.UP || code == KeyCode.W) {
					if (currentDirection != DOWN) {
						currentDirection = UP;
					}
				} else if (code == KeyCode.DOWN || code == KeyCode.S) {
					if (currentDirection != UP) {
						currentDirection = DOWN;
					}
				}
			}
		});

		for (int i = 0; i < 3; i++) {
			snakeBody.add(new Point(5, ROWS / 2));
		}
		snakeHead = snakeBody.get(0);
		FoodsController.generateFood();
		BombController.generateBomb();

		Main.backgroundMusicPlayer.stop();

		String backgroundMusicFile = "/audio/snakecharmer.mp3";
		Media backgroundMusic = new Media(getClass().getResource(backgroundMusicFile).toExternalForm());
		backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
		backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		backgroundMusicPlayer.play();

		timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(130), e -> run(gc)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

	}

	public void run(GraphicsContext gc) {

		if (gameOver) {
			backgroundMusicPlayer.stop();
			timeline.stop();

			String gameOverClick = "/audio/game-over-sound.mp3";
			gameOverSound = new AudioClip(getClass().getResource(gameOverClick).toExternalForm());
			gameOverSound.play();

			gameOverButton.setVisible(true);
			gameOverButton.setOnAction(event -> handleGameOver(event));
			snakeBody.removeAll(snakeBody);

			return;
		}

		drawBackground(gc);
		FoodsController.drawFood(gc);
		drawSnake(gc);
		BombController.drawBomb(gc);
		drawScore(gc);

		for (int i = snakeBody.size() - 1; i >= 1; i--) {
			snakeBody.get(i).x = snakeBody.get(i - 1).x;
			snakeBody.get(i).y = snakeBody.get(i - 1).y;
		}

		switch (currentDirection) {
		case RIGHT:
			moveRight();
			break;
		case LEFT:
			moveLeft();
			break;
		case UP:
			moveUp();
			break;
		case DOWN:
			moveDown();
			break;
		}

		gameOver();
		FoodsController foodsController = new FoodsController();
		foodsController.eatFood();
		BombController bombController = new BombController();
		bombController.eatBomb();
		timeDuration();
	}

	public Stage getPrimaryStage() {

		GamePlay(primaryStage);
		return primaryStage;
	}

	public Stage setPrimaryStage(Stage stage) {

		primaryStage = GamePlay(stage);

		return primaryStage;
	}

	public static Scene getGameScene() {
		return primaryscene;
	}

	public static Scene setGameScene(Scene scene) {
		primaryscene = scene;
		return scene;
	}

	private static void drawBackground(GraphicsContext gc) {

		switch (theme) {
		case LIGHT: {
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLUMNS; j++) {
					if ((i + j) % 2 == 0) {
						gc.setFill(Color.web("AAD751"));
					} else {
						gc.setFill(Color.web("A2D149"));
					}
					gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				}
			}
		}

			break;

		case DARK: {
			gc.setFill(Color.web("#333333")); // Midnight Blue Background
			gc.fillRect(0, 0, WIDTH, HEIGHT);

			// gc.setStroke(Color.web("#D8BFD8"));
			gc.setLineWidth(1);

			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLUMNS; j++) {
					gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				}
			}
			break;
		}
		}

		switch (skin) {
		case LAVENDER: {
			switch (theme) {
			case LIGHT:
				gc.setFill(Color.web("#E6E6FA"));
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#D8BFD8"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			case DARK:
				gc.setFill(Color.web("#2E2E3A"));
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#D8BFD8"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			}
		}

			break;

		case RED: {
			switch (theme) {
			case LIGHT:
				gc.setFill(Color.web("#F5F5F5"));
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#D8BFD8"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			case DARK:
				gc.setFill(Color.web("#2B2B2B")); // Midnight Blue Background
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#666666"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			}
		}
			break;

		case PINK: {
			switch (theme) {
			case LIGHT:
				gc.setFill(Color.web("#FFE4B5"));
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#D8BFD8"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			case DARK:
				gc.setFill(Color.web("#2E4053")); // Midnight Blue Background
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#D8BFD8"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			}
		}
			break;

		case GREEN: {
			switch (theme) {
			case LIGHT:
				gc.setFill(Color.web("#CCFFCC"));
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#669966"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			case DARK:
				gc.setFill(Color.web("#1B512D")); // Midnight Blue Background
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#CCCCCC"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			}
		}
			break;

		case ASHAMED: {
			switch (theme) {
			case LIGHT:
				gc.setFill(Color.web("#FFFFE0"));
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#CCCC00"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			case DARK:
				gc.setFill(Color.web("#333333")); // Midnight Blue Background
				gc.fillRect(0, 0, WIDTH, HEIGHT);

				gc.setStroke(Color.web("#B8860B"));
				gc.setLineWidth(1);

				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLUMNS; j++) {
						gc.strokeRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
					}
				}
				break;
			}
		}
			break;

		}

	}
	private static void drawSnake(GraphicsContext gc) {

		switch (skin) {
		case YELLOW:
			snakeHeadImage = new Image(SNAKEHEAD_IMAGE[0]);
			gc.drawImage(snakeHeadImage, snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);

			gc.setFill(Color.web("FFD300"));
			for (int i = 1; i < snakeBody.size(); i++) {

				gc.fillOval(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
						SQUARE_SIZE - 1, SQUARE_SIZE - 1);
			}
			break;
		case RED:
			snakeHeadImage = new Image(SNAKEHEAD_IMAGE[1]);
			gc.drawImage(snakeHeadImage, snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);

			gc.setFill(Color.web("EA5A47"));
			for (int i = 1; i < snakeBody.size(); i++) {
				gc.fillOval(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
						SQUARE_SIZE - 1, SQUARE_SIZE - 1);
			}
			break;
		case GREEN:
			snakeHeadImage = new Image(SNAKEHEAD_IMAGE[2]);
			gc.drawImage(snakeHeadImage, snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);

			gc.setFill(Color.web("60A917"));
			for (int i = 1; i < snakeBody.size(); i++) {
				gc.fillOval(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
						SQUARE_SIZE - 1, SQUARE_SIZE - 1);
			}
			break;

		case SAD: {
			snakeHeadImage = new Image(SNAKEHEAD_IMAGE[3]);
			gc.drawImage(snakeHeadImage, snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);

			gc.setFill(Color.web("FFD300"));
			for (int i = 1; i < snakeBody.size(); i++) {
				gc.fillOval(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
						SQUARE_SIZE - 1, SQUARE_SIZE - 1);
			}
		}
			break;
		case PINK: {
			snakeHeadImage = new Image(SNAKEHEAD_IMAGE[4]);
			gc.drawImage(snakeHeadImage, snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);

			gc.setFill(Color.web("FB3AA7"));
			for (int i = 1; i < snakeBody.size(); i++) {
				gc.fillOval(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
						SQUARE_SIZE - 1, SQUARE_SIZE - 1);
			}
		}
			break;
		case ASHAMED: {
			snakeHeadImage = new Image(SNAKEHEAD_IMAGE[5]);
			gc.drawImage(snakeHeadImage, snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);

			gc.setFill(Color.web("FCC314"));
			for (int i = 1; i < snakeBody.size(); i++) {
				gc.fillOval(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
						SQUARE_SIZE - 1, SQUARE_SIZE - 1);
			}
		}
			break;
		case LAVENDER: {
			snakeHeadImage = new Image(SNAKEHEAD_IMAGE[6]);
			gc.drawImage(snakeHeadImage, snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);

			gc.setFill(Color.web("8A2BE2"));
			for (int i = 1; i < snakeBody.size(); i++) {
				gc.fillOval(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
						SQUARE_SIZE - 1, SQUARE_SIZE - 1);
			}
		}
			break;
		}
	}

	private void moveRight() {
		snakeHead.x++;
	}

	private void moveLeft() {
		snakeHead.x--;
	}

	private void moveUp() {
		snakeHead.y--;
	}

	private void moveDown() {
		snakeHead.y++;
	}

	public void gameOver() {

		if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH
				|| snakeHead.y * SQUARE_SIZE >= HEIGHT) {
			gameOver = true;


		}
		if (lives == 0) {
			gameOver = true;
		
		}

		// destroy itself
		for (int i = 1; i < snakeBody.size(); i++) {
			if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
				gameOver = true;
		
				break;
			}
		}

	}

	

	private void drawScore(GraphicsContext gc) {
		switch (theme) {
		case DARK:
			gc.setFill(Color.web("FFFFFF"));
			break;
		case LIGHT:
			gc.setFill(Color.web("333333"));
			break;
		}

		gc.setFont(customFont);
		gc.fillText("Score: " + score, 10, 30);

		switch (mode) {
		case NORMAL:
			gc.setFont(customFont);
			gc.fillText("Lives: " + lives, 510, 70);
			break;
		case ENDLESS:
			break;
		}

		gc.setFont(customFont);
		gc.fillText("Total Fruit Eaten: " + totalfruitEaten, 10, 70);
		gc.fillText("Duration: " + formattedTime, 415, 30);
		gc.setFont(customFont);

	}


	public void timeDuration() {
		end = LocalDateTime.now();
		long secondCount = 0;
		Duration d = Duration.between(start, end);
		secondCount = d.getSeconds();
		long minuteCount = secondCount / 60;
		secondCount = secondCount % 60;
		long hourCount = minuteCount / 60;
		minuteCount = minuteCount % 60;
		formattedTime = String.format("%02d:%02d:%02d", hourCount, minuteCount, secondCount);

	}

	private void handleGameOver(ActionEvent event) {

		Main.backgroundMusicPlayer.play();

		guest = MainController.guest;
		if (guest != UserMode.GUEST) {
			createHistory(user.getId());
			String mouseClick = "/audio/mouse_click.mp3";
			mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
			mouseClickSound.play();

			ChangeSceneController changescene = ChangeSceneController.getInstance();
			changescene.changeScene("GameOverUI.fxml", event, "Game Over UI");

			score = 0;
			totalfruitEaten = 0;
			lives = 3;
		} else {
			String mouseClick = "/audio/mouse_click.mp3";
			mouseClickSound = new AudioClip(getClass().getResource(mouseClick).toExternalForm());
			mouseClickSound.play();
			ChangeSceneController changescene = ChangeSceneController.getInstance();
			changescene.changeScene("GameOverGuest.fxml", event, "Game Over Guest UI");

			score = 0;
			totalfruitEaten = 0;
			lives = 3;
		}


	}


	public boolean createHistory(Long userId) {

		boolean created = false;

		Connection connection = null;
		PreparedStatement psInsert = null;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakegame", "root", "1234");

			psInsert = connection.prepareStatement(
					"insert into history(total_fruit_eaten,total_score,start_time,end_time,user_id,mode) values(?,?,?,?,?,?); ");
			psInsert.setLong(1, totalfruitEaten);
			psInsert.setLong(2, score);

			psInsert.setTimestamp(3, Timestamp.valueOf(start));
			psInsert.setTimestamp(4, Timestamp.valueOf(end));
			psInsert.setLong(5, userId);
			psInsert.setString(6, mode.toString());
			created = psInsert.executeUpdate() > 0 ? true : false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return created;

	}

}