package snake.view;

import snake.model.Game;
import snake.model.Level;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * Frame amely tartalmazza az alkalmazás elemeit.
 */
public class GameFrame extends JFrame{
    /**
     * A játékmező szélessége pixelben.
     */
    public int WIDTH = 800;
    /**
     * A játékmesző magassága pixelben.
     */
    public int HEIGHT = 600;

    /**
     * Másodpercenkénti képkocka szám.
     */
    public final int FPS = 60;
    /**
     * Az előző játékok adatait tartalmazó JList.
     */
    private JList<String> leaderboard;
    /**
     * Az előző játékok adatait tartalmazó lista modell.
     */
    private DefaultListModel<String> leaderboardModel;
    /**
     * FileChooser a mentésnél/beolvasásnál a fájl kiválasztásához.
     */
    private JFileChooser fileChooser = new JFileChooser();
    /**
     * Aktuális játék.
     */
    private Game game;

    /**
     * Konstruktor. Beállítja a frame tulajdonságait. Elkezd egy új játékot. Beállítja a fileChoosert.
     */
    public GameFrame(){
        super("Snake");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        initMenuBar();
        setVisible(true);
        addWindowListener(new GameFrameWindowListener());
        newGame();
        game.start();
        pack();

        fileChooser.addChoosableFileFilter(
                new FileNameExtensionFilter("Snake game files (.snek)", "snek"));
        fileChooser.setMultiSelectionEnabled(false);
    }

    /**
     * Készít egy új GameFrame példányt.
     * @param args A program parancssori argumentumai.
     */
    public static void main(String[] args){
        GameFrame gf = new GameFrame();
    }

    /**
     * A program kezdetekor beolvassa a leaderboardot, amikor bezárják az ablakot, akkor pedig elmenti azt.
     */
    private class GameFrameWindowListener extends WindowAdapter {

        /**
         * Az ablak megnyitásakor beolvassa az előző játékok listáját, deszerializál.
         * Ha nem létezik a fájl, akkor készít egy újat.
         * @param e A feldolgozandó esemény.
         */
        public void windowOpened(WindowEvent e){
            File file = new File(System.getProperty("user.dir"), "leaderboard.dat");
            try {
                FileInputStream f = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(f);
                leaderboardModel = (DefaultListModel<String>) in.readObject();
                in.close();
            } catch (IOException | ClassNotFoundException exception) {
                leaderboardModel = new DefaultListModel<String>();
            }
            if (leaderboardModel == null){
                leaderboardModel = new DefaultListModel<String>();
            }
            leaderboard = new JList<String>(leaderboardModel);
            leaderboard.setFocusable(false);
            add(leaderboard, BorderLayout.EAST);
            leaderboard.setVisible(false);
        }

        /**
         * Az ablak bezárásakor fájlba menti, szerializálja az előző játékok listáját.
         * @param e A feldolgozandó esemény.
         */
        public void windowClosing(WindowEvent e){
            File file = new File(System.getProperty("user.dir"), "leaderboard.dat");
            try {
                FileOutputStream f = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(f);
                out.writeObject(leaderboardModel);
                out.close();
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null, "Error happened while saving the leaderboard! Try again!");
            }
        }
    }

    /**
     * ActionListener a menühöz, minden menüponthoz tartozó eseményt kezeli.
     */
    private class MenuActionListener implements ActionListener {
        /**
         * Végrehajtja az adott menüpontnak megfelelő teendőket.
         * Végrehajtás közben szünetelteti a futó játékot.
         * @param e A feldolgozandó esemény.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game != null) {
                game.stop();
            }
            String command = e.getActionCommand();
            if ("New Game".equals(command)){
                newGame();
            }else if("Load Game".equals(command)){
                loadGame();
            }else if("Save Game".equals(command)){
                saveGame();
            }else if("Show Leaderboard".equals(command)){
                JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
                leaderboard.setVisible(source.getState());
                pack();
            }else if("Set width".equals(command)){
                String input = JOptionPane.showInputDialog("Please type in the preferred width!");
                try {
                    WIDTH = Integer.parseInt(input);
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null, "Incorrect input! Try again!");
                }

            }else if("Set height".equals(command)) {
                String input = JOptionPane.showInputDialog("Please type in the preferred height!");
                try {
                    HEIGHT = Integer.parseInt(input);
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null, "Incorrect input! Try again!");
                }
            }
            if (game != null) {
                game.start();
            }
        }
    }

    /**
     * Indít egy új játékot. Eltávolítja az előzőt és hozzáadja az újat.
     */
    public void newGame(){
        if (game != null){
            game.clear();
            remove(game);
        }
        game = new Game(this, new Dimension(WIDTH, HEIGHT));
        game.setLevel(new Level(game, WIDTH, HEIGHT, FPS));
        add(game, BorderLayout.CENTER);
        pack();
    }

    /**
     * Bekér a felhasználótól egy fájlt, majd ebből a fájlból megpróbálja beolvasni a korábban elmentett játékot.
     * Ha sikertelen a beolvasás, akkor szól a felhasználónak.
     */
    private void loadGame(){
        int userSelection = fileChooser.showDialog(this, "Load");
        System.out.println(userSelection);
        if (userSelection == JFileChooser.APPROVE_OPTION){
            try {
                Game loadedGame = Game.loadGameFromFile(fileChooser.getSelectedFile());
                remove(game);
                add(loadedGame, BorderLayout.CENTER);
                game = loadedGame;
            } catch (IOException | ClassNotFoundException exception) {
                JOptionPane.showMessageDialog(null, "Error happened while loading the game! Try again!");
            }
        }
    }

    private void saveGame(){
        int userSelection = fileChooser.showDialog(this, "Save");
        if (userSelection == JFileChooser.APPROVE_OPTION){
            try {
                Game.saveGameToFile(game, fileChooser.getSelectedFile());
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null, "Error happened while saving the game! Try again!");
            }
        }
    }

    /**
     * Létrehozza a menubar-t.
     */
    private void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        MenuActionListener mal = new MenuActionListener();
        initGameMenu(menuBar, mal);
        initSettingsMenu(menuBar, mal);
        initLeaderboardMenu(menuBar, mal);

        setJMenuBar(menuBar);
    }

    /**
     * Létrehozza a game menüt és az az alá tartozó menüpontokat és
     * a paraméterként átadott actionlistenert hozzáadja a menüpontokhoz.
     * @param menuBar Az a menüsor, ahova a game menü kerül.
     * @param al Az az actionlistener, amely az egyes menüpontokhoz hozzáadódik.
     */
    private void initGameMenu(JMenuBar menuBar, ActionListener al){
        JMenu game = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.setActionCommand("New Game");
        JMenuItem saveGame = new JMenuItem("Save Game");
        saveGame.setActionCommand("Save Game");
        JMenuItem loadGame = new JMenuItem("Load Game");
        loadGame.setActionCommand("Load Game");
        game.add(newGame);
        game.add(saveGame);
        game.add(loadGame);
        newGame.addActionListener(al);
        saveGame.addActionListener(al);
        loadGame.addActionListener(al);

        menuBar.add(game);
    }

    /**
     * Létrehozza a settings menüt és az az alá tartozó menüpontokat és
     * a paraméterként átadott actionlistenert hozzáadja a menüpontokhoz.
     * @param menuBar Az a menüsor, ahova a settings menü kerül.
     * @param al Az az actionlistener, amely az egyes menüpontokhoz hozzáadódik.
     */
    private void initSettingsMenu(JMenuBar menuBar, ActionListener al){
        JMenu settings = new JMenu("Settings");
        JMenuItem width = new JMenuItem("Width");
        width.setActionCommand("Set width");
        JMenuItem height = new JMenuItem("Height");
        height.setActionCommand("Set height");
        settings.add(width);
        settings.add(height);
        width.addActionListener(al);
        height.addActionListener(al);

        menuBar.add(settings);
    }

    /**
     * Létrehozza a leaderboard menüt és az az alá tartozó menüpontokat és
     * a paraméterként átadott actionlistenert hozzáadja a menüpontokhoz.
     * @param menuBar Az a menüsor, ahova a leaderboard menü kerül.
     * @param al Az az actionlistener, amely az egyes menüpontokhoz hozzáadódik.
     */
    private void initLeaderboardMenu(JMenuBar menuBar, ActionListener al){
        JMenu leaderboard = new JMenu("Leaderboard");
        JCheckBoxMenuItem show = new JCheckBoxMenuItem("Show"); show.setActionCommand("Show Leaderboard");
        leaderboard.add(show);
        show.addActionListener(al);

        menuBar.add(leaderboard);
    }

    /**
     * Akkor hívódik meg, amikor véget ért a játék és a korábbi játékok listájába új elem kerül.
     * @param leaderboardEntry Az új elem a listába.
     */

    public void gameEnded(String leaderboardEntry){
        leaderboardModel.addElement(leaderboardEntry);
        newGame();
        game.start();
    }
}
