package spaceinvaders;

import biuoop.DrawSurface;
import driver.GameLevel;
import sprites.Block;
import sprites.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a flock of aliens.
 */
public class AliensFlock implements Sprite {
    private static final int SPACESHIP_WIDTH = 40;
    private static final int SPACESHIP_HEIGHT = 30;
    private static final int SPACESHIP_SPACE = 10;
    private static final int FLOCK_ROWS = 5;
    private static final int FLOCK_COLS = 10;
    private List<List<Alien>> aliens;
    private double speed, startSpeed;
    private long lastShot;
    private List<AlienShot> alienShots;
    /**
     * aliens flock constructor.
     *
     * @param speed the start speed of the flock
     */
    public AliensFlock(double speed) {
        this.speed = speed;
        this.startSpeed = speed;
        this.lastShot = 0;
        this.alienShots = new ArrayList<>();
        initAliens();
    }
    /**
     * resets all the aliens.
     */
    public void initAliens() {
        this.aliens = new ArrayList<>();
        for (int i = 1; i <= FLOCK_COLS; i++) {
            List<Alien> aliensCol = new ArrayList<>();
            for (int j = 1; j <= FLOCK_ROWS; j++) {
                aliensCol.add(
                        new Alien((SPACESHIP_SPACE + SPACESHIP_WIDTH) * i, (SPACESHIP_SPACE + SPACESHIP_HEIGHT) * j,
                                  this));
            }
            aliens.add(aliensCol);
        }
    }
    /**
     * @return all the aliens in the flock.
     */
    public List<Block> getAliens() {
        List<Block> tempAliens = new ArrayList<>();
        for (List<Alien> aliensCol : aliens) {
            tempAliens.addAll(aliensCol);
        }

        return tempAliens;
    }
    @Override
    public void drawOn(DrawSurface d) {

    }
    @Override
    public void timePassed(double dt) {
        // moves the aliens
        double xChangeRight = 800 - aliens.get(aliens.size() - 1).get(0).getCollisionRectangle().getUpperRight().getX();
        double xChangeLeft = aliens.get(0).get(0).getCollisionRectangle().getUpperLeft().getX();
        if (xChangeRight > 0 && speed > 0) {
            moveAliens(Math.min(speed * dt, xChangeRight), 0);
        } else if (xChangeLeft > 0 && speed < 0) {
            moveAliens(Math.max(speed * dt, -xChangeLeft), 0);
        } else {
            moveAliens(0, 40);
            speed *= -1.1;
        }

        // shoots a shot
        long thisShot = System.currentTimeMillis();
        if (thisShot - lastShot >= 500) {
            Random random = new Random();
            int col = random.nextInt(aliens.size());
            List<Alien> aliensCol = aliens.get(col);
            alienShots.add(aliensCol.get(aliensCol.size() - 1).shoot());
            this.lastShot = thisShot;
        }
    }
    /**
     * move all the alines.
     *
     * @param xChange the change in X.
     * @param yChange the change in Y.
     */
    private void moveAliens(double xChange, double yChange) {
        for (List<Alien> aliensCol : aliens) {
            for (Alien alien : aliensCol) {
                alien.moveDown(yChange);
                alien.moveRight(xChange);
            }
        }
    }
    /**
     * Removes an alien from the flock.
     *
     * @param alien the alien to remove
     */
    public void removeAlien(Alien alien) {
        for (int i = 0; i < aliens.size(); i++) {
            for (int j = 0; j < aliens.get(i).size(); j++) {
                if (aliens.get(i).contains(alien)) {
                    aliens.get(i).remove(alien);
                    if (aliens.get(i).isEmpty()) {
                        aliens.remove(i);
                    }
                    return;
                }
            }
        }
    }
    /**
     * checks if the flock is at the bottom.
     *
     * @return true if the flock is at the bottom and false it is not.
     */
    public boolean isBottom() {
        double bottom = 0;
        for (List<Alien> aliensCol : aliens) {
            bottom = Math.max(aliensCol.get(aliensCol.size() - 1).getUpperLeft().getY() + SPACESHIP_HEIGHT, bottom);
        }
        return bottom >= 500;
    }
    /**
     * resets the position of the aliens.
     */
    public void resetFlock() {
        speed = startSpeed;

        double mostHigh = 800, mostLeft = 600;
        for (List<Alien> aliensCol : aliens) {
            for (Alien alien : aliensCol) {
                mostHigh = Math.min(mostHigh, alien.getUpperLeft().getY());
                mostLeft = Math.min(mostLeft, alien.getUpperLeft().getX());
            }
        }

        moveAliens(-mostLeft + SPACESHIP_SPACE + SPACESHIP_WIDTH, -mostHigh + SPACESHIP_SPACE + SPACESHIP_HEIGHT);
    }
    /**
     * Removes all the shots this flock has shot.
     *
     * @param level the level the shots will be removed from.
     */
    public void removeShots(GameLevel level) {
        for (AlienShot alienShot : alienShots) {
            alienShot.removeFromGame(level);
        }

        alienShots = new ArrayList<>();
    }
}
