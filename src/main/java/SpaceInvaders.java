import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class SpaceInvaders extends Application {

    public static Stage tmpStage;
    Scene opening, game;
    enum SCENES {OPENING, LEVEL1, LEVEL2, LEVEL3};

    @Override
    public void start(Stage stage) {
        tmpStage = stage;
        OpeningScene openingScene = new OpeningScene(Constants.GAME_STATUS.STARTGAME, 0);
        opening = openingScene.scene;

        //KeyPressing
        opening.setOnKeyPressed(event -> {
            if ((event.getCode() == KeyCode.DIGIT1) || (event.getCode() == KeyCode.ENTER)) {
                setScene(stage, SCENES.LEVEL1);
            }
            if (event.getCode() == KeyCode.DIGIT2) {
                setScene(stage, SCENES.LEVEL2);
            }
            if (event.getCode() == KeyCode.DIGIT3) {
                setScene(stage, SCENES.LEVEL3);
            }
            if (event.getCode() == KeyCode.Q) {
                System.exit(0);
            }
        });

        stage.setResizable(false);
        stage.setTitle("Space Invaders");
        stage.setScene(opening);
        stage.show();
    }

    public void setScene(Stage stage, SCENES scene) {

        switch(scene) {
            case OPENING:
                stage.setScene(opening);
                break;
            case LEVEL1:
                GameScene gameScene1 = new GameScene(0, 3, 1);
                game = gameScene1.scene;
                stage.setScene(game);
                break;
            case LEVEL2:
                GameScene gameScene2 = new GameScene(0, 3, 2);
                game = gameScene2.scene;
                stage.setScene(game);
                break;
            case LEVEL3:
                GameScene gameScene3 = new GameScene(0, 3, 3);
                game = gameScene3.scene;
                stage.setScene(game);
                break;
        }
    }

}
