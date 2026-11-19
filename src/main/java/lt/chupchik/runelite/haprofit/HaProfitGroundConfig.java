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
		keyName = "natureRuneGePrice",
		name = "Nature rune GE price (auto)",
		description = "Current Nature rune price used for the calculation. Updated automatically; manual edits are ignored.",
		position = 0
	)
	default String natureRuneGePrice()
	{
		return "Waiting for game tick...";
	}

	@ConfigItem(
		keyName = "profitColor",
		name = "Profitable HA color",
		description = "Color of the entire profitable (HA: ...) segment, leaving GE unchanged",
		position = 2
	)
	@Alpha
	default Color profitColor()
	{
		return new Color(0, 255, 100);
	}

	@ConfigItem(
		keyName = "minProfit",
		name = "Color HA from profit (gp)",
		description = "HA minus GE minus Nature rune per item. 1 = highlight any positive profit",
		position = 1
	)
	@Range(min = 1)
	default int minProfit()
	{
		return 1;
	}
}
