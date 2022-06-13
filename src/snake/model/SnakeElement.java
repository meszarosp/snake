package snake.model;

import java.io.Serializable;

public class SnakeElement implements Serializable {
    /**
     * Az elem középpontjának x koordinátája.
     */
    private double x;
    /**
     * Az elem középpontjának y koordinátája.
     */
    private double y;
    /**
     * Az elem mérete. (Kőr átmérője.)
     */
    private int size;

    /**
     * Üres konstruktor.
     */
    public SnakeElement(){}

    /**
     * Konstruktor, inicializálja az attribútumokat.
     * @param x Az elem középpontjának x koordinátája.
     * @param y Az elem középpontjának y koordinátája.
     * @param size Az elem mérete, átmérője.
     */
    public SnakeElement(double x, double y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    /**
     * Kiszámolja az elem új pozícióját a paraméterként átadott sebességgel és iránnyal.
     * @param velocity Az a sebesség amivel halad az elem, pixel/lépés mértékegységben.
     * @param angle Annak az iránynak a vízszintessel bezárt szöge, amerre az elem új pozícióját ki kell számolni.
     */
    public void newPosition(double velocity, double angle){
        x +=  velocity*Math.cos(angle);
        y += -velocity*Math.sin(angle);
    }

    /**
     * Kiszámolja az elem pozícióját egy másik elem mögött.
     * Úgy számolja ki, hogy az elem és a paraméterként átadott elem között offset legyen a távolság és
     * a két elemre illesztett egyenes a vízszintessel angle szöget zárjon be.
     * @param e Az elem amely mögött ki kell számolni az új pozíciót.
     * @param angle A szög amelyet a vízszintessel bezár a két elemre illesztett egyenes.
     * @param offset A két elem középpontja közti távolság.
     */
    public void calculatePositionBehindElement(SnakeElement e, double angle, int offset){
        x = e.x - Math.cos(angle)*offset;
        y = e.y + Math.sin(angle)*offset;
        size = e.size;
    }

    /**
     * Getter az x koordinátához egész számként.
     * @return Az x koordináta integerré castolva.
     */
    public int getX(){
        return (int) x;
    }

    /**
     * Getter az y koordinátához egész számként.
     * @return Az y koordináta integerré castolva.
     */
    public int getY(){
        return (int) y;
    }

    /**
     * Getter az x koordinátához valós számként.
     * @return Az x koordináta valós számként.
     */
    public double getXDouble(){
        return x;
    }

    /**
     * Getter az y koordinátához valós számként.
     * @return Az y koordináta valós számként.
     */
    public double getYDouble(){
        return y;
    }

    /**
     * Visszaadja az elem méretét.
     * @return Az elem mérete.
     */
    public int getSize() {
        return size;
    }
}
