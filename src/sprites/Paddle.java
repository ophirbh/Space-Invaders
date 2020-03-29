package sprites;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import driver.GameLevel;
import geometryprimitives.Point;
import geometryprimitives.Rectangle;
import spaceinvaders.PlayerShot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a paddle.
 */
public class Paddle implements Sprite, Collidable {
    public static final double PADDLE_SEGMENTS = 5;
    private biuoop.KeyboardSensor keyboard;
    private Rectangle geoRep;
    private Color color;
    private int speed;
    private GameLevel level;
    private long lastShot;
    private boolean gotShot;
    private List<PlayerShot> playerShots;
    /**
     * Create a new paddle.
     *
     * @param lowestCentral The lowest middle point of the paddle.
     * @param width         The width of the paddle.
     * @param height        The height of the paddle.
     * @param c             The color of the paddle.
     * @param sensor        The keyboard sensor.
     * @param speed         The paddle's speed.
     * @param level         The level of the paddle.
     */
    public Paddle(Point lowestCentral, int width, int height, Color c, KeyboardSensor sensor, int speed,
                  GameLevel level) {
        this.geoRep = new Rectangle(new Point(lowestCentral.getX() - (width / 2), lowestCentral.getY() - height), width,
                                    height);
        this.color = c;
        this.keyboard = sensor;
        this.speed = speed;
        this.level = level;
        this.lastShot = 0;
        this.gotShot = false;
        this.playerShots = new ArrayList<>();
    }
    /**
     * Move paddle left.
     *
     * @param dt The amount of seconds passed since the last call.
     */
    public void moveLeft(double dt) {
        int dx = (int) (this.speed * dt);
        if (this.geoRep.getUpperLeft().getX() <= GameLevel.VERT_BLOCK_BOUND_WIDTH) {
            this.geoRep.setXUpperLeft(GameLevel.VERT_BLOCK_BOUND_WIDTH);
        } else {
            this.geoRep.setXUpperLeft(this.geoRep.getUpperLeft().getX() - dx);
        }
    }
    /**
     * Move paddle right.
     *
     * @param dt The amount of seconds passed since the last call.
     */
    public void moveRight(double dt) {
        int dx = (int) (this.speed * dt);
        if (this.geoRep.getUpperLeft().getX() >= 800 - GameLevel.VERT_BLOCK_BOUND_WIDTH - this.geoRep.getWidth()) {
            this.geoRep.setXUpperLeft(800 - GameLevel.VERT_BLOCK_BOUND_WIDTH - this.geoRep.getWidth());
        } else {
            this.geoRep.setXUpperLeft(this.geoRep.getUpperLeft().getX() + dx);
        }
    }
    /**
     * shoots a shot.
     */
    public void shoot() {
        long thisShot = System.currentTimeMillis();
        if (thisShot - lastShot >= 350) {
            double x = geoRep.getUpperLeft().getX() + geoRep.getWidth() / 2;

            PlayerShot shot = new PlayerShot((int) x, (int) geoRep.getUpperLeft().getY() - 1, level.getEnvironment());
            shot.addToGame(level);
            playerShots.add(shot);
            lastShot = thisShot;
        }
    }
    /**
     * Removes all the shots this paddle has shot.
     */
    public void removeShots() {
        for (PlayerShot shot : playerShots) {
            shot.removeFromGame(level);
        }

        playerShots = new ArrayList<>();
    }
    /**
     * Notify the sprite that time has passed.
     *
     * @param dt The amount of seconds passed since the last call.
     */
    @Override
    public void timePassed(double dt) {
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            this.moveLeft(dt);
        }
        if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            this.moveRight(dt);
        }
        if (keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
            this.shoot();
        }
    }
    /**
     * Draw paddle on the given draw surface.
     *
     * @param d The given draw surface.
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillRectangle((int) this.geoRep.getUpperLeft().getX(), (int) this.geoRep.getUpperLeft().getY(),
                        (int) this.geoRep.getWidth(), (int) this.geoRep.getHeight());
        d.setColor(Color.BLACK);
        d.drawRectangle((int) this.geoRep.getUpperLeft().getX(), (int) this.geoRep.getUpperLeft().getY(),
                        (int) this.geoRep.getWidth(), (int) this.geoRep.getHeight());
    }

    // sprites.Collidable
    /**
     * Return the "collision shape" of the object.
     *
     * @return The "collision shape" of the object.
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return new Rectangle(geoRep.getUpperLeft(), geoRep.getWidth(), 0);
    }
    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity. The return is the new velocity expected after the
     * hit (based on the force the object inflicted on us).
     *
     * @param collisionPoint  The collision point.
     * @param currentVelocity The current velocity.
     * @param hitter          The hitter ball.
     * @return The new velocity expected after the hit.
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.gotShot = true;
        return currentVelocity;
    }
    /**
     * Add this paddle to the game.
     *
     * @param g The game.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }
    /**
     * Get geometric representation.
     *
     * @return Geometric representation.
     */
    public Rectangle getGeoRep() {
        return new Rectangle(this.geoRep);
    }
    /**
     * Set geometric representation.
     *
     * @param rectangle The rectangle used for geo rep.
     */
    public void setGeoRep(Rectangle rectangle) {
        this.geoRep = rectangle;
    }
    /**
     * Get the length of a paddle segment.
     *
     * @return The length of a paddle segment.
     */
    public double getPaddleSegmentLength() {
        return this.geoRep.getWidth() / this.PADDLE_SEGMENTS;
    }
    /**
     * Remove this paddle from the game.
     *
     * @param g The paddle to remove.
     */
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
        g.removeCollidable(this);
    }
    /**
     * Set the paddle left X coordinate.
     *
     * @param xCoor The new X coordinate.
     */
    public void setLeftXCoor(double xCoor) {
        double oldYcoor = this.getGeoRep().getUpperLeft().getY();
        double oldHeight = this.getGeoRep().getHeight();
        double oldWidth = this.getGeoRep().getWidth();
        Rectangle newGeoRep = new Rectangle(new Point(xCoor, oldYcoor), oldWidth, oldHeight);
        this.setGeoRep(newGeoRep);
    }
    /**
     * Checks if the paddle got shot.
     *
     * @return true if the paddle got shot and false if it is not.
     */
    public boolean isGotShot() {
        return this.gotShot;
    }
    /**
     * Change the got shot status to false.
     */
    public void resetGotShot() {
        this.gotShot = false;
    }
}
