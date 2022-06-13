package snake.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Buff implements Steppable, Serializable {
    /**
     * A buff élettartama, még ennyi step-ig él. Minden step-be csökken egyel.
     */
    private int timeToLive;
    /**
     * A módosítokat tartalmazza, a kulcs az attribútum neve, az érték a szorzó, amivel módosítja.
     */
    private HashMap<String, Double> modifiers = new HashMap<String, Double>();

    /**
     * Konstruktor, az élettartamot állítja be.
     * @param timeToLive A buff kezdő élettartama.
     */
    public Buff(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * Azt mondja meg, hogy él-e még a buff, lejárt-e már az ideje.
     * @return IGAZ, ha a buff még él, HAMIS, ha nem.
     */
    public boolean isAlive(){
        return timeToLive > 0;
    }

    /**
     * Egy szorzót tesz be a szorzók közé
     * @param name A modósítani kívánt attribútum neve.
     * @param modifier A szorzó, amivel módosítja.
     */
    public void addModifier(String name, double modifier){
        modifiers.put(name, modifier);
    }

    /**
     * Megmondja, hogy a paraméterként átadott attribútum értékét milyen értékre módosítja.
     * @param name A módosítani kívánt attribútum neve.
     * @param value A kezdő érték, amit módosít.
     * @return Az átadott érték, ha a buffnak nincsen a kívánt attribútumra szorzója, különben a módosított érték.
     */
    public double modifyValue(String name, double value){
        return value*modifiers.getOrDefault(name, 1.0);
    }

    /**
     * Egy lépés, csökkenti az élettartamot egyel.
     */
    @Override
    public void step() {
        timeToLive--;
    }

    /**
     * Sztringgé konvertáló függvény.
     * @return A buff módosítói (attribútumnév szorzóx hátralévőidő) formátumban külön sorokban.
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");
        modifiers.forEach( (name, value) -> sb.append(name).append(" ").append(df.format(value)).append("x ").append(timeToLive/Game.FPS).append("\n"));
        return sb.toString();
    }
}
