package snake.model;

import snake.view.Drawable;
import snake.view.GameFrame;
import snake.view.SnakeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;

public class Game extends JPanel implements Serializable {
    /**
     * FPS szám, másodpercenként ennyiszer lépteti a pályát és ennyiszer rajzolja újra a játékot.
     */
    public static final int FPS = 60;
    /**
     * Az a GameFrame, amelyikben a Game van.
     */
    private transient GameFrame gameFrame;
    /**
     * A pálya, amelyikkel éppen játszanak, amelyiket kirajzol és léptet.
     */
    private Level level;
    /**
     * Timer, aminek hatására lépteti a pályát és újrarajzol.
     */
    private Timer timer;
    /**
     * A kirajzolandó elemek listája.
     */
    private LinkedList<Drawable> drawables = new LinkedList<Drawable>();

    /**
     * Listener a játék timeréhez.
     */
    private class TimerActionListener implements ActionListener, Serializable {

        /**
         * Lépteti a Game pályáját és újrarajzoltatja.
         * @param e Az esemény, amelyiket feldolgoz.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            level.step();
            repaint();
        }
    }

    /**
     * Konstruktor. Meghívja az ős konstruktorát, beálltítja a méretet, Inicializálja a timert, hogy minden másodpercben
     * FPS darabszor triggereljen eventet.
     * @param gameFrame Az a GameFrame objektum, amelyikben ez az objektum van.
     * @param dimension A játékmező mérete.
     */
    public Game(GameFrame gameFrame, Dimension dimension){
        super(true);
        this.gameFrame = gameFrame;
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setSize(dimension);

        timer = new Timer(0,new TimerActionListener());
        timer.setDelay(1000/FPS);
    }

    /**
     * Setter a gameFrame attribútumhoz.
     * @param gameFrame Az a GameFrame objektum, amelyikben ez a Game van.
     */
    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    /**
     * Törli a kirajzolandók listáját.
     */
    public void clear(){
        drawables.clear();
    }

    /**
     * Setter a level attribútumhoz. Hozzáadja a gameFrame-hez a pálya kígyójának keylistenerét.
     * @param level Az a pálya, amire beállítja a pályáját.
     */
    public void setLevel(Level level){
        this.level = level;
        gameFrame.addKeyListener(new SnakeKeyListener(level.getSnake()));
    }

    /**
     * Hozzáad egy kirajzolandó elemet a kirajzolandók listájához.
     * @param drawable Az a kirajzolandó, amit hozzá kell adni.
     */
    public void addDrawable(Drawable drawable){
        drawables.add(drawable);
    }

    /**
     * Töröl egy kirajzolandó elemet a kirajzolandók listájából.
     * @param drawable Az elem, amit törölni kell.
     */
    public void removeDrawable(Drawable drawable){
        drawables.remove(drawable);
    }

    /**
     * Meghívja az ős paintComponent metódusát, és kirajzoltatja a kirajzolandókat a paraméterként kapott Graphics
     * objektumra.
     * @param g Az a Graphics objektum, amelyre a rajzolás történik.
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        drawables.forEach(d->d.draw(g));
    }

    /**
     * Ezzel jelezheti a pálya, hogy véget ért a játék.
     * Leállítja az időzítőt, bekér a felhasználótól egy nevet, majd szól a gameFrame-nek, hogy végetért a játék és adja
     * hozzá a leaderboardhoz az eredményeket.
     * Ezután letörli a képernyőt.
     * @param result A játék végeredménye, IGAZ, ha a játékos nyert, HAMIS, ha vesztett.
     */
    public void gameEnded(boolean result){
        timer.stop();
        String message = (result ? "Game won!" : "Game Lost!") + " What is your name?";
        String playerName = JOptionPane.showInputDialog(this, message);
        if (playerName == null){
            playerName = "Snake";
        }
        gameFrame.gameEnded( playerName + "  " + level.getSnake().getPoints() + " " + level.getSnake().getLength());
        
        drawables.clear();
        repaint();
    }

    /**
     * Leállítja a játékot, megállítja az időzítőt.
     */
    public void stop(){
        timer.stop();
        //repaint();
    }

    /**
     * Elindítja a játékot, az időzítőt.
     */
    public void start(){
        timer.start();
    }

    /**
     * Beolvas visszasorosítással egy Game objektumot egy fájlból.
     * @param file Az a file, ahonnan be kell olvasni.
     * @return A Game objektum, amit beolvasott.
     * @throws IOException Ha a beolvasás a fájlkezelés miatt hiba történik, akkor IOException dob.
     * @throws ClassNotFoundException Ha ismeretlen osztály van a beolvasandó fájlban.
     */
    public static Game loadGameFromFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fis);
        Game game = (Game) in.readObject();
        in.close();
        return game;
    }

    /**
     * Fájlba ment sorosítással egy paraméterként átadott Game objektumot.
     * @param game Game objektum, amelyet sorosítani kell.
     * @param file A fájl, ahova írni kell.
     * @throws IOException Ha a fájlkezelés során valamilyen hiba keletkezik.
     */
    public static void saveGameToFile(Game game, File file) throws IOException {
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(f);
        out.writeObject(game);
        out.close();
    }
}
