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
public class SnakeLineTest {
	
	/**
	 * Egy SnakeLine objektum a teszteléshez.
	 */
	private SnakeLine snakeLine;
	
	/**
	 * Egy SnakeElement objektum a teszteléshez, ez benne van a SnakeLine-ban.
	 */
	private SnakeElement snakeElement;
	
	/**
	 * Ennyi elemmel növeli meg a snakeLine-t.
	 */
	private int n;
	
	/**
	 * A snakeLine "velocity" attribútuma.
	 */
	private double velocity;
	
	/**
	 * A snakeLine "angle" attribútuma.
	 */
	private double angle; 
	
	/**
	 * A snakeLine "offset" attribútuma.
	 */
	private int offset;
	
	
	/**
	 * Konkstruktor a paraméteres teszteléshez.
	 * Inicializálja az attribútumokat.
	 * @param snakeElement Egy SnakeElement, amely a snakeLine-ba kerül.
	 * @param velocity A snakeLine sebessége.
	 * @param angle A snakeLine vízszintessel bezárt szöge
	 * @param offset Két egymás utáni SnakeElement közötti távolság.
	 * @param n Ennyi elemet ad hozzá a snakeLine-hoz.
	 */
	public SnakeLineTest(SnakeElement snakeElement, double velocity, double angle, int offset, int n) {
		this.snakeElement = snakeElement;
		this.n = n;
		this.angle = angle;
		this.offset = offset;
		this.velocity = velocity;
	}
	
	/**
	 * Létrehoz egy SnakeLine-t és beleteszi a paraméteres snakeElementet.
	 */
	@Before
	public void setUp() {
		snakeLine = new SnakeLine(velocity, angle, offset, null);
		snakeLine.addSnakeElement(snakeElement);
	}
	
	/**
	 * Teszteli a makeBigger metódust.
	 * Megnézi, hogy annyi SnakeElement került-e bele, amennyi kell és ezután leellenőrzi a SnakeElementek koordinátáit is.
	 */
	@Test
	public void testMakeBigger() {
		snakeLine.makeBigger(n);
		Assert.assertEquals(n + 1, snakeLine.getElements().size());
		LinkedList<SnakeElement> elements = snakeLine.getElements();
		SnakeElement previous = elements.getFirst();
		SnakeElement temp = new SnakeElement();
		for (int i = 0; i < n; i++) {
			SnakeElement se = elements.get(1+i);
			temp.calculatePositionBehindElement(previous, angle, offset);
			Assert.assertEquals(temp.getXDouble(), se.getXDouble(), 0.01);
			Assert.assertEquals(temp.getYDouble(), se.getYDouble(), 0.01);
			previous = se;
		}
	}
	
	/**
	 * A deleteFromEnd metódust teszteli.
	 * Megnézi, hogy annyi elemet törölt-e amennyit paraméterként kapott vagy az összesset, ha többet kellett volna törölnie,
	 * mint amennyi volt.
	 */
	@Test
	public void testDeleteFromEnd() {
		for(int i = 0; i < n; i++) {
			snakeLine.addSnakeElement(new SnakeElement());
		}
		int size = snakeLine.getElements().size();
		int delete = 25;
		int countdeleted = snakeLine.deleteFromEnd(delete);
		if (delete < size) {
			Assert.assertEquals(delete, countdeleted);
			Assert.assertEquals(size-delete, snakeLine.getElements().size());
		}else {
			Assert.assertEquals(0, snakeLine.getElements().size());
			Assert.assertTrue(snakeLine.isEmpty());
		}
		
	}
	
	/**
	 * Teszteli a passElement metódust.
	 * Létrehoz egy új SnakeLine-t és annak átadatja az első elemet a snakeLine-nal.
	 * Ezután leellenőrzi, hogy ez megtörtént-e.
	 */
	@Test
	public void testPassElement() {
		SnakeLine sl = new SnakeLine(velocity, 2.0, offset, null);
		SnakeElement firstElement = snakeLine.getElements().getFirst();
		snakeLine.setNext(sl);
		snakeLine.passElement();
		Assert.assertSame(firstElement, sl.getElements().getLast());
	}

	
	/**
	 * A paraméteres teszteléshez készíti el a paramétereket.
	 * @return a paraméterek listája.
	 */
	@Parameters
	public static List<Object[]> parameters(){
		List<Object[]> params = new ArrayList<Object[]>();
		params.add(new Object[] {new SnakeElement(40, 70, 20), 2.5, 1.7, 20, 10});
		params.add(new Object[] {new SnakeElement(70, 120, 30), 0.4, 0, 10, 15});
		params.add(new Object[] {new SnakeElement(80, 90, 67), 1.8, Math.PI, 40, 70});
		params.add(new Object[] {new SnakeElement(20, 170, 50), 9.8, Math.PI/2, 25, 40});
		params.add(new Object[] {new SnakeElement(260, 120, 90), 17.445, -Math.PI/2, 50, 1});
		params.add(new Object[] {new SnakeElement(432, 53, 40), 10.432, -2*Math.PI, 20, 22});
		params.add(new Object[] {new SnakeElement(210, 476, 30), 15.93, 2.34, 31, 32});
		return params;
	}
}
