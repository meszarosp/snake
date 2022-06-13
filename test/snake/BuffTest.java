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
public class BuffTest {
	
	/**
	 * Buff, aminek a segítségével tesztel.
	 */
	private Buff buff;
	
	/**
	 * A buff élettartama.
	 */
	private int ttl;
	
	
	/**
	 * Konstruktor, a paraméteres teszteléshez. Beállítja a ttl értékét.
	 * @param ttl A buff élettartama.
	 */
	public BuffTest(int ttl) {
		this.ttl = ttl;
	}
	
	/**
	 * Létrehoz egy olyan buffot, amelynek az élettartama a paraméteres tesztben megadott.
	 */
	@Before
	public void setUp() {
		buff = new Buff(ttl);
	}
	
	/**
	 * A step() és az isAlive() metódusokat teszteli.
	 * Ha még él a buff, akkor az isAlive() igaz, minden step()-ben egyel kell, hogy csökkenjen az élettartam,
	 * mindvégig él a buff. Miután ttl-szer csökkent az élet, akkor az isAlive() hamissal tér vissza.
	 */
	@Test
	public void testStepIsAlive() {
		for (int i = 0; i < ttl; i++) {
			Assert.assertTrue(buff.isAlive());
			buff.step();
		}
		Assert.assertFalse(buff.isAlive());
	}
	
	/**
	 * Teszteli a modifyValue() függvényt.
	 * A buffhoz hozzáad egy módosítót, megnézi, hogy helyes értéket ad-e a modifyValue() és,
	 * hogy más attribútumra nem változtat semmit az értéken.
	 */
	@Test
	public void testModifyValue() {
		buff.addModifier("modifier1", 20);
		Assert.assertEquals(5*20, buff.modifyValue("modifier1", 5), 0.01);
		Assert.assertEquals(5, buff.modifyValue("modifier2", 5), 0.01);
	}

	
	/**
	 * A paraméteres teszteléshez készíti el a paramétereket.
	 * @return a paraméterek listája.
	 */
	@Parameters
	public static List<Object[]> parameters(){
		List<Object[]> params = new ArrayList<Object[]>();
		params.add(new Object[] {100});
		params.add(new Object[] {0});
		params.add(new Object[] {-200});
		return params;
	}
}
