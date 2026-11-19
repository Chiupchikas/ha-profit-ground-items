package lt.chupchik.runelite.haprofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class GroundDropTest
{
	@Test
	public void haAboveGeMeetsTheConfiguredDifference()
	{
		GroundDrop drop = new GroundDrop(1, "Example", null, 1200, 900, true, 2);
		assertEquals(300L, drop.haMinusGePerItem());
		assertTrue(drop.haMinusGePerItem() >= 0);
		assertTrue(drop.haMinusGePerItem() >= 300);
		assertFalse(drop.haMinusGePerItem() >= 301);
		assertEquals(2400L, drop.haTotal());
		assertEquals(1800L, drop.geTotal());
	}

	@Test
	public void equalHaAndGeMeetsTheZeroDifferenceThreshold()
	{
		GroundDrop drop = new GroundDrop(1, "Example", null, 1200, 1200, true, 1);
		assertEquals(0L, drop.haMinusGePerItem());
		assertTrue(drop.haMinusGePerItem() >= 0);
		assertFalse(drop.haMinusGePerItem() >= 1);
	}

	@Test
	public void haBelowGeDoesNotMeetTheZeroDifferenceThreshold()
	{
		GroundDrop drop = new GroundDrop(1, "Example", null, 900, 1200, true, 1);
		assertEquals(-300L, drop.haMinusGePerItem());
		assertFalse(drop.haMinusGePerItem() >= 0);
	}
}
