import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.*;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Constants {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final Font TITLE_FONT = Font.font("Verdana", FontWeight.BOLD, 30);
    public static final Font NORMAL_FONT = Font.font("Verdana", 18);
    public static final Font NORMAL_FONT_BOLDED = Font.font("Verdana", FontWeight.BOLD, 18);
    public static final Font SMALL_FONT = Font.font("Verdana", 12);

    //Opening Texts
    public static final String NAME = "\n\n\n\nEmily Shao - 20772922";
    public static final String OPENING_TITLE = "Instructions";
    public static final String OPENING_SUBTEXT1 = "ENTER - Start Game";
    public static final String OPENING_SUBTEXT2 = "A and D or < and > - Move ship left or right";
    public static final String OPENING_SUBTEXT3 = "SPACE - Fire!";
    public static final String OPENING_SUBTEXT4 = "Q - Quit Game";
    public static final String OPENING_SUBTEXT5 = "1 or 2 or 3 - Start Game at a specific level";

    //Closing Texts
    public static final String GAMEWON_TEXT = "Congratulations, you won the game!";
    public static final String GAMELOST_TEXT = "Sorry, you lost the game";
    public static final String START_NEWGAME_TEXT = "Follow the instructions below to start a new game:";

    //Game Images
    public static final int ENEMY_WIDTH = 50;
    public static final int ENEMY_HEIGHT = 35;

    public static final int MISSLE_WIDTH = 8;
    public static final int MISSLE_HEIGHT = 20;

    public static final int BULLET_WIDTH = 12;
    public static final int BULLET_HEIGHT = 24;

    public static Image spaceship = new Image("spaceship.png");
    public static Image spaceBullet = new Image("spaceBullet.png");

    public static Image greenEnemy = new Image("greenEnemy.png");
    public static Image blueEnemy = new Image("blueEnemy.png");
    public static Image pinkEnemy = new Image("pinkEnemy.png");

    public static Image greenBullet = new Image("greenBullet.png");
    public static Image blueBullet = new Image("blueBullet.png");
    public static Image pinkBullet = new Image("pinkBullet.png");

    //Game Speeds
    public static final double ENEMY_SPEED = 0.5;
    public static final double SHIP_SPEED = 4.0;
    public static final double ENEMY_BULLET_SPEED = 4.0;
    public static final double SHIP_MISSLE_SPEED = 5.0;
    public static final double MAX_ENEMY_BULLETS = 3.0;

    public static final int ENEMY_POINTS = 5;

    enum GAME_STATUS {GAMEWON, GAMEOVER, STARTGAME};
}
