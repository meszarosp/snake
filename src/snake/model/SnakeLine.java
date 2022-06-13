package snake.model;

import java.io.Serializable;
import java.util.LinkedList;

public class SnakeLine implements Serializable, Steppable {
    /**
     * Sebesség, amellyel a kígyóelemek haladnak pixel/sebesség mértékegységben.
     */
    private double velocity;

    /**
     * Az irány, amelybe a kígyóelemek haladnak.
     * Annak az egynesnek a vízszintessel bezárt szöge. amelybe kígyóelemek haladnak.
     * 0 és 2*PI közötti érték
     */
    private double angle;

    /**
     * Két egymás utáni kígyóelem középpontja közötti távolság.
     */
    private int offset;

    /**
     * Ezen kígyóvonal után következő kígyóvonal, null, ha ez a legelső kígyóvonal.
     */
    private SnakeLine next;

    /**
     * Annak a pontnak az x koordinátája,
     * amely pontot ha elér egy kígyóelem, akkor a következő kígyóvonalnak kell továbbadnia.
     */
    private double pivotX;

    /**
     * Annak a pontnak az y koordinátája,
     * amely pontot ha elér egy kígyóelem, akkor a következő kígyóvonalnak kell továbbadnia.
     */
    private double pivotY;

    /**
     * A kígyóvonalban található kígyóelemek listája.
     * A listában a legelső elem az amelyik legelől van.
     * A legutolsó elem az amelyik a legvégén van.
     */
    private LinkedList<SnakeElement> elements = new LinkedList<SnakeElement>();

    /**
     * Konstruktor, inicializálja az attribútumokat.
     * @param velocity Sebesség amellyel haladnak a vonalban található elemek.
     * @param angle Vízszintessel bezárt szög, amely irányba haladnak az elemek.
     * @param offset Két egymás utáni elem közötti távolság.
     * @param next A kígyóvonal után következő kígyóvonal, null, ha ez a legelső.
     */
    public SnakeLine(double velocity, double angle, int offset, SnakeLine next) {
        this.velocity = velocity;
        this.angle = angle;
        this.offset = offset;
        this.next = next;
    }

    /**
     * Visszaadja a kígyóvonalban található kígyóelemek listája.
     * @return A kígyóelemek listája.
     */
    public LinkedList<SnakeElement> getElements() {
        return elements;
    }

    /**
     * Hozzáad egy kígyóelemet a kígyóelemek listájának végére.
     * @param e A hozzáadni kívánt kígyóelem, nem lehet null.
     */
    public void addSnakeElement(SnakeElement e){
        elements.addLast(e);
    }

    /**
     * Beállítja az ezután következő kígyóvonalat.
     * @param next A következő kígyóvonal, lehet null is, ha ez a kígyóelem lesz a legelső.
     */
    public void setNext(SnakeLine next) {
        this.next = next;
    }

    /**
     * Végrehajt egy lépést a kígyóvonalon.
     * Ha üres, akkor nem csinál semmit.
     * Ha em üres, akkor megnézi, hogy az első elem elérte-e a fordulópontot, ha igen, akkor továbbadja a következő
     * kígyóvonalnak. Ezután minden kígyóelemnek szól, hogy kiszámolja az új pozícióját.
     */
    public void step(){
        if (isEmpty())
            return;

        SnakeElement first = elements.getFirst();
        if (next != null && compareToPivot(first)){
            passElement();
        }

        for (SnakeElement e : elements){
            e.newPosition(velocity, angle);
        }
    }

    /**
     * Ha üres akkor nem csinál semmit.
     * Ha nem üres, akkor átadja az első elemét a következő kígyóvonalnak (ha az nem null) és beállítja a fordulópontot
     * arra a pontra, ami az első elemének a középpontja volt és törli az első elemet a kígyóelemek listájából.
     */
    public void passElement(){
        if (!isEmpty() && next != null){
            SnakeElement first = elements.getFirst();
            next.addSnakeElement(first);
            pivotX = first.getXDouble();
            pivotY = first.getYDouble();
            elements.removeFirst();
        }
    }

    /**
     * Összehasonlítja, hogy egy kígyóelem a fordulópontnál van-e, eps tűréssel.
     * A tűrés az a sebesség fele.
     * @param e A kígyóelem, amelyet vizsgál.
     * @return IGAZ, ha az elem a fordulópont körül van a tűrésen belül, HAMIS, ha nem.
     */
    private boolean compareToPivot(SnakeElement e){
        double eps = velocity/2;
        double x = e.getXDouble();
        double y = e.getYDouble();
        return (pivotX - eps <= x && x <= pivotX + eps ) && (pivotY - eps <= y && y <= pivotY + eps);
    }

    /**
     * A végéről töröl legfeljebb annyi elemet, amennyi a paraméterként átadott számnak megfelel.
     * Ha ez a szám kevesebb, mint ahány elem a listában van, akkor annyit töröl, amennyi a szám.
     * Ha ez a szám nagyobb, akkor törli az egész listát.
     * @param count Ahány elemet törölni kell. (Nemnegatív szám.)
     * @return Azzal tér vissza ahány elemet törölt.
     */
    public int deleteFromEnd(int count){
        int i = 0;
        while(elements.size() > 0 && i < count){
            elements.removeLast();
            i++;
        }
        return i;
    }

    /**
     * Ha üres, akkor nem csinál semmit.
     * Hozzáad a paraméterként átadott számnak megfelelő darab kígyóelemet tesz a lista végére.
     * Az új elemeknek kiszámoltatja a pozícióját, hogy egymás után sorban következzenek a legvégére.
     * @param n Ahány elemet hozzá kell adni az elemek listájának végére,
     */
    public void makeBigger(int n){
        if (!isEmpty()) {
            SnakeElement last = elements.getLast();
            for (int i = 0; i < n; i++) {
                SnakeElement newElement = new SnakeElement();
                newElement.calculatePositionBehindElement(last, angle, offset);
                elements.add(newElement);
                last = newElement;
            }
        }
    }

    /**
     * Megmondja, hogy van-e benne kígyóelem.
     * @return IGAZ, ha a kígyóelemek listája üres, különben HAMIS.
     */
    public boolean isEmpty(){
        return elements.isEmpty();
    }

    /**
     * Setter a sebességhez.
     * @param velocity Az új sebesség.
     */
    public void setVelocity(double velocity){
        this.velocity=velocity;
    }
}
