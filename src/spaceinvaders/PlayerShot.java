package spaceinvaders;

import management.GameEnvironment;
import sprites.Ball;

import java.awt.Color;

/**
 * Represents an alien shot.
 */
public class PlayerShot extends Ball {
    /**
     * Creates new player shot.
     *
     * @param x    the X position of the shot.
     * @param y    the Y position of the shot.
     * @param game the game environment of the shot.
     */
    public PlayerShot(int x, int y, GameEnvironment game) {
        super(x, y, 5, Color.white, game);
        super.setVelocity(0, -500);
    }
}
