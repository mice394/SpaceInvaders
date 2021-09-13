import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.util.*;

public class GameScene extends Scene {

    Scene scene;
    Group root;
    Random rand = new Random();
    int seconds = 0;
    int misslesFired = 0;

    //Game Data
    public int score;
    public int lives;
    public int level;

    //Game Text
    HBox top;
    public Label SCORE_TEXT;
    public Label LIVES_TEXT;
    public Label LEVEL_TEXT;

    //Game Sounds
    String invader = getClass().getClassLoader().getResource("invader.wav").toString();
    AudioClip invaderSound = new AudioClip(invader);
    String invaderExplosion = getClass().getClassLoader().getResource("invaderkilled.wav").toString();
    AudioClip invaderExplosionSound = new AudioClip(invaderExplosion);
    String shipExplosion = getClass().getClassLoader().getResource("shipExplosion.wav").toString();
    AudioClip shipExplosionSound = new AudioClip(shipExplosion);
    String shootMissle = getClass().getClassLoader().getResource("shoot.wav").toString();
    AudioClip shootMissleSound = new AudioClip(shootMissle);

    enum DIRECTION {LEFT, RIGHT, NONE};

    //Enemy
    GridPane enemiesGridPane;
    Vector<Rectangle> allEnemies = new Vector<>();
    Vector<Shape> enemyBullets = new Vector<>();
    DIRECTION enemyDirection = DIRECTION.RIGHT;
    double dy;

    //Spaceship
    Rectangle spaceship;
    DIRECTION shipDirection = DIRECTION.NONE;
    Boolean shipFire = false;
    Vector<Shape> shipMissles = new Vector<>();

    AnimationTimer timer;

    public GameScene(int sco, int liv, int lev) {
        super(new BorderPane(), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        score = sco;
        lives = liv;
        level = lev;

        setUpScene();
        setUpKeyPressed();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                ++seconds;
                if (seconds % 60 == 0) {
                    misslesFired = 0;
                }
                if (seconds % 20 == 0) invaderSound.play();
                enemyAnimation();

                if ((shipDirection == DIRECTION.LEFT) && (spaceship.getX() > 0)) {
                    spaceship.setX(spaceship.getX() - Constants.SHIP_SPEED);
                }
                if ((shipDirection == DIRECTION.RIGHT) && (spaceship.getX() + spaceship.getWidth() < Constants.SCREEN_WIDTH)) {
                    spaceship.setX(spaceship.getX() + Constants.SHIP_SPEED);
                }

                if ((shipFire) && (misslesFired <= 2)) {
                    Rectangle missle = new Rectangle(Constants.MISSLE_WIDTH, Constants.MISSLE_HEIGHT);
                    ImagePattern image = new ImagePattern(Constants.spaceBullet);
                    missle.setFill(image);
                    missle.setX(spaceship.getX() + spaceship.getWidth()/2);
                    missle.setY(spaceship.getY() + 1);
                    shipMissles.add(missle);
                    root.getChildren().add(missle);
                    shipFire = false;
                    ++misslesFired;
                    shootMissleSound.play();
                }
                shipMissleAnimation();

                int enemyRate = (level == 1) ? 60 : (level == 2) ? 50 : 40;
                if ((enemyBullets.size() <= level*Constants.MAX_ENEMY_BULLETS) && (seconds % enemyRate == 0)) {
                    Rectangle bullet = new Rectangle(Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT);
                    ImagePattern image;
                    int idx = rand.nextInt(allEnemies.size());
                    Rectangle shooter = allEnemies.elementAt(idx);
                    int row = enemiesGridPane.getRowIndex(shooter);
                    int col = enemiesGridPane.getColumnIndex(shooter) + 1;
                    if (row == 0) {
                        image = new ImagePattern(Constants.greenBullet);
                    } else if ((row == 1) || (row == 2)) {
                        image = new ImagePattern(Constants.blueBullet);
                    } else {
                        image = new ImagePattern(Constants.pinkBullet);
                    }
                    bullet.setFill(image);

                    int xOffset = (col - 1)*Constants.ENEMY_WIDTH + ((col - 1) * 10) + Constants.ENEMY_WIDTH/2;
                    int yOffset = row*Constants.ENEMY_HEIGHT + row*10;
                    bullet.setX(enemiesGridPane.getLayoutX() + xOffset);
                    bullet.setY(enemiesGridPane.getLayoutY() + yOffset);
                    enemyBullets.add(bullet);
                    root.getChildren().add(bullet);
                }
                enemyBulletAnimation();

                //updating labels
                SCORE_TEXT.setText("Score: " + score);
                LIVES_TEXT.setText("Lives: " + lives);
            }
        };
        timer.start();
    }

    private boolean didHitShipDetection(Shape bullet) {
        Shape intersect = Shape.intersect(spaceship, bullet);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            return true;
        }
        return false;
    }

    private boolean didHitEnemyDetection(Shape missle) {
        Shape deadEnemy = missle;
            for (Shape enemy : allEnemies) {
                Shape intersect = Shape.intersect(missle, enemy);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    enemy.setFill(Color.BLACK);
                    deadEnemy = enemy;
                    break;
                }
            }
            if ((deadEnemy != missle) && (allEnemies.contains(deadEnemy))) {
                score += Constants.ENEMY_POINTS;
                invaderExplosionSound.play();
                allEnemies.remove(deadEnemy);
                return true;
            }
        return false;
    }

    private void enemyBulletAnimation(){
        Vector<Shape> deadBullets = new Vector<>();
        boolean didHitShip = false;
        for(Shape bullet : enemyBullets) {
            bullet.setLayoutY(bullet.getLayoutY() + Constants.ENEMY_BULLET_SPEED);
            didHitShip = didHitShipDetection(bullet);
            if ((bullet.getLayoutY() >= spaceship.getY()) || didHitShip) {
                deadBullets.add(bullet);
                root.getChildren().remove(bullet);
                break;
            }
        }
        for(Shape bullet : deadBullets) {
            enemyBullets.remove(bullet);
        }
        if (didHitShip) shipWasHit();
    }

    private void shipWasHit() {
        --lives;
        shipExplosionSound.play();
        if (lives > 0) { //spawn a new ship somewhere random
            int newShipPos = rand.nextInt(Constants.SCREEN_WIDTH - 20);
            newShipPos += 10;
            spaceship.setX(newShipPos);
        } else {
            level = -1; //GAME OVER!
            endOfGame();
        }
    }

    private void shipMissleAnimation() {
        Vector<Shape> deadMissles = new Vector<>();
        for(Shape missle : shipMissles) {
            missle.setLayoutY(missle.getLayoutY() - Constants.SHIP_MISSLE_SPEED);
            if ((missle.getLayoutY() * -1 > Constants.SCREEN_HEIGHT - 65) || didHitEnemyDetection(missle)) {
                deadMissles.add(missle);
                root.getChildren().remove(missle);
            }
        }
        for(Shape missle : deadMissles) {
            shipMissles.remove(missle);
        }
        if (allEnemies.isEmpty()) {
            endOfGame();
        }
    }

    private void enemyAnimation() {
        for(Shape enemy : allEnemies) {
            if (didHitShipDetection(enemy)) shipWasHit();
        }

        Shape lowestEnemy = allEnemies.lastElement();
        int row = enemiesGridPane.getRowIndex(lowestEnemy) + 1;
        int enemyY = row*Constants.ENEMY_HEIGHT + row*10;
        if (enemyY + enemiesGridPane.getLayoutY() >= Constants.SCREEN_HEIGHT) { // if aliens reach the bottom of the screen
            level = -1; //game over
            shipExplosionSound.play();
            endOfGame();
        }

        Rectangle rightMostEnemy = allEnemies.elementAt(0);
        Rectangle leftMostEnemy = allEnemies.elementAt(0);
        for (Rectangle enemy : allEnemies) {
            if (enemiesGridPane.getColumnIndex(enemy) > enemiesGridPane.getColumnIndex(rightMostEnemy)) {
                rightMostEnemy = enemy;
            }
            if (enemiesGridPane.getColumnIndex(enemy) < enemiesGridPane.getColumnIndex(leftMostEnemy)) {
                leftMostEnemy = enemy;
            }
        }
        int RightXOffset = enemiesGridPane.getColumnIndex(rightMostEnemy)*Constants.ENEMY_WIDTH + (enemiesGridPane.getColumnIndex(rightMostEnemy) * 10) + Constants.ENEMY_WIDTH;
        int LeftXOffset = enemiesGridPane.getColumnIndex(leftMostEnemy)*Constants.ENEMY_WIDTH + (enemiesGridPane.getColumnIndex(leftMostEnemy) * 10);
        if (enemyDirection == DIRECTION.RIGHT) {
            if (enemiesGridPane.getLayoutX() + RightXOffset >= Constants.SCREEN_WIDTH) { //right
                enemyDirection = DIRECTION.LEFT;
                enemiesGridPane.setLayoutY(enemiesGridPane.getLayoutY() + enemiesGridPane.getHeight()/5);
            }
        }
        else {
            if (enemiesGridPane.getLayoutX() + LeftXOffset <= 0) { //left
                enemyDirection = DIRECTION.RIGHT;
                enemiesGridPane.setLayoutY(enemiesGridPane.getLayoutY() + enemiesGridPane.getHeight()/5);
            }
        }
        dy = ((level * Constants.ENEMY_SPEED) + ((50 - allEnemies.size())*0.03)) * ((enemyDirection == DIRECTION.RIGHT) ? 1 : -1);
        enemiesGridPane.setLayoutX(enemiesGridPane.getLayoutX() + dy);
    }

    private void setUpKeyPressed() {
        scene.setOnKeyPressed(event -> {
            if ((event.getCode() == KeyCode.LEFT) || (event.getCode() == KeyCode.A)) shipDirection = DIRECTION.LEFT;
            if ((event.getCode() == KeyCode.RIGHT) || (event.getCode() == KeyCode.D)) shipDirection = DIRECTION.RIGHT;
            if (event.getCode() == KeyCode.SPACE) shipFire = true;
            if (event.getCode() == KeyCode.Q) System.exit(0);
        });
        scene.setOnKeyReleased(event -> {
            shipDirection = DIRECTION.NONE;
        });
    }

    private void setLabels() {
        SCORE_TEXT = new Label("Score: " + score);
        SCORE_TEXT.setFont(Constants.NORMAL_FONT);
        SCORE_TEXT.setTextFill(Color.WHITE);

        LIVES_TEXT = new Label("Lives: " + lives);
        LIVES_TEXT.setFont(Constants.NORMAL_FONT);
        LIVES_TEXT.setTextFill(Color.WHITE);

        LEVEL_TEXT = new Label("Level: " + level);
        LEVEL_TEXT.setFont(Constants.NORMAL_FONT);
        LEVEL_TEXT.setTextFill(Color.WHITE);
    }

    private void setUpScene() {
        setLabels();
        top = new HBox(SCORE_TEXT, LIVES_TEXT, LEVEL_TEXT);
        top.setAlignment(Pos.TOP_CENTER);
        top.setSpacing(250);

        enemiesGridPane = new GridPane();

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 10; ++j) {
                if (i == 0) {
                    Rectangle greenEnemy = new Rectangle(Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT);
                    ImagePattern image = new ImagePattern(Constants.greenEnemy);
                    greenEnemy.setFill(image);
                    enemiesGridPane.add(greenEnemy, j, i);
                    allEnemies.add(greenEnemy);
                }
                else if ((i == 1) || (i == 2)) {
                    Rectangle blueEnemy = new Rectangle(Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT);
                    ImagePattern image = new ImagePattern(Constants.blueEnemy);
                    blueEnemy.setFill(image);
                    enemiesGridPane.add(blueEnemy,j, i);
                    allEnemies.add(blueEnemy);
                }
                else {
                    Rectangle pinkEnemy = new Rectangle(Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT);
                    ImagePattern image = new ImagePattern(Constants.pinkEnemy);
                    pinkEnemy.setFill(image);
                    enemiesGridPane.add(pinkEnemy,j, i);
                    allEnemies.add(pinkEnemy);
                }
            }
        }
        enemiesGridPane.setVgap(10);
        enemiesGridPane.setHgap(10);
        enemiesGridPane.setLayoutY(50);

        spaceship = new Rectangle(Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT);
        ImagePattern image = new ImagePattern(Constants.spaceship);
        spaceship.setFill(image);
        spaceship.setY(Constants.SCREEN_HEIGHT - Constants.ENEMY_HEIGHT - 10);
        spaceship.setX(Constants.SCREEN_WIDTH/2);

        root = new Group(top, enemiesGridPane, spaceship);

        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Color.BLACK);
    }

    void endOfGame() {
        timer.stop();

        if (level == -1) {
            setUpEnd(Constants.GAME_STATUS.GAMEOVER);
            return;
        }
        if ((level == 1) || (level == 2)) {
            GameScene tmpScene = new GameScene(score, lives, level + 1);
            SpaceInvaders.tmpStage.setScene(tmpScene.scene);
            return;
        }
        else {
            setUpEnd(Constants.GAME_STATUS.GAMEWON);
            return;
        }
    }

    public void setUpEnd(Constants.GAME_STATUS gameStatus){
        root.getChildren().clear();
        scene.setFill(Color.WHITE);

        OpeningScene restart = new OpeningScene(gameStatus, score);
        SpaceInvaders.tmpStage.setScene(restart.scene);

        restart.scene.setOnKeyPressed(event -> {
            if ((event.getCode() == KeyCode.DIGIT1) || (event.getCode() == KeyCode.ENTER)) {
                GameScene tmpScene = new GameScene(0, 3, 1);
                SpaceInvaders.tmpStage.setScene(tmpScene.scene);
            }
            if (event.getCode() == KeyCode.DIGIT2) {
                GameScene tmpScene = new GameScene(0, 3, 2);
                SpaceInvaders.tmpStage.setScene(tmpScene.scene);
            }
            if (event.getCode() == KeyCode.DIGIT3) {
                GameScene tmpScene = new GameScene(0, 3, 3);
                SpaceInvaders.tmpStage.setScene(tmpScene.scene);
            }
            if (event.getCode() == KeyCode.Q) {
                System.exit(0);
            }
        });
    }
}
