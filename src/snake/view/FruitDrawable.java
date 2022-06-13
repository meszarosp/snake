package snake.view;

import snake.model.Fruit;

import java.awt.*;
import java.io.Serializable;

public class FruitDrawable implements Drawable, Serializable {
    /**
     * Az a gyümölcs, amelyiket kirajzolja.
     */
    private Fruit model;

    /**
     * Konstruktor, inicializál.
     * @param fruit Az a gyümölcs, amelyiket kirajzolja.
     */
    public FruitDrawable(Fruit fruit){
        model = fruit;
    }

    /**
     * Kirajzolja a gyömölcsöt, tehát kirajzol egy piros kiszínezett kört, amelynek középpontja és
     * átmérője a gyümölcsével megegyező.
     * Erre rárajzol egy sárga körvonalat is ugyanazzal a középponttal és mérettel.
     * @param g Az a Graphics objektum, amire rajzolni kell.
     */
    @Override
    public void draw(Graphics g) {
        int modelX = model.getX();
        int modelY = model.getY();
        int modelSize = model.getSize();

        g.setColor(Color.RED);
        g.fillOval(modelX - modelSize/2,  modelY - modelSize/2, modelSize, modelSize);
        g.setColor(Color.YELLOW);
        g.drawOval(modelX - modelSize/2,  modelY - modelSize/2, modelSize, modelSize);
    }
}
