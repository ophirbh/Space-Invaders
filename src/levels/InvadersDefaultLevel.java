package levels;

import geometryprimitives.Point;
import geometryprimitives.Rectangle;
import management.LevelInformation;
import spaceinvaders.AliensFlock;
import sprites.Background;
import sprites.Block;
import sprites.Fill;
import sprites.Sprite;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a default space invaders level.
 */
public class InvadersDefaultLevel implements LevelInformation {
    private String levelName;
    private Background background;
    private double flockSpeed;
    private AliensFlock aliensFlock;
    /**
     * Creates a new space invaders level.
     *
     * @param levName        the number of the current battle.
     * @param initFlockSpeed the starting speed of the flock.
     */
    public InvadersDefaultLevel(int levName, int initFlockSpeed) {
        this.background = new Background(new Fill(Color.BLACK));
        this.flockSpeed = initFlockSpeed;
        this.levelName = "Battle no." + levName;
        this.aliensFlock = new AliensFlock(initFlockSpeed);
    }
    @Override
    public int paddleSpeed() {
        return 600;
    }
    @Override
    public int paddleWidth() {
        return 100;
    }
    @Override
    public String levelName() {
        return levelName;
    }
    @Override
    public Sprite getBackground() {
        return background;
    }
    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();

        blocks.addAll(createShields(80));
        blocks.addAll(createShields(315));
        blocks.addAll(createShields(550));

        return blocks;
    }
    /**
     * Creates the shield of the game.
     *
     * @param start the X of the starting position.
     * @return list of block which are shields.
     */
    private List<Block> createShields(int start) {
        List<Block> blocks = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            for (int j = 1; j <= 3; j++) {
                Rectangle rectangle = new Rectangle(new Point(start + 6 * i, 500 + j * 6), 6, 6);
                Fill fill = new Fill(Color.CYAN);
                blocks.add(new Block(rectangle, fill, null, 1));
            }
        }

        return blocks;
    }
    @Override
    public AliensFlock aliensFlock() {
        return aliensFlock;
    }
    @Override
    public int numberOfAliensToRemove() {
        return 50;
    }
    @Override
    public double flockSpeed() {
        return flockSpeed;
    }
}
