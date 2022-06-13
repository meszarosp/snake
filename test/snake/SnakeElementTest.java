package snake;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SnakeElementTest {
	/**
	 * Egy SnakeElement, aminek segítségével tesztel.
	 */
	private SnakeElement snakeElement;
	/**
	 * Sebesség.
	 */
	private double velocity;
	/**
	 * Vízszintessel bezárt szög.
	 */
	private double angle;
	
	/**
	 * Két egymás utáni SnakeElement közötti távolság.
	 */
	private int offset;
	
	
	/**
	 * Konstruktor a paraméteres teszteléshez.
	 * @param snakeElement Egy SnakeElement a teszteléshez.
	 * @param velocity Sebesség.
	 * @param angle Szög.
	 * @param offset Két egymás utáni SnakeElement távolsága.
	 */
	public SnakeElementTest(SnakeElement snakeElement, double velocity, double angle, int offset) {
		this.snakeElement = snakeElement;
		this.velocity = velocity;
		this.angle = angle;
		this.offset = offset;
	}
	
	/**
	 * A paraméterként megadott szög és sebesség segítségével kiszámolja az elvárt értékeket,
	 * majd meghívja a SnakeElement newPosition metódusát és összehasonlítja a kapott értékeket az elvártakkal.
	 */
	@Test
	public void testNewPosition() {
		double expectedX = snakeElement.getXDouble() + velocity*Math.cos(angle);
        double expectedY = snakeElement.getYDouble() - velocity*Math.sin(angle);
        snakeElement.newPosition(velocity, angle);
        Assert.assertEquals(expectedX, snakeElement.getXDouble(), 0.01);
        Assert.assertEquals(expectedY, snakeElement.getYDouble(), 0.01);
	}
	
	/**
	 * Létrehoz egy új SnakeElement objektumot.
	 * Ennek segítségével teszteli a calculatePositionBehindElement metódust.
	 */
	@Test
	public void testCalculatePositionBehindElement() {
		SnakeElement se = new SnakeElement(0, 0, 0);
		double expectedX = snakeElement.getXDouble() - Math.cos(angle)*offset;
        double expectedY = snakeElement.getYDouble() + Math.sin(angle)*offset;
        int expectedSize = snakeElement.getSize();
        se.calculatePositionBehindElement(snakeElement, angle, offset);
        Assert.assertEquals(expectedX, se.getXDouble(), 0.01);
        Assert.assertEquals(expectedY, se.getYDouble(), 0.01);
        Assert.assertEquals(expectedSize, se.getSize());
	}
	
	
	/**
	 * A paraméteres teszteléshez készíti el a paramétereket.
	 * @return a paraméterek listája.
	 */
	@Parameters
	public static List<Object[]> parameters(){
		List<Object[]> params = new ArrayList<Object[]>();
		params.add(new Object[] {new SnakeElement(40, 70, 20), 2.5, 1.7, 20});
		params.add(new Object[] {new SnakeElement(70, 120, 30), 0.4, 0, 10});
		params.add(new Object[] {new SnakeElement(80, 90, 67), 1.8, Math.PI, 40});
		params.add(new Object[] {new SnakeElement(20, 170, 50), 9.8, Math.PI/2, 25});
		params.add(new Object[] {new SnakeElement(260, 120, 90), 17.445, -Math.PI/2, 50});
		params.add(new Object[] {new SnakeElement(432, 53, 40), 10.432, -2*Math.PI, 20});
		params.add(new Object[] {new SnakeElement(210, 476, 30), 15.93, 2.34, 31});
		return params;
	}

}
