package snake.model;

public class GoldenApple extends Entity{

    /**
     * A buff ami az aranyalmában van.
     */
    private Buff buff;

    /**
     * Konkstruktor, inicializálja az attribútumokat.
     * @param x Az aranyalma középpontjának x koordinátája.
     * @param y Az aranyalma középpontjának y koordinátája.
     * @param size Az aranyalma átmérője.
     * @param level A pálya, amelyre az aranyalma kerül.
     * @param buff A buff, amely az aranyalmába kerül.
     */
    public GoldenApple(int x, int y, int size, Level level, Buff buff) {
        super(x, y, size, level);
        this.buff = buff;
    }

    /**
     * Ha a kígyóvel ütközik akkor odaadja neki az almában található buffot.
     * @param snake A kígyó, amivel ütközik az aranyalma.
     */
    @Override
    public void collideWithSnake(Snake snake) {
        if (doesCollide(snake)){
            snake.addBuff(buff);
            removeFromLevel();
        }
    }

    /**
     * Kiszámolja, hogy az aranyalma és a kígyó első eleme érintkezik-e.
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
