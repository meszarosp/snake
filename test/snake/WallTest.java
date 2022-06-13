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
public class WallTest {
	
	/**
	 * Kígyó, ami ütközik a fallal.
	 */
	private Snake snake;
	
	/**
	 * Egy wall objektum, aminek segítségével tesztel.
	 */
	private Wall wall;
	/**
	 * A fal "invincible" attribútuma.
	 */
	private boolean invincible;
	
	/**
	 * A tény, hogy a megadott paraméteres tesztesetekben ütközik-e a kígyó a fallal.
	 */
	private boolean collide;
	
	/**
	 * A fal "damage" attribútuma.
	 */
	private int damage;
	
	public WallTest(Wall wall, boolean invincible, int damage,boolean collide) {
		this.wall = wall;
		this.invincible = invincible;
		this.collide = collide;
		this.damage = damage;
	}
	
	
	@Before
	public void setUp() {
		snake = new Snake(100, 200, 40, 2.0, 10, 20);
	}
	
	/**
	 * Teszteli a collideWithSnake() metódust.
	 * Ha ütköznek, akkor a kígyó életet veszít, és ha nem törhető a fal, akkor a kígyó elfordult.
	 */
	@Test
	public void testWallCollideWithSnake() {
		double health = snake.getHealth();
		double angle = snake.getAngle();
		wall.collideWithSnake(snake);
		if (collide) {
			Assert.assertEquals(health-damage, snake.getHealth(), 0.01);
			if (invincible) {
				Assert.assertEquals(2, snake.getLines().size());
			}
		}
	}
	
	/**
	 * Paraméteres teszteléshez.
	 * Azt, hogy a snake ütközik-e a falakkal azt előre egy másik programban számoltattam ki.
	 * @return A paraméterek listája.
	 */
	
	@Parameters
	public static List<Object[]> parameters(){
		List<Object[]> params = new ArrayList<Object[]>();
		//Nem ütközik
		params.add(new Object[] {new Wall(133, 206, 132, 2, 1.06273, false, 20, null), false, 20, false});
		//Nem ütközik
		params.add(new Object[] {new Wall(113, 181, 47, 3, 2.006255975, true, 50, null), true, 50, false});
		//Ütközik
		params.add(new Object[] {new Wall(112, 185, 76, 5, 1.040914366, false, 40, null), false, 40, true});
		//Ütközik
		params.add(new Object[] {new Wall(118, 209, 28, 2, 0.496371639, true, 10, null), true, 10, true});
		return params;
	}
}
