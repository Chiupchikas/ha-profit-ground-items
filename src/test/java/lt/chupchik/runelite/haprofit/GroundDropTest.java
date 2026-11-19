package lt.chupchik.runelite.haprofit;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GroundDropTest
{
	@Test
	public void calculatesProfitAfterNatureRune()
	{
		GroundDrop drop = new GroundDrop(1, "Example", null, 1200, 900, true, 2);
		assertEquals(100L, drop.profitPerItem(200));
		assertEquals(2400L, drop.haTotal());
		assertEquals(1800L, drop.geTotal());
	}

	@Test
	public void aCostlierRuneMakesAlchingUnprofitable()
	{
		GroundDrop drop = new GroundDrop(1, "Example", null, 1200, 900, true, 1);
		assertEquals(-50L, drop.profitPerItem(350));
	}
}
