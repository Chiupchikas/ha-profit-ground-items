package lt.chupchik.runelite.haprofit;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("chupchik-ha-profit-ground")
public interface HaProfitGroundConfig extends Config
{
	@ConfigItem(
		keyName = "profitColor",
		name = "Profitable HA color",
		description = "Color of the entire profitable (HA: ...) segment, leaving GE unchanged",
		position = 0
	)
	@Alpha
	default Color profitColor()
	{
		return new Color(0, 255, 100);
	}

	@ConfigItem(
		keyName = "minProfit",
		name = "Minimum profit per item (gp)",
		description = "Minimum HA minus GE minus one nature rune; 1 = any profit",
		position = 1
	)
	@Range(min = 1)
	default int minProfit()
	{
		return 1;
	}
}
