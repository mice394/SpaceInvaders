import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OpeningScene extends Scene {

    //private Presenter presenter;
    Scene scene;

    public OpeningScene(Constants.GAME_STATUS gameStatus, int score) {
        super(new BorderPane(), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        Image logoImg = new Image("logo.png");
        ImageView logo = new ImageView(logoImg);

        Label name = new Label(Constants.NAME);
        name.setFont(Constants.SMALL_FONT);

        Label title = new Label(Constants.OPENING_TITLE);
        title.setFont(Constants.TITLE_FONT);

        Label postGameText = new Label((gameStatus == Constants.GAME_STATUS.GAMEOVER) ? Constants.GAMELOST_TEXT : Constants.GAMEWON_TEXT);
        postGameText.setFont(Constants.TITLE_FONT);

        Label scoreText = new Label("Score: " + score);
        scoreText.setFont(Constants.TITLE_FONT);

        Label startNewGame = new Label(Constants.START_NEWGAME_TEXT);
        startNewGame.setFont(Constants.NORMAL_FONT_BOLDED);

        Label instruction1 = new Label(Constants.OPENING_SUBTEXT1);
        instruction1.setFont(Constants.NORMAL_FONT);
        Label instruction2 = new Label(Constants.OPENING_SUBTEXT2);
        instruction2.setFont(Constants.NORMAL_FONT);
        Label instruction3 = new Label(Constants.OPENING_SUBTEXT3);
        instruction3.setFont(Constants.NORMAL_FONT);
        Label instruction4 = new Label(Constants.OPENING_SUBTEXT4);
        instruction4.setFont(Constants.NORMAL_FONT);
        Label instruction5 = new Label(Constants.OPENING_SUBTEXT5);
        instruction5.setFont(Constants.NORMAL_FONT);

        VBox center;
        if (gameStatus == Constants.GAME_STATUS.STARTGAME) {
            center = new VBox(logo, title, instruction1, instruction2, instruction3, instruction4, instruction5, name);
        } else {
            center = new VBox(postGameText, scoreText, startNewGame, instruction1, instruction2, instruction3, instruction4, instruction5);
        }
        center.setAlignment(Pos.CENTER);
        center.setSpacing(10);

        scene = new Scene(center, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }
}
