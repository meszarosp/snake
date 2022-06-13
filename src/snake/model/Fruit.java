package snake.model;

public class Fruit extends Entity{
    /**
     * A gyümölcs értéke, pontértéke.
     */
    protected int value;

    /**
     * IGAZ, ha a gyümölcs romlott, HAMIS, ha nem romlott.
     */
    protected boolean rotten;

    /**
     * Konstruktor, beállítja az attribőtumok értékét.
     * @param x Az gyümölcs középpontjának x koordinátája.
     * @param y Az gyümölcs középpontjának y koordinátája.
     * @param size Az gyümölcs átmérője.
     * @param level A pálya, amelyen a gyümölcs van.
     * @param value A gyümölcs pontértéke.
     * @param rotten Romlott-e a gyümölcs.
     */
    public Fruit(int x, int y, int size, Level level, int value, boolean rotten) {
        super(x, y, size, level);
        this.value = value;
        this.rotten = rotten;
    }

    /**
     * Ha a kígyóval ütköznik és nem romlott, akkor a gyümölcs értékével növeli a kígyó pontszámát és egyel a hosszát.
     * Ha romlott, akkor a gyümölcs értékével sebzi a kígyót és egyel csökkenti a hosszát.
     * Ezután szól a pályának, hogy törölje.
     * @param snake A kígyó, amivel ütközik a gyümölcs.
     */
    @Override
    public void collideWithSnake(Snake snake) {
        if (doesCollide(snake)){
            if (rotten){
                snake.damageHealth(value);
                snake.damage(1);
            }else {
                snake.givePoints(value);
                snake.giveLength(1);
            }
            if (level != null){
                level.removeFruit(this);
            }
        }
    }

    /**
     * Kiszámolja, hogy a gyümölcs és a kígyó első eleme érintkezik-e.
     * Azt számolja ki, hogy a két kör metszi-e egymást.(Érintkezést nem tekinti metszésnek.)
     * @param snake Az a kígyó, amelyikkel ellenőrizni kell, hogy ütköznek-e.
     * @return IGAZ, ha a kígyó és a gyümölcs ütköznek, HAMIS, ha nem.
     */
    private boolean doesCollide(Snake snake){
        int snakeX = snake.getX();
        int snakeY = snake.getY();
        int snakeSize = snake.getSize();

        return (Math.sqrt((x-snakeX)*(x-snakeX)+(y-snakeY)*(y-snakeY)) < size/2 + snakeSize/2);
    }

}
