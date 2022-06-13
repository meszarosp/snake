package snake.view;

import snake.model.Wall;

import java.awt.*;
import java.io.Serializable;

public class WallDrawable implements Drawable, Serializable {

    /**
     * A fal objektum, amit kirajzol.
     */
    private Wall wall;

    /**
     * Konstruktor, inicializálja a "wall" attribútumot.
     * @param wall A fal, amit kirajzol.
     */
    public WallDrawable(Wall wall) {
        this.wall = wall;
    }

    /**
     * Kirajzolja kék színnel egy teli poligonként a falat.
     * @param g Az a Graphics objektum, amire rajzolni kell.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(32, 23, 155));
        g.fillPolygon(wall.getPolygon());
    }
}
