package snake.model;

import snake.view.Drawable;

import java.io.Serializable;


public abstract class Entity implements Serializable {

    /**
     * Az entitás közeppontjának x (vízszintes) koordinátája.
     * nemnegatív szám, 0 és a pálya szélessége között van
     */
    protected int x;
    /**
     * Az entitás középpontjának y (vízszintes) koordinátája.
     * nemnegatív szám, 0 és a pálya magassága között van
     */
    protected int y;
    /**
     * Az entitás mérete.
     */
    protected int size;
    /**
     * Az a pálya, amelyen az entitás van.
     */
    protected Level level;
    /**
     * Egy Drawable, amely ezt az entitást rajzolja ki.
     */
    protected Drawable drawable;

    /**
     * Konstruktor, inicializálja az attribútumokat.
     * @param x A középpont x koordinátája.
     * @param y A középpont y koordinátája.
     * @param size Méret.
     * @param level Pálya, amelyen az entitás lesz.
     */
    public Entity(int x, int y, int size, Level level) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.level = level;
    }

    /**
     * Getter az entitás kirajzolójához.
     * @return Az entitás rajzolója.
     */
    public Drawable getDrawable() {
        return drawable;
    }

    /**
     * Setter az entitás rajzolójához.
     * @param drawable Az a rajzoló, ami az entitásé lesz.
     */
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    /**
     * Ellenőrzi, hogy ütközik-e az entitás a kígyóval, és ennek eredményeképpen dönt.
     * @param snake A kígyó, amivel ütközik az entitás.
     */
    public abstract void collideWithSnake(Snake snake);

    /**
     * Getter az x koordinátához.
     * @return Az x koordináta.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter az y koordinátához.
     * @return Az y koordináta.
     */
    public int getY() {
        return y;
    }

    /**
     * Visszaadja az entitás méretét.
     * @return Az entités mérete.
     */
    public int getSize() {
        return size;
    }


    /**
     * Szól a pályának, hogy törölje az entitást onnan.
     */
    protected void removeFromLevel(){
        if (level != null){
            level.removeEntity(this);
        }
    }
}
