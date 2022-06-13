package snake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GoldenAppleTest {
	
	/**
	 * GoldenApple objektum, amelynek segítségével tesztel.
	 */
	private GoldenApple goldenApple;
	
	/**
	 * A kígyó, amelyik ütközik az almával.
	 */
	private Snake snake;
	
	/**
	 * Buff, ami az almában van.
	 */
	private Buff buff;
	
	/**
	 * Konstruktor, a paraméteres teszteléshez inicializálja a megfelelő attribútumokat.
	 * @param goldenApple Aranyalma, amelynek segítségével tesztel.
	 * @param buff Buff, ami az almában van.
	 */
	public GoldenAppleTest(GoldenApple goldenApple, Buff buff) {
		this.goldenApple = goldenApple;
		this.buff = buff;
	}
	
	/**
	 * Létrehoz egy kígyót.
	 */
	@Before
	public void setUp() {
		snake = new Snake(100, 200, 30, 2.0, 10, 20);
	}
	
	/**
	 * Ha ütköznek, akkor odaadja a buffot a kígyónak.
	 * Ha nem, akkor nem csinál semmit, ekkor nem változhat a kígyó.
	 */
	@Test
	public void testGoldenAppleCollideWithSnake() {
		LinkedList<Buff> snakeBuffs = snake.getBuffs();
		int size = snakeBuffs.size();
		
		boolean collide = (Math.sqrt((goldenApple.getX()-snake.getX())*(goldenApple.getX()-snake.getX())
				+(goldenApple.getY()-snake.getY())*(goldenApple.getY()-snake.getY())) < goldenApple.getSize()/2 + snake.getSize()/2);
		
		goldenApple.collideWithSnake(snake);
		if(collide) {
			Assert.assertEquals(size+1, snake.getBuffs().size());
			Assert.assertSame(buff, snakeBuffs.getLast());
		}else {
			Assert.assertEquals(size, snake.getBuffs().size());
		}
	}
	
	/**
	 * A paraméteres teszteléshez készíti el a paramétereket.
	 * @return a paraméterek listája.
	 */
	@Parameters
	public static List<Object[]> parameters(){
		List<Object[]> params = new ArrayList<Object[]>();
		Buff buff = new Buff(100);
		//Nem ütközik
		params.add(new Object[] {new GoldenApple(300, 10, 40, null, buff), buff});
		//Nem ütközik
		params.add(new Object[] {new GoldenApple(140, 200, 40, null, buff), buff});
		//Érintkezik
		params.add(new Object[] {new GoldenApple(140, 200, 50, null, buff), buff});
		//Ütközik
		params.add(new Object[] {new GoldenApple(110, 205, 15, null, buff), buff});
		//Ütközik
		params.add(new Object[] {new GoldenApple(95, 194, 20, null, buff), buff});
		return params;
	}
}
