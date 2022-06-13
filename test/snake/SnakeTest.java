package snake;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SnakeTest {
	
	/**
	 * A kígyó objektum, ami segítségével tesztel.
	 */
	
	private Snake snake;
	
	/**
	 * Létrehozza a kígyót.
	 */
	@Before
	public void setUp() {
		snake = new Snake(50, 50, 40, 2.0, 10, 20);
	}
	
	/**
	 * Teszteli a givePoints() metódust.
	 * Teszteli, hogy negatív pont esetén nem változik és teszteli, hogy pozitív pont esetén hozzáadódik a kígyó pontszámához.
	 */
	@Test
	public void testGivePoints(){
		int points = snake.getPoints();
		snake.givePoints(20);
		Assert.assertEquals(points+20, snake.getPoints());
		points = points + 20;
		snake.givePoints(-200);
		Assert.assertEquals(points, snake.getPoints());
	}
	
	/**
	 * Teszteli a givePoints() metódust a buffok hatását vizsgálva.
	 * Hozzáad két buffot és megnézi, hogy volt-e hatásuk.
	 */
	@Test
	public void testGivePointsModifiers() {
		int points = snake.getPoints();
		Buff buff1 = new Buff(100);
		buff1.addModifier("points", 2);
		Buff buff2 = new Buff(200);
		buff2.addModifier("points", 10.5);
		snake.addBuff(buff1);
		snake.addBuff(buff2);
		
		snake.givePoints(100);
		Assert.assertEquals(points+100*2*10.5, snake.getPoints(), 0.01);
	}
	
	/**
	 * Teszteli a giveLength() metódust.
	 * Megnézi, hogy a kígyó hossza nőtt-e és, hogy a legutolsó kígyóvonal elemszáma is nőtt-e.
	 */
	@Test
	public void testGiveLength() {
		int bonusLength = 30;
		int length = snake.getLength();
		LinkedList<SnakeLine> lines = snake.getLines();
		int startingLength = lines.getLast().getElements().size();
		
		snake.giveLength(bonusLength);
		Assert.assertEquals(length + bonusLength, snake.getLength());
		Assert.assertEquals(startingLength + bonusLength, lines.getLast().getElements().size());
	}
	
	/**
	 * Teszteli a giveLength() metódust a buffok hatását vizsgálva.
	 * Megnézi, hogy a kígyó hossza nőtt-e és, hogy a legutolsó kígyóvonal elemszáma is nőtt-e.
	 */
	@Test
	public void testGiveLengthModifiers() {
		Buff buff1 = new Buff(150);
		buff1.addModifier("length", 3);
		Buff buff2 = new Buff(40);
		buff2.addModifier("length", 51.34231);
		snake.addBuff(buff1);
		snake.addBuff(buff2);
		
		
		int bonusLength = 30;
		int length = snake.getLength();
		LinkedList<SnakeLine> lines = snake.getLines();
		int expectedLength = length + (int) (bonusLength*3*51.34231);
		
		snake.giveLength(bonusLength);
		Assert.assertEquals(expectedLength, snake.getLength());
		Assert.assertEquals(expectedLength, lines.getLast().getElements().size());
	}
	
	/**
	 * Teszteli a damageHealth() metódust
	 * Sebzi a kígyót és megnézi, hogy csökkent-e az élete és, hogy nem csökkent 0 alá.
	 */
	@Test
	public void testDamageHealth() {
		double health = snake.getHealth();
		double damage = 40.5443534;
		snake.damageHealth(damage);
		Assert.assertEquals(health-damage, snake.getHealth(), 0.01);
		damage = 67.012312;
		health = snake.getHealth();
		snake.damageHealth(damage);
		Assert.assertEquals(health-damage, snake.getHealth(), 0.01);
	}
	
	/**
	 * Teszteli a damageHealth() metódust a buffok hatását is vizsgálva.
	 * Sebzi a kígyót és megnézi, hogy csökkent-e az élete és, hogy nem csökkent 0 alá.
	 */
	
	@Test
	public void testDamageHealthModifiers() {
		Buff buff1 = new Buff(150);
		buff1.addModifier("damage", 3);
		Buff buff2 = new Buff(40);
		buff2.addModifier("damage", 51.34231);
		snake.addBuff(buff1);
		snake.addBuff(buff2);
		
		
		double health = snake.getHealth();
		double damage = 40.5443534;
		snake.damageHealth(damage);
		Assert.assertEquals(health-damage*3*51.34231, snake.getHealth(), 0.01);
		damage = 67.012312;
		health = snake.getHealth();
		snake.damageHealth(damage);
		Assert.assertEquals(health-damage*3*51.34231, snake.getHealth(), 0.01);
	}
	
	
	/**
	 * Teszteli a rotate() metódust.
	 * Elforgatja egy szöggel és megvizsgálja, hogy változott-e a kígyó szöge, keletkezett-e még egy kígyóvonal.
	 * Továbbá megvizsgálja, hogy a legelső elem átkerült-e az új kígyóvonalba, az új kígyóvonal az elejére került-e
	 * a kígyóvonalak listájának.
	 */
	@Test
	public void testRotate() {
		SnakeElement firstElement = snake.getLines().getFirst().getElements().getFirst();
		SnakeLine startingFirstLine = snake.getLines().getFirst();
		int size = snake.getLines().size();
		snake.rotate(2*Math.PI+1.5);
		Assert.assertEquals(1.5, snake.getAngle(), 0.01);
		Assert.assertEquals(size+1, snake.getLines().size());
		Assert.assertNotSame(startingFirstLine, snake.getLines().getFirst());
		Assert.assertNotSame(firstElement, startingFirstLine.getElements().getFirst());
		Assert.assertSame(firstElement, snake.getLines().getFirst().getElements().getFirst());
	}
}
