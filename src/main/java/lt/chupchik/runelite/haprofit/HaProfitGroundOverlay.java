package lt.chupchik.runelite.haprofit;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.grounditems.GroundItemsConfig;
import net.runelite.client.plugins.grounditems.config.ItemHighlightMode;
import net.runelite.client.plugins.grounditems.config.PriceDisplayMode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.util.QuantityFormatter;

/** Paint only the HA number above RuneLite's existing Ground Items label. */
public class HaProfitGroundOverlay extends Overlay
{
	private static final int MAX_DISTANCE = 2500;
	private static final int OFFSET_Z = 20;
	private static final int STRING_GAP = 15;

	private final Client client;
	private final HaProfitGroundPlugin plugin;
	private final HaProfitGroundConfig config;
	private final GroundItemsConfig groundConfig;
	private final TextComponent textComponent = new TextComponent();
	private final Map<WorldPoint, Integer> offsetMap = new HashMap<>();
	private final StringBuilder label = new StringBuilder();

	@Inject
	public HaProfitGroundOverlay(Client client, HaProfitGroundPlugin plugin,
		HaProfitGroundConfig config, ConfigManager configManager)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(PRIORITY_HIGHEST);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		// Ground Items belongs to a separate plugin injector; obtain its config
		// using ConfigManager so our plugin can load independently.
		this.groundConfig = configManager.getConfig(GroundItemsConfig.class);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		ItemHighlightMode highlightMode = groundConfig.itemHighlightMode();
		if (groundConfig.priceDisplayMode() != PriceDisplayMode.BOTH
			|| highlightMode == ItemHighlightMode.MENU
			|| highlightMode == ItemHighlightMode.NONE
			|| groundConfig.showHighlightedOnly())
		{
			return null;
		}

		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return null;
		}

		LocalPoint playerLocation = player.getLocalLocation();
		FontMetrics fm = graphics.getFontMetrics();
		offsetMap.clear();

		for (GroundDrop drop : plugin.getDrops())
		{
			WorldView view = drop.layer.getWorldView();
			LocalPoint location = LocalPoint.fromWorld(view, drop.layer.getWorldLocation());
			if (location == null || (location.getWorldView() == WorldView.TOPLEVEL
				&& playerLocation.distanceTo(location) > MAX_DISTANCE))
			{
				continue;
			}
			// Hidden low-value drops do not consume a text line in Ground Items.
			// A manually highlighted low-value item is an acknowledged edge case.
			if (drop.geTotal() < groundConfig.getHideUnderValue()
				&& drop.haTotal() < groundConfig.getHideUnderValue())
			{
				continue;
			}

			// Drops that do not meet the HA-GE threshold still occupy a line.
			int offset = offsetMap.compute(drop.layer.getWorldLocation(),
				(k, old) -> old == null ? 0 : old + 1);

			if (drop.itemId == ItemID.COINS || !drop.tradeable
				|| drop.geEach <= 0 || drop.haEach <= 0
				|| drop.haMinusGePerItem() < config.minProfit())
			{
				continue;
			}

			label.append(drop.name);
			if (drop.quantity > 1)
			{
				label.append(" (")
					.append(QuantityFormatter.quantityToStackSize(drop.quantity))
					.append(')');
			}
			label.append(" (GE: ")
				.append(QuantityFormatter.quantityToStackSize(drop.geTotal()))
				.append(" gp)");
			String prefix = label.toString();
			String haPart = " (HA: " + QuantityFormatter.quantityToStackSize(drop.haTotal()) + " gp)";
			label.append(haPart);
			String fullText = label.toString();
			label.setLength(0);

			Point textPoint = Perspective.getCanvasTextLocation(client, graphics,
				location, fullText, drop.layer.getHeight() + OFFSET_Z);
			if (textPoint == null)
			{
				continue;
			}

			textComponent.setText(haPart);
			textComponent.setColor(config.profitColor());
			textComponent.setOutline(groundConfig.textOutline());
			textComponent.setPosition(new java.awt.Point(
				textPoint.getX() + fm.stringWidth(prefix),
				textPoint.getY() - STRING_GAP * offset));
			textComponent.render(graphics);
		}
		return null;
	}
}
