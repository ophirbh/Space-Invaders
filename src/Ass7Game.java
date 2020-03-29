import biuoop.GUI;
import driver.GameFlow;
import driver.HighScoresTable;
import management.AnimationRunner;
import management.LevelInformation;
import management.MenuAnimation;
import management.Task;
import management.TaskHighScores;
import management.TaskQuit;
import management.TaskRunGame;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents a game.
 */
public class Ass7Game {
    private HighScoresTable table;
    /**
     * Runs a game.
     *
     * @param args User arguments.
     * @throws Exception Exception.
     */
    public static void main(String[] args) throws Exception {

        GUI gui = new GUI("Space Invaders", 800, 600);
        AnimationRunner runner = new AnimationRunner(gui);
        HighScoresTable highScoresTable = HighScoresTable.loadFromFile(new File(HighScoresTable.FILE_NAME));
        //GameFlow flow = new GameFlow(runner, gui.getKeyboardSensor(), 7, highScoresTable);
        MenuAnimation<Void> menuAnimation = new MenuAnimation<Void>(gui.getKeyboardSensor());
        Task<Void> highScores = new TaskHighScores(highScoresTable, gui.getKeyboardSensor(), gui);
        Task<Void> quit = new TaskQuit();

        MenuAnimation<Void> levelsMenu = new MenuAnimation<Void>(gui.getKeyboardSensor());
        //      SubMenu levelsSubMenu = new SubMenu(levelsMenu, runner);

        //        for (String levelKey: levelsMap.keySet()) {
        //            levelsMenu.addSelection(levelKey, levelsMap.get(levelKey),
        //                    (Task<Void>) () -> {
        //                        String levelFile = levelSet.getLevelFile(levelKey);
        //                        List<LevelInformation> levels = getLevelInformations(levelFile);
        //
        //                        flow.runLevels(levels);
        //                        return null;
        //                    });
        //        }

        //*//

        GameFlow flow = new GameFlow(runner, gui.getKeyboardSensor(), 1, highScoresTable);
        ArrayList<LevelInformation> levels = new ArrayList<>();
        Task<Void> runGame = new TaskRunGame(flow, levels);

        menuAnimation.addSelection("s", "Start game", runGame);
        menuAnimation.addSelection("h", "Show high scores", highScores);
        menuAnimation.addSelection("q", "Quit game", quit);

        while (true) {

            runner.run(menuAnimation);
            Task<Void> selectedTask = (Task<Void>) menuAnimation.getStatus();
            selectedTask.run();
            menuAnimation.resetStop();
        }
    }
}
