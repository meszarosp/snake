package snake.view;

import snake.model.GoldenApple;

import java.awt.*;
import java.io.Serializable;


public class GoldenAppleDrawable implements Drawable, Serializable {
    /**
     * Az az aranyalma, amelyiket kirajzolja.
     */
    private GoldenApple model;

    /**
     * Konstruktor, inicializálja az egyetlen attribútumot.
     * @param goldenApple Az az aranyalma,amelyiket kirajzolja.
     */
    public GoldenAppleDrawable(GoldenApple goldenApple){
        model = goldenApple;
    }

    /**
     * Kirajzolja az aranyalmát, tehát kirajzol egy sárga kiszínezett kört, amelynek középpontja és
     * átmérője az aranyalmáéval megegyező.
     * Erre rárajzol egy narancssárga körvonalat is ugyanazzal a középponttal és mérettel.
     * @param g Az a Graphics objektum, amire rajzolni kell.
     */
    @Override
    public void draw(Graphics g) {
        int modelX = model.getX();
        int modelY = model.getY();
        int modelSize = model.getSize();

        g.setColor(Color.YELLOW);
        g.fillOval(modelX - modelSize/2,  modelY - modelSize/2, modelSize, modelSize);
        g.setColor(Color.ORANGE);
        g.drawOval(modelX - modelSize/2,  modelY - modelSize/2, modelSize, modelSize);
    }
}
