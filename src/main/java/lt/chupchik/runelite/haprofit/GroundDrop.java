package lt.chupchik.runelite.haprofit;

import net.runelite.api.ItemLayer;

final class GroundDrop
{
	final int itemId;
	final String name;
	final ItemLayer layer;
	final int haEach;
	final long geEach;
	final boolean tradeable;
	int quantity;

	GroundDrop(int itemId, String name, ItemLayer layer, int haEach,
		long geEach, boolean tradeable, int quantity)
	{
		this.itemId = itemId;
		this.name = name;
		this.layer = layer;
		this.haEach = haEach;
		this.geEach = geEach;
		this.tradeable = tradeable;
		this.quantity = quantity;
	}

	long haTotal()
	{
		return (long) haEach * quantity;
	}

	long geTotal()
	{
		return geEach * quantity;
	}

	long profitPerItem(long natureRunePrice)
	{
		return (long) haEach - geEach - natureRunePrice;
	}
}
