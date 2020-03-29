package driver;

import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import levels.InvadersDefaultLevel;
import management.AnimationRunner;
import management.Counter;
import management.EndScreen;
import management.HighScoresAnimation;
import management.KeyPressStoppableAnimation;
import management.LevelInformation;

import java.io.File;
import java.io.IOException;

/**
 * Represents a game flow.
 */
public class GameFlow {
    public static final int LIVES = 7;
    public static final int START_SCORE = 0;
    private Counter remainingLives;
    private Counter score;
    private AnimationRunner animRunner;
    private KeyboardSensor keySensor;
    private HighScoresTable highScoresTable;
    /**
     * Create a new game flow.
     *
     * @param ar    Animation runner.
     * @param ks    Keyboard sensor.
     * @param lives Number of lives.
     * @param table High scores table.
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, int lives, HighScoresTable table) {
        this.remainingLives = new Counter();
        this.remainingLives.increase(lives);
        this.score = new Counter();
        this.animRunner = ar;
        this.keySensor = ks;
        this.highScoresTable = table;
        resetFlow();
    }
    /**
     * Runs the argument levels.
     *
     * @throws Exception Exception.
     */
    public void runLevels() throws Exception {
        boolean playerWon = false;

        for (int i = 1; true; i++) {
            LevelInformation levelInfo = new InvadersDefaultLevel(i, i * 30);

            GameLevel level = new GameLevel(levelInfo, this.animRunner, this.remainingLives, this.score);

            level.initialize();

            while ((level.getRemainingLives() > 0) && (level.getRemainingBlocks() > 0)) {
                level.playOneTurn();

                if (level.getRemainingBlocks() > 0) {
                    remainingLives.decrease(1);
                }
            }

            if (this.remainingLives.getValue() == 0) {
                break;
            }
        }
        if (this.remainingLives.getValue() > 0) {
            playerWon = true;
        }
        EndScreen endScreen = new EndScreen(playerWon, this.score.getValue());
        KeyPressStoppableAnimation keyPressStoppableAnimation = new KeyPressStoppableAnimation(this.keySensor,
                                                                                               KeyboardSensor.SPACE_KEY,
                                                                                               endScreen);
        this.animRunner.run(keyPressStoppableAnimation);
        int rank = this.highScoresTable.getRank(this.score.getValue());
        if (rank <= this.highScoresTable.size()) {
            this.addHighScore(this.score.getValue());
        }
        //savetable
        HighScoresAnimation highScoresAnimation = new HighScoresAnimation(this.highScoresTable);
        keyPressStoppableAnimation = new KeyPressStoppableAnimation(this.keySensor, KeyboardSensor.SPACE_KEY,
                                                                    highScoresAnimation);
        this.animRunner.run(keyPressStoppableAnimation);
        this.resetFlow();
    }
    /**
     * Returns the high scores table.
     *
     * @return The high scores table.
     */
    public HighScoresTable getHighScoresTable() {
        File highScoresTableFile = new File(HighScoresTable.FILE_NAME);
        HighScoresTable hScoresTable = new HighScoresTable();
        if (highScoresTableFile.exists() && !highScoresTableFile.isDirectory()) {
            hScoresTable = HighScoresTable.loadFromFile(highScoresTableFile);
        } else {
            try {
                hScoresTable.save(highScoresTableFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hScoresTable;
    }
    /**
     * Add high score.
     *
     * @param scr The new score.
     */
    public void addHighScore(int scr) {
        DialogManager manager = this.animRunner.getGui().getDialogManager();
        String playerName = manager.showQuestionDialog("High Scores!!!", "What is your name?", "Your name...");
        ScoreInfo scoreInfo = new ScoreInfo(playerName, scr);
        this.highScoresTable.add(scoreInfo);
        try {
            this.highScoresTable.save(new File(HighScoresTable.FILE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Reset the game flow.
     */
    public void resetFlow() {
        this.remainingLives.setValue(3);
        this.score.setValue(START_SCORE);
    }
}
