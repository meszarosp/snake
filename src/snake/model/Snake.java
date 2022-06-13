package snake.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;


public class Snake implements Serializable, Steppable {
    /**
     * A kígyó x (vízszintes) koordinátája.
     * nemnegatív szám, 0 és a pálya szélessége között van
     */
    private int x;
    /**
     * A kígyó y (függőleges) koordinátája.
     * nemnegatív szám, 0 és a pálya magassága között van
     */
    private int y;
    /**
     * A kígyó mérete, a kígyót reprezentáló karikák sugara.
     */
    private int size;
    /**
     * A kígyó sebessége pixel/lépés mértékegységben.
     * A sebességre és az offsetre az a megköztés,
     * hogy sebességnek osztania kell az offsetet.
     */
    private double velocity;
    /**
     * A kígyó elejének vízszintessel bezárt szöge.
     * Az irány amerre a kígyó megy.
     * 0 és 2*PI közötti érték
     */
    private double angle;
    /**
     * A kígyó hossza, ahány körből, elemből a kígyó áll.
     */
    private int length;
    /**
     * A körök, elemek közötti átfedés pixelben.
     * Két egymást követő kör középpontjának ekkora a távolsága.
     * A sebességre és az offsetre az a megköztés,
     * hogy sebességnek osztania kell az offsetet.
     */
    private int offset;
    /**
     * A kígyó élete, 100-ról indul és legfeljebb 100 lehet.
     */
    private double health = 100;

    /**
     * Lépésenkénti életregeneráció. Minden lépésben ennyivel lesz több a health.
     */
    private double healthRegen = 1.0/60;
    /**
     * Pálya, amelyen a kígyó tartózkodik.
     */
    private Level level;
    /**
     * Az kígyó elem, a kígyó feje.
     */
    private SnakeElement firstElement;
    /**
     * Minden lépésben a forgás során ekkora szöggel fordul.
     */
    private double angleIncrement = Math.PI/64;
    /**
     * IGAZ, ha a kígyónak kell balra fordulnia,
     * HAMIS, ha nem kell balra fordulnia.
     */
    private boolean leftRotate = false;
    /**
     * IGAZ, ha a kígyónak kell jobbra fordulnia,
     * HAMIS, ha nem kell jobbra fordulnia.
     */
    private boolean rightRotate = false;

    /**
     * A kígyó tulajdonságai, amiket pl buffok tudnak módosítani.
     */
    public static String[] attributes = new String[]{"points", "length", "damage", "healthRegen"};

    /**
     * Az összegyűjtött pontok száma.
     */
    private int points = 0;


    /**
     * A kígyót alkotó kígyóvonalak listája.
     * A legelső eleme ennek a listának az, amelyik legelől van, tehát az, amelyik a kígyó fejét is tartalmazza.
     */
    private LinkedList<SnakeLine> lines = new LinkedList<SnakeLine>();

    /**
     * A kígyón éppen lévő buffok listája.
     * A legelső eleme a kígyó eleje, tehát amerre megy a kígyó. A legutolsó eleme a kígyó legvége.
     */
    private LinkedList<Buff> buffs = new LinkedList<Buff>();

    /**
     * Konkstruktor, inicializálja az attribútumokat és létrehoz egy kígyóvonalat, annyi kígyóelemmel,
     * amennyi a kígyó hossza.
     * @param x A kígyó fejének a középpontjának az x koordinátája.
     * @param y A kígyó fejének a középpontjának az y koordinátája.
     * @param size A kígyó elemeinek az átmérője.
     * @param velocity A kígyó sebessége.
     * @param length A kígyó kígyóelemeinek a száma.
     * @param offset Két egymás utáni kígyóelem középpontjának távolsága.
     */
    public Snake(int x, int y, int size, double velocity , int length, int offset) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.offset = offset;
        this.angle = 0;
        this.length = length;

        SnakeLine firstLine = new SnakeLine(velocity, angle, offset,  null);
        lines.add(firstLine);
        firstElement = new SnakeElement(x, y, size);
        firstLine.addSnakeElement(firstElement);
        for (int i = 0; i < length -1 ; i++){
            firstLine.addSnakeElement(new SnakeElement(x-(i+1)*offset, y, size));
        }

        setVelocity(velocity);
    }

    /**
     * Setter a level attribútumhoz.
     * Beállítja a pálya értékét a paraméterként átadottra.
     * @param level A pálya, amelyen a kígyó tartózkodni fog.
     */
    public void setLevel(Level level){
        this.level = level;
    }

    /**
     * Setter az angle attribútumhoz.
     * Ha a szög 2*PI-nél nagyobb lenne, vagy 0-nál kisebb,
     * akkor [0; 2*PI] tartományba csúsztatja.
     * @param angle Az új szög.
     */
    public void setAngle(double angle) {
        while (angle > 2*Math.PI){
            angle -= 2*Math.PI;
        }
        while (angle < 0){
            angle += 2*Math.PI;
        }
        this.angle = angle;
    }
    /**
     * Setter a velocity attribútumhoz, de a forgás szögegységét is beállítja.
     * Úgy állítja be a sebessget, hogy a paraméterként átadottnál  nagyobb, de a lehető legkisebb olyan sebesség legyen,
     * amely teljesíti azt, hogy a sebesség osztja az offsetet.
     * @param velocity Az új sebesség.
     */
    public void setVelocity(double velocity){
        if (offset % velocity != 0){
            this.velocity = offset/(Math.floor(offset/velocity));
        }else{
            this.velocity = velocity;
        }
        if (this.velocity >= offset){
            this.velocity = offset;
        }
        lines.forEach((l) -> l.setVelocity(this.velocity));
        angleIncrement = (Math.PI/128)*this.velocity;
    }

    /**
     * Getter a velocity attribútumhoz.
     * @return A kígyó sebessége.
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Getter a length attribútumhoz.
     * @return A kígyó hossza, azaz, hogy hány elemből áll.
     */
    public int getLength() {
        return length;
    }

    /**
     * A kígyó lépése. Lépteti a kígyóvonalakat hátulról előre.
     * Kitörli az üres kígyóvonalakat a végéről. Forgatja a kígyót, ha kell.
     * Lépteti a buffokat, kitörli azokat, amelyek  már nem élnek.
     * Végrehajt egy élet regenerációt. Szól a pályának, hogy ellenőriztesse, hogy ütközik-e valamivel.
     */
    public void step(){
        ListIterator<SnakeLine> linesIter = lines.listIterator(lines.size());
        while (linesIter.hasPrevious()){
            linesIter.previous().step();
        }
        while(lines.getLast().isEmpty()){
            lines.removeLast();
        }
        if (leftRotate) {
            rotate(angle + angleIncrement);
        } else if(rightRotate){
            rotate(angle - angleIncrement);
        }
        buffs.forEach(Buff::step);
        buffs.forEach(b -> {
            if (!b.isAlive()){
                setVelocity(velocity/b.modifyValue("velocity", 1));
            }
        });
        buffs.removeIf(b -> !b.isAlive());
        heal();
        if (level != null){
            level.checkCollideWithSnake();
        }
    }

    /**
     * Gyógyítja a kígyót. Ha az élet kevesebb, mint 100, akkor az életregeneráció értékével növeli az életet,
     * figyelembe véve az esetleges buffok hatását erre. Ha 100, akkor nem csinál semmit.
     */
    private void heal(){
        if (health < 100){
            double h = healthRegen;
            for (Buff b : buffs){
                h = b.modifyValue("healthRegen", h);
            }
            health += healthRegen;
        }
        if (health > 100){
            health = 100;
        }
    }

    /**
     * Fordítja a kígyót, a paraméterként átadott szög lesz a kígyó fejének az új szöge.
     * Hozzáad egy új kígyóvonalat a kígyóvonalakhoz, és átadatja neki az előzőtől a legelső elemét.
     * @param newAngle Az új szög, amelyet a vízszintessen be fog zárni a kígyó feje.
     */
    public void rotate(double newAngle) {
        setAngle(newAngle);
        SnakeLine newLine = new SnakeLine(velocity, angle, offset, null);
        lines.getFirst().setNext(newLine);
        lines.getFirst().passElement();
        lines.addFirst(newLine);
    }

    /**
     * A paraméterként átadott értékkel csökkenti a kígyó hosszát.
     * Az utolsó kígyóvonalból törli az elemeket. Ha az utolsóban nincs elég elem, amennyit törölni kell,
     * akkor az egyel utána következőből töröl és így tovább.
     * @param damage Amennyit sebződött a kígyó, ahány elemet törölni kell.
     */
    public void damage(int damage){
        length -= damage;
        if (length <= 0 && level != null){
            level.snakeDied();
        }
        int deleteCount = 0;
        ListIterator<SnakeLine> linesIter = lines.listIterator(lines.size());
        while (linesIter.hasPrevious() && deleteCount < damage){
            deleteCount += linesIter.previous().deleteFromEnd(damage - deleteCount);
            System.out.println(deleteCount + " " + damage + " " + (damage - deleteCount));
        }
    }

    /**
     * A paraméterként átadott értékkel növeli a kígyó pontszámát, figyelembe véve az esetleges buffokat.
     * @param points A pontszám amennyit a kígyó mindenféle szorzók nélkül kap. Csak pozitív lehet.
     */
    public void givePoints(int points){
        if (points <= 0)
            return;
        double p = points;
        for (Buff b : buffs){
            p = b.modifyValue("points", p);
        }
        this.points +=  (int) p;
    }

    /**
     * A kígyó hosszát növeli, az esetleges buffokat figyelembe véve.
     * A legutolsó kígyóvonalhoz teszi az új elemeket.
     * @param length
     */
    public void giveLength(int length){
        double l = length;
        for (Buff b : buffs){
            l = b.modifyValue("length", l);
        }
        this.length += (int) l;
        lines.getLast().makeBigger((int) l);
    }

    /**
     * Csökkenti a kígyó életét a paraméterként átadott értékkel, amelyeket az esetleges buffokkal módosít.
     * Ha a kígyó élete 0 alá csökkent, akkor szól a pályának, hogy a kígyó meghalt.
     * @param healthDamage A sebzés, amelyet a kígyó az életerejéből elszenved.
     */
    public void damageHealth(double healthDamage){
        for (Buff b : buffs){
            healthDamage = b.modifyValue("damage", healthDamage);
        }
        this.health -= healthDamage;
        if (health <= 0 && level != null){
            level.snakeDied();
        }
    }

    /**
     * Getter a szög attribútumhoz.
     * @return A szög, amelyet a vízszintessel bezár a kígyó feje.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Getter a kígyó health attribútumához.
     * @return A kígyó élete.
     */
    public double getHealth() {
        return health;
    }

    /**
     * Hozzáad egy buffot a kígyó buffjaihoz és szól neki, hogy elkezdődött a buff hatása.
     * @param buff Buff, amelyet a kígyó kap.
     */
    public void addBuff(Buff buff){
        buffs.add(buff);
        buffs.forEach(b -> setVelocity(b.modifyValue("velocity", velocity)));
    }

    /**
     * Visszaadja a kígyón éppen található buffok listáját.
     * @return A kígyón található buffok listája.
     */
    public LinkedList<Buff> getBuffs() {
        return buffs;
    }

    /**
     * Beállítja a balra fordulás értékét a paraméterként átadott értékre.
     * @param leftRotate IGAZ, ha a kígyó forduljon balra, HAMIS, ha ne.
     */
    public synchronized void setLeftRotate(boolean leftRotate) {
        this.leftRotate = leftRotate;
    }

    /**
     * Beállítja a jobbra fordulás értékét a paraméterként átadott értékre.
     * @param rightRotate IGAZ, ha a kígyó forduljon jobbra, HAMIS, ha ne.
     */
    public synchronized void setRightRotate(boolean rightRotate) {
        this.rightRotate = rightRotate;
    }

    /**
     * Getter a kígyó fejének a középpontjának az x koordinátájához.
     * @return A kígyó első elemének (fejének) a középpontjának az x koordinátája.
     */
    public int getX(){
        return firstElement.getX();
    }

    /**
     Getter a kígyó fejének a középpontjának az y koordinátájához.
     * @return A kígyó első elemének (fejének) a középpontjának az y koordinátája.
     */
    public int getY(){
        return firstElement.getY();
    }

    /**
     * A kígyó mérete, a kígyót reprezentáló körök átmérője.
     * @return A kígyó köreinek átmérője.
     */
    public int getSize(){
        return size;
    }

    /**
     * Visszaadja a kígyót alkotó kígyóvonalak listáját.
     * @return A kígyóvonalak listája.
     */
    public LinkedList<SnakeLine> getLines() {
        return lines;
    }

    /**
     * Getter a kígyó pontszámához.
     * @return A kígyó aktuális pontszáma.
     */
    public int getPoints() {
        return points;
    }
}