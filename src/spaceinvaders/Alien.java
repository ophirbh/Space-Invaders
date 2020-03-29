package spaceinvaders;

import driver.GameLevel;
import geometryprimitives.Point;
import geometryprimitives.Rectangle;
import management.BallRemover;
import sprites.Ball;
import sprites.Block;
import sprites.Fill;
import sprites.Velocity;

import java.awt.Color;

/**
 * Represents an alien.
 */
public class Alien extends Block {
    private AliensFlock aliensFlock;
    private GameLevel level;
    /**
     * Constructor.
     *
     * @param xCoor       The alien's X coordinate.
     * @param yCoor       The alien's Y coordinate.
     * @param aliensFlock The flock this alien belongs to.
     */
    public Alien(int xCoor, int yCoor, AliensFlock aliensFlock) {
        super(new Rectangle(new Point(xCoor, yCoor), 40, 30), new Fill("enemy.png"), Color.BLACK, 1);
        this.aliensFlock = aliensFlock;
    }

    /**
     * Moves the alien down.
     *
     * @param yChange Y coordinates change.
     */
    public void moveDown(double yChange) {
        Point upperLeft = this.getCollisionRectangle().getUpperLeft();
        super.changeUpperLeft(new Point(upperLeft.getX(), upperLeft.getY() + yChange));
    }
    /**
     * Moves the alien left or right.
     *
     * @param xChange X coordinates change.
     */
    public void moveRight(double xChange) {
        Point upperLeft = this.getCollisionRectangle().getUpperLeft();
        super.changeUpperLeft(new Point(upperLeft.getX() + xChange, upperLeft.getY()));
    }
    @Override
    public boolean removeFromGame(GameLevel gameLevel) {
        super.removeFromGame(gameLevel);
        aliensFlock.removeAlien(this);
        return false;
    }
    @Override
    public void addToGame(GameLevel gameLevel) {
        super.addToGame(gameLevel);
        this.level = gameLevel;
    }
    /**
     * shoots a shot.
     *
     * @return the shot of the alien.
     */
    public AlienShot shoot() {
        Point upperLeft = this.getUpperLeft();
        AlienShot alienShot = new AlienShot((int) (upperLeft.getX() + getWidth() / 2),
                                            (int) (upperLeft.getY() + getHeight() + 1), level.getEnvironment());
        alienShot.addToGame(level);

        return alienShot;
    }
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        if (hitter.getVelocity().getDY() < 0) {
            return super.hit(hitter, collisionPoint, currentVelocity);
        }
        new BallRemover(level, null).hitEvent(this, hitter);
        return currentVelocity;
    }
}
