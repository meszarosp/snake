package snake.view;

import snake.model.Buff;
import snake.model.Snake;
import snake.model.SnakeElement;
import snake.model.SnakeLine;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;


public class SnakeDrawable implements Drawable, Serializable {

    /**
     * A kígyó, amelyet kirajzol.
     */
    private Snake snake;

    /**
     * Konstruktor, beállítja a kígyót.
     * @param snake A kígyó, amelyet kirajzol,
     */
    public SnakeDrawable(Snake snake) {
        this.snake = snake;
    }

    /**
     * Hátulról előrefelé kirajzolja az összes kígyóvonal összes kígyóelemét.
     * Egy kígyóelemet egy világoszöld kitöltött körként rajzol ki, amelyet egy sötétebb zöld körvonal vesz körül.
     * Az körök így egymásra lapolódnak, a legelső, a kígyó feje van legelől.
     * @param g Az a Graphics objektum, amire rajzolni kell.
     */
    @Override
    public void draw(Graphics g){
        LinkedList<SnakeLine> lines = snake.getLines();
        ListIterator<SnakeLine> linesIter = lines.listIterator(lines.size());
        while (linesIter.hasPrevious()){
            LinkedList<SnakeElement> elements = linesIter.previous().getElements();
            ListIterator<SnakeElement> elementsIter = elements.listIterator(elements.size());
            while (elementsIter.hasPrevious()){
                SnakeElement currentElement = elementsIter.previous();
                int elementX = currentElement.getX();
                int elementY = currentElement.getY();
                int elementSize = currentElement.getSize();
                g.setColor(new Color(0,102,51));
                g.fillOval(elementX-elementSize/2, elementY-elementSize/2, elementSize, elementSize);
                g.setColor(Color.GREEN);
                g.fillOval(elementX-elementSize*9/20, elementY-elementSize*9/20, elementSize*9/10, elementSize*9/10);
            }
        }
        drawInfos(g);
    }

    /**
     * A bal felső sarokba írja ki a kígyó pontszámát, hosszát, seességét és életét.
     * Ez alá külön sorokban írja ki a buffokat.
     * @param g Az a Graphics objektum, amire rajzolni kell.
     */
    private void drawInfos(Graphics g){
        String info = "POINTS: " + snake.getPoints() + " LENGTH: " + snake.getLength() + " SPEED: " + snake.getVelocity() + " HEALTH: " + snake.getHealth();
        g.drawString(info, 10, 20);
        LinkedList<Buff>  buffs = snake.getBuffs();
        int i = 0;
        for (Buff b : buffs){
            g.drawString(b.toString(), 10, 30+i*10);
            i++;
        }
    }
}
