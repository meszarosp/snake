package snake.model;

import java.awt.*;

public class Wall extends Entity{
    /**
     * A fal 4 sarkát tartalmazó polígon.
     */
    private Polygon polygon = new Polygon();
    /**
     * Szög amelyben a fal áll.
     * A szöget a vízszintessel bezárva a másik irányba kell mérni. ("lefelé")
     * 0 és PI közötti érték.
     */
    private double angle;

    /**
     * A fal magassága.
     * Ez sokkal kisebb, mint a szélesség.
     */
    private int height;

    /**
     * A fal szélessége.
     */
    private int width;

    /**
     * A középpontján a hossz szélességében (a megadott szög szerint) átmenő egyenes "a" paramétere.
     * Ez a normálvektor első koordinátája.
     * (a*x+b*y+c=0 képlet szerint, ahol x, y tetszőleges pont)
     */
    private double a;
    /**
     * A középpontján a hossz szélességében (a megadott szög szerint) átmenő egyenes "b" paramétere.
     * Ez a normálvektor második koordinátája.
     * (a*x+b*y+c=0 képlet szerint, ahol x, y tetszőleges pont)
     */
    private double b;

    /**
     * A középpontján a hossz szélességében (a megadott szög szerint) átmenő egyenes "c" paramétere.
     * (a*x+b*y+c=0 képlet szerint, ahol x, y tetszőleges pont)
     */
    private double c;

    /**
     * IGAZ, ha a fal törhető, HAMIS, ha a fal nem törhető.
     */
    private boolean invincible;

    /**
     * Sebzés amelyet okoz, amikor érintkezik a kígyóval.
     */
    private double damage;

    /**
     * A fal egyenesének egy olyan pontjának az x koordinátája,
     * amelyik metszi a fal rövidebbik oldalát (azaz a magasságát).
     * 2 ilyen pont lehet, ebből az a pont, amelyiknek kisebb az x vagy az y koordinátája.
     */
    private double endPointX1;
    /**
     * A fal egyenesének egy olyan pontjának az x koordinátája,
     * amelyik metszi a fal rövidebbik oldalát (azaz a magasságát).
     * 2 ilyen pont lehet, ebből az a pont, amelyiknek nagyobb az x vagy az y koordinátája.
     */
    private double endPointX2;
    /**
     * A fal egyenesének egy olyan pontjának az y koordinátája,
     * amelyik metszi a fal rövidebbik oldalát (azaz a magasságát).
     * 2 ilyen pont lehet, ebből az a pont, amelyiknek kisebb az x vagy az y koordinátája.
     */
    private double endPointY1;
    /**
     * A fal egyenesének egy olyan pontjának az y koordinátája,
     * amelyik metszi a fal rövidebbik oldalát (azaz a magasságát).
     * 2 ilyen pont lehet, ebből az a pont, amelyiknek nagyobb az x vagy az y koordinátája.
     */
    private double endPointY2;

    /**
     * Konstruktor, inicializálja az attribútumokat. Kiszámolja a fal csócsainak koordinátáit, a falon áthaladó
     * egyenes egyenletének együtthatóit és az egyenesnek a falon belüli szakaszának koordinátáit.
     * @param centerX A fal középpontjának x koordinátája.
     * @param centerY A fal középpontjának y koordinátája.
     * @param width A fal szélessége, ez sokkal nagyobb, mint a magasság.
     * @param height A fal magassága, ez sokkal kisebb, mint a szélesség.
     * @param angle A fal vízszintessel bezárt szöge, a másik irányba.
     * @param invincible IGAZ, ha törhető a fal, HAMIS, ha nem.
     * @param damage A fal által okozott sebzés, ha a kígyóval ütközik.
     * @param level A pálya, amelyen a fal van.
     */
    public Wall(int centerX, int centerY, int width, int height, double angle, boolean invincible, double damage, Level level) {
        super(centerX, centerY, width, level);
        while (angle > Math.PI){
            angle -= Math.PI;
        }
        while (angle < 0){
            angle += Math.PI;
        }
        this.angle = angle;
        this.height = height;
        this.width = width;
        this.invincible = invincible;
        this.damage = damage;

        initPolygon();
        initEndPoints();

        a = -Math.sin(angle);
        b = Math.cos(angle);
        c = -a*centerX - b*centerY;
    }

    /**
     * Kiszámolja a fal 4 csúcsát. Először kiszámolja a 4 sarkot,
     * mintha nem lenne elforgatva a fal utána pedig elforgatja.
     */
    private void initPolygon(){
        Polygon tempPolygon = new Polygon();
        tempPolygon.addPoint(x + width/2, y + height/2);
        tempPolygon.addPoint(x + width/2, y - height/2);
        tempPolygon.addPoint(x - width/2, y - height/2);
        tempPolygon.addPoint(x - width/2, y + height/2);

        for(int i = 0; i < 4; i++){
            polygon.addPoint(rotateX(angle, tempPolygon.xpoints[i], tempPolygon.ypoints[i]),
                    rotateY(angle, tempPolygon.xpoints[i], tempPolygon.ypoints[i]));
        }
    }

    /**
     * Kiszámolja a fal közepén átmenő egyenes falon belül lévő szakaszának 2 végpontjának koordinátáit.
     */
    private void initEndPoints(){
        endPointX2 = (double)(polygon.xpoints[0]+polygon.xpoints[1])/2;
        endPointY2 = (double)(polygon.ypoints[0]+polygon.ypoints[1])/2;
        endPointX1 = (double)(polygon.xpoints[2]+polygon.xpoints[3])/2;
        endPointY1 = (double)(polygon.ypoints[2]+polygon.ypoints[3])/2;

        if (endPointX2 < endPointX1){
            double temp = endPointX1;
            endPointX1 = endPointX2;
            endPointX2 = temp;
        }
        if (endPointY2 < endPointY1){
            double temp = endPointY1;
            endPointY1 = endPointY2;
            endPointY2 = temp;
        }
    }

    /**
     * Kiszámolja egy pontnak a paraméterként átadott szöggel való elforgatottjának az x koordinátáját.
     * A forgatást a fal középpontjára nézi.
     * @param angle A szög amennyivel el kell forgatni (balra).
     * @param x1 A pont x koordinátája.
     * @param y1 A pont y koordinátája.
     * @return Az elforgatott pont új x koordinátája.
     */
    private int rotateX(double angle, double x1, double y1){
        return (int) Math.round((x1-x)*Math.cos(angle)-(y1-y)*Math.sin(angle)) + x;
    }

    /**
     * Kiszámolja egy pontnak a paraméterként átadott szöggel való elforgatottjának az x koordinátáját.
     * A forgatást a fal középpontjára nézi.
     * @param angle A szög amennyivel el kell forgatni (balra).
     * @param x1 A pont x koordinátája.
     * @param y1 A pont y koordinátája.
     * @return Az elforgatott pont új y koordinátája.
     */
    private int rotateY(double angle, double x1, double y1){
        return (int) Math.round((x1-x)*Math.sin(angle)+(y1-y)*Math.cos(angle)) + y;
    }

    /**
     * Megnézi, hogy ütközik-e a fallal a kígyó. Ha nem ütköznek, akkor nem csinál semmit.
     * Ha igen, és a fal nem törhető, akkor eltávolítja magát a pályára.
     * Ha ütköznek és a fal törhető, akkor elforgatja a kígyót úgy, hogy az visszapattanjon róla.
     * Ütközés esetén megsebzi a kígyót.
     * @param snake A kígyó, amivel ütközik az entitás.
     */
    @Override
    public void collideWithSnake(Snake snake) {
        if (doesCollide(snake)) {
            if (invincible) {
                double eps = 0.001;
                double snakeAngle = snake.getAngle();
                if ((-eps <= angle && angle <= eps) || (Math.PI / 2 - eps <= angle && angle <= Math.PI / 2 + eps)) {
                    snake.rotate(2 * angle - snakeAngle);
                } else {
                    snake.rotate(-2 * angle - snakeAngle);
                }
            }else{
                removeFromLevel();
            }
            snake.damageHealth(damage);
        }
    }

    /**
     * Kiszámolja, hogy a kígyó és a fal ütköznek-e.
     * Kiszámolja azt az egyenest, amelyik merőleges a fal egyenesére és a kígyó fejének középpontján átmegy.
     * Ezután meghatározza a két egyenes együtthatóiból a két egyenes metszéspontját.
     * A metszéspont segítségével kiszámolja a kígyó és a fal egyenesének távolságát.
     * Ha a kígyó és az egyenes távolsága akkora, hogy ütköznek, akkor megvizsgálja azt is, hogy a metszéspont
     * a falon belül lévő szakasz két végpontja között van-e. Tehát, hogy a metszéspont az egyenes azon részén van-e,
     * amelyik a falon belül van.
     * @param snake A kígyó amelyikkel ütközik.
     * @return IGAZ, ha ütköznek, HAMIS, ha nem.
     */
    private boolean doesCollide(Snake snake){
        int snakeX = snake.getX();
        int snakeY = snake.getY();
        int snakeSize = snake.getSize();
        //képlet: (a*x0+b*y0+c)/(Math.sqrt(a*a+b*b))
        //a konstruktorban a, b kiszámolása révén
        // (a, b) egységvektor, ezért a nevező 1
        double dist = a*snakeX+b*snakeY+c;

        //Az egyenesre merőleges, a Snake első elemének
        // középpontján átmenő egyenes
        double perpendA = b;
        double perpendB = -a;
        double perpendC = -perpendA*snakeX - perpendB*snakeY;


        double intersectY;
        double intersectX;
        if (a == 0){
            intersectY = -c/b;
            intersectX = -perpendC/(b);
        }else{
            intersectY = (-perpendC+perpendA*c/a)/(perpendB-perpendA*b/a);
            intersectX = (-c-intersectY*perpendA)/a;
        }

        return (endPointX1 <= intersectX + snakeSize/2 && intersectX - snakeSize/2 <= endPointX2) &&
                (endPointY1 <= intersectY + snakeSize/2 && intersectY - snakeSize/2 <= endPointY2)
                && Math.abs(dist) < (double)snakeSize/2 + (double)height/2;
    }

    /**
     * Megadja a fal 4 sarkát tartalmazó poligont.
     * @return A falat alkotó poligon.
     */
    public Polygon getPolygon() {
        return polygon;
    }
}
