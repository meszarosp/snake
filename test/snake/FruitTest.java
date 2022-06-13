package snake;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FruitTest {
	
	/**
	 * Fruit objektum, aminek segítségével tesztel.
	 */
	private Fruit fruit;
	/**
	 * A kígyó amelyik ütközik a gyümölccsel.
	 */
	private Snake snake;
	
	/**
	 * A fruit "rotten" attribútuma.
	 */
	private boolean rotten;
	
	/**
	 * A fruit "value" attribútuma.
	 */
	private int value;
	
	/**
	 * Konkstruktor a maraméteres teszteléshez.
	 * @param fruit Egy gyümölcs.
	 * @param value A gyümölcs értéke.
	 * @param rotten IGAZ, ha romlott a gyümölcs, HAMIS, ha nem.
	 */
	public FruitTest(Fruit fruit, int value,  boolean rotten) {
		this.fruit = fruit;
		this.rotten = rotten;
		this.value = value;
	}
	
	/**
	 * Készit egy kígyót.
	 */

	@Before
	public void setUp() {
		snake = new Snake(50, 50, 40, 2.0, 10, 20);
	}
	
	/**
	 * Teszteli a collideWithSnake metódust.
	 * Ha nem ütköznek, akkor nem kell változnia a kígyónak.
	 * Ha ütköznek és nem romlott a gyümölcs, akkor pontot kap a kígyó, megnő a hossza és az élete nem változik.
	 * Ha ütköznek és romlott a gyümölcs, akkor életet veszít a kígyó, csökken a hossza és a pontszáma nem változik.
	 */
	@Test
	public void testFruitCollideWithSnake() {
		double health = snake.getHealth();
		int points = snake.getPoints();
		int length = snake.getLength();
		
		boolean collide = (Math.sqrt((fruit.getX()-snake.getX())*(fruit.getX()-snake.getX())
							+(fruit.getY()-snake.getY())*(fruit.getY()-snake.getY())) < fruit.getSize()/2 + snake.getSize()/2);
		
		fruit.collideWithSnake(snake);
		if (collide && rotten) {
			Assert.assertEquals(health-value, snake.getHealth(), 0.01);
			Assert.assertEquals(length-1, snake.getLength());
			Assert.assertEquals(points, snake.getPoints());
		}else if(collide && !rotten){
			Assert.assertEquals(health, snake.getHealth(), 0.01);
			Assert.assertEquals(length+1, snake.getLength());
			Assert.assertEquals(points+value, snake.getPoints());
		}else {
			Assert.assertEquals(health, snake.getHealth(), 0.01);
			Assert.assertEquals(length, snake.getLength());
			Assert.assertEquals(points, snake.getPoints());
		}
	}
	
	/**
	 * A paraméteres teszteléshez készíti el a paramétereket.
	 * @return a paraméterek listája.
	 */
	@Parameters
	public static List<Object[]> parameters(){
		List<Object[]> params = new ArrayList<Object[]>();
		//Nem ütköznek, nem romlott
		params.add(new Object[] {new Fruit(200, 200, 50, null, 10, false), 10, false});
		//Nem ütköznek, romlott
		params.add(new Object[] {new Fruit(300, 500, 20, null, 20, true), 20, true});
		//Érintkeznek (nem ütköznek), nem romlott
		params.add(new Object[] {new Fruit(100, 50, 60, null, 20, false), 20, false});
		//Érintkeznek (nem ütköznek), romlott
		params.add(new Object[] {new Fruit(50, 85, 30, null, 40, true), 40, true});
		//Ütköznek, nem romlott
		params.add(new Object[] {new Fruit(55, 65, 40, null, 10, false), 10, false});
		//Ütköznek, romlott
		params.add(new Object[] {new Fruit(55, 65, 40, null, 20, true), 20, true});
		return params;
	}

}
