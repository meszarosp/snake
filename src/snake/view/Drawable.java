package snake.view;

import java.awt.*;

public interface Drawable {
    /**
     * Rajzol egy paraméterként átadott Graphics objektumra.
     * @param g Az a Graphics objektum, amire rajzolni kell.
     */
    public void draw(Graphics g);
}
