package management;

import spaceinvaders.AliensFlock;
import sprites.Block;
import sprites.Sprite;

import java.util.List;

/**
 * Specifies the information required to fully describe a level.
 */
public interface LevelInformation {

    /**
     * Returns the paddle speed.
     *
     * @return The paddle speed.
     */
    int paddleSpeed();

    /**
     * Returns the paddle width.
     *
     * @return The paddle width.
     */
    int paddleWidth();

    /**
     * The level name will be displayed at the top of the screen.
     *
     * @return Level name.
     */
    String levelName();

    /**
     * Returns a sprite with the background of the level.
     *
     * @return A sprite with the background of the level.
     */
    Sprite getBackground();

    /**
     * The Blocks that make up this level, each block contains
     * its size, color and location.
     *
     * @return The Blocks that make up this level
     */
    List<Block> blocks();

    /**
     * The aliens flock of the level.
     *
     * @return the aliens flock.
     */
    AliensFlock aliensFlock();

    /**
     * Number of blocks that should be removed
     * before the level is considered to be "cleared".
     * This number should be <= blocks.size();
     *
     * @return The number of blocks that should be removed before the
     * level is considered to be "cleared".
     */
    int numberOfAliensToRemove();

    /**
     * Returns the Aliens flock speed.
     *
     * @return The Aliens flock speed.
     */
    double flockSpeed();
}
