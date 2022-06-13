package snake.model;

import snake.view.FruitDrawable;
import snake.view.GoldenAppleDrawable;
import snake.view.SnakeDrawable;
import snake.view.WallDrawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Level implements Serializable, Steppable {
    /**
     * Entitások, amelyek a pályán vannak.
     */
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    /**
     * Entitások, amelyek a következő lépésben törlésre kerülnek az entitások listájából.
     */
    private LinkedList<Entity> removable = new LinkedList<Entity>();
    /**
     * Entitások, amelyek a következő lépésben a pályára kerülnek.
     */
    private LinkedList<Entity> newEntities = new LinkedList<Entity>();
    /**
     * Kígyó, amelyik a pályához tartozik.
     */
    private Snake snake;
    /**
     * A randomgeneráláshoz használt Random objektum.
     */
    private Random random = new Random();
    /**
     * A másodpercek számolásához használt counter.
     */
    private int counter;

    /**
     * A pálya szélessége.
     */
    private int WIDTH;
    /**
     * A pálya magassága.
     */
    private int HEIGHT;
    /**
     * A lépések száma másodpercenként.
     */
    private int STEPPS;
    /**
     * A maximális hossz, amelyet ha a kígyó elér, akkor nyer a játékos.
     */
    private int maxlength = 100;
    /**
     * A Game objektum, amelyikhez tartozik a pálya.
     */
    private Game game;


    /**
     * Konstruktor, inicializálja az attribútumokat.
     * Létrehozza a pálya szélén lévő falakat.
     * Elhelyez az a pályára egy gyümölcsöt és egy aranyalmát.
     * @param game Az a játék, amelyikhez  pálya tartozik.
     * @param width A játékmező szélessége.
     * @param height A játékmező magassága.
     * @param stepps A másodpercenkénti lépésszám.
     */
    public Level(Game game, int width, int height, int stepps){
        this.game = game;
        WIDTH = width;
        HEIGHT = height;
        STEPPS = stepps;

        snake = new Snake(120, 40, 30, 2, 10, 12);
        snake.setLevel(this);
        SnakeDrawable sd = new SnakeDrawable(snake);
        game.addDrawable(sd);
        makeBorderWalls();

        makeFruit();
        makeGoldenApple();
        restartCounter();
    }

    /**
     * Szól a játéknak, hogy a játékos vesztett, véget ért a játék.
     */
    public void snakeDied(){
        game.gameEnded(false);
    }

    /**
     * Újraindítja a számlálót a másodpercenkénti lépésszámra.
     */
    private void restartCounter(){
        counter = STEPPS;
    }

    /**
     * Elkészíti a pálya szélén elhelyezkedő nem törhető falakat.
     * Elkészíti hozzájuk a kirajzolójukat és odaadja a game-nek.
     */
    private void makeBorderWalls(){
        Wall w = new Wall(WIDTH/2, 5, WIDTH-20, 10,0, true,0, this);
        w.setDrawable(new WallDrawable(w));
        addEntity(w);
        game.addDrawable(w.getDrawable());
        w = new Wall(WIDTH/2, HEIGHT-5, WIDTH-20, 10,0, true,0, this);
        w.setDrawable(new WallDrawable(w));
        addEntity(w);
        game.addDrawable(w.getDrawable());
        w = new Wall(5, HEIGHT/2, HEIGHT, 10,Math.PI/2, true,0, this);
        w.setDrawable(new WallDrawable(w));
        addEntity(w);
        game.addDrawable(w.getDrawable());
        w = new Wall(WIDTH-5, HEIGHT/2, HEIGHT, 10,Math.PI/2, true,0, this);
        w.setDrawable(new WallDrawable(w));
        addEntity(w);
        game.addDrawable(w.getDrawable());
    }

    /**
     * Elhelyez egy új gyümölcsöt a pályán.
     * Elkészíti hozzá a kirajzolót és odaadja a game-nek.
     */
    public void makeFruit(){
        int x = random.nextInt(WIDTH-40+1) + 20;
        int y = random.nextInt(HEIGHT-40+1) + 20;
        int value = random.nextInt(3)+1;
        boolean rotten = random.nextInt(20) == 0;
        Fruit fruit = new Fruit(x, y, 15, this, value, rotten);
        fruit.setDrawable(new FruitDrawable(fruit));
        addEntity(fruit);
        game.addDrawable(fruit.getDrawable());
    }

    /**
     * Akkor hívódik meg, amikor egy gyümölcsöt megevett a kígyó.
     * Leveszi a gyümölcsöt a pályáról és csináltat egy újat.
     * @param fruit A gyümölcs, amit le kell venni a pályáról.
     */
    public void removeFruit(Fruit fruit){
        removeEntity(fruit);
        makeFruit();
    }

    /**
     * Elhelyez a pályán egy véletlenszerűen generált aranyalmát egy véletlenszerű buffal.
     */
    public void makeGoldenApple(){
        int ttl = (random.nextInt(20)+10)*STEPPS;
        Buff buff = new Buff(ttl);
        int x = random.nextInt(WIDTH-40+1) + 20;
        int y = random.nextInt(HEIGHT-40+1) + 20;

        int attributeID = random.nextInt(Snake.attributes.length);
        String attributeName = Snake.attributes[attributeID];
        double modifier;
        if ("damage".equals(attributeName)){
            modifier = random.nextDouble()*0.5+0.5;
        }else if("velocity".equals(attributeName)) {
            modifier = 1.5+random.nextInt(2)*0.5;
        }else if("length".equals(attributeName)){
            modifier = random.nextInt(2)+2;
        }else if("healthRegen".equals(attributeName)){
            modifier = random.nextDouble()+2;
        }else{
            modifier = (random.nextDouble()+1)*1.5;
        }
        buff.addModifier(attributeName, modifier);

        GoldenApple ga = new GoldenApple(x, y, 15, this, buff);
        ga.setDrawable(new GoldenAppleDrawable(ga));
        addEntity(ga);
        game.addDrawable(ga.getDrawable());
    }

    /**
     * Elhelyez egy falat a pályán úgy, hogy a kígyó aktuális irányára merőleges legyen és a kígyó előtt legyen.
     * A hossza és egyéb paraméterei véletlenszerűek.
     */
    public void makeWall(){
        double distance = snake.getVelocity()*STEPPS*1;
        int wallX = (int) (snake.getX()+distance*Math.cos(snake.getAngle()));
        int wallY = (int) (snake.getY()-distance*Math.sin(snake.getAngle()));
        boolean invincible = random.nextInt(5) == 0;
        int width = (int) (snake.getSize()*(random.nextDouble()+2));
        double damage = invincible ? 0 : (double)width/snake.getSize()*10;
        Wall wall = new Wall(wallX, wallY, width, 6, -snake.getAngle()+Math.PI/2, invincible, damage,this);
        wall.setDrawable(new WallDrawable(wall));
        addEntity(wall);
        game.addDrawable(wall.getDrawable());
    }

    /**
     * Getter a snake attríbútumhoz.
     * @return A kígyó, amelyik a pályán tartózkodik.
     */
    public Snake getSnake(){
        return snake;
    }


    /**
     * Hozzáadja a paraméterként átadott entitást az új entitások listájához.
     * @param e Az új entitás.
     */
    public void addEntity(Entity e){
        newEntities.add(e);
    }

    /**
     * Hozzáadja a paraméterként átadott entitást a törlendő entitások listájához.
     * @param e A törlendő entitás.
     */
    public void removeEntity(Entity e){
        removable.add(e);
    }


    /**
     * Lépteti a kígyót. Törli a törlendő entitásokat és hozzáadja az új entitásokat az entitások listájához.
     * Csökkenti a másodpercszámláló értékét. Másodpercenként eldönti, hogy generál-e véletlenszerűen falat
     * vagy aranyalmát. Ezután újraindítja a számlálót. Ellenőrzi, hogy a kígyó elérte-e már a maximális hosszt.
     */
    @Override
    public void step(){
        snake.step();
        removable.forEach(e -> game.removeDrawable(e.getDrawable()));
        entities.removeAll(removable);
        removable.clear();
        entities.addAll(newEntities);
        newEntities.clear();

        counter--;
        if(counter <= 0){
            int r = random.nextInt(20);
            if (r % 10 == 0){
                makeWall();
            }else if (r == 1){
                makeGoldenApple();
            }
            restartCounter();
        }

        if(snake.getLength() >= maxlength){
            game.gameEnded(true);
        }
    }

    /**
     * Ellenőrizteti a pályán lévő entitásokkal, hogy ütköznek-e a kígyóval és ha igen akkor végrehajtják az ütközéskor
     * teendőket.
     */
    public void checkCollideWithSnake(){
        entities.forEach((e) -> e.collideWithSnake(snake));
    }

}
