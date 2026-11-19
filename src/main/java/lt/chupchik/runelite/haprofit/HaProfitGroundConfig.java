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
		description = "Current Nature rune GE price for reference only. It is not subtracted from HA-GE. Updated automatically; manual edits are ignored.",
		position = 0
	)
	default String natureRuneGePrice()
	{
		return "Waiting for game tick...";
	}

	@ConfigItem(
		keyName = "profitColor",
		name = "HA highlight color",
		description = "Color of the entire (HA: ...) segment when HA-GE meets the threshold, leaving GE unchanged",
		position = 2
	)
	@Alpha
	default Color profitColor()
	{
		return new Color(0, 255, 100);
	}

	@ConfigItem(
		keyName = "minProfit",
		name = "Minimum HA - GE (gp)",
		description = "Minimum HA minus GE per item. 0 = green when HA equals or exceeds GE. Nature rune is not deducted.",
		position = 1
	)
	@Range(min = 0)
	default int minProfit()
	{
		return 0;
	}
}
