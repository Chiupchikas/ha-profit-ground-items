package lt.chupchik.runelite.haprofit;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.Provides;
import java.util.Collection;
import javax.inject.Inject;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemLayer;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemQuantityChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.WorldViewUnloaded;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "HA Profit Ground Items",
	description = "Color HA values when HA is at least GE inside Ground Items labels",
	tags = {"ground", "loot", "alchemy", "profit"},
	internalName = "ha-profit-ground-items"
)
public class HaProfitGroundPlugin extends Plugin
{
	private static final String CONFIG_GROUP = "chupchik-ha-profit-ground";
	private static final String RUNE_PRICE_KEY = "natureRuneGePrice";

	@Inject private ItemManager itemManager;
	@Inject private OverlayManager overlayManager;
	@Inject private HaProfitGroundOverlay overlay;
	@Inject private ConfigManager configManager;

	// Match the iteration order used by RuneLite's built-in Ground Items plugin.
	private final Table<WorldPoint, Integer, GroundDrop> drops = HashBasedTable.create();
	private long natureRunePrice;

	@Provides
	HaProfitGroundConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HaProfitGroundConfig.class);
	}

	@Override
	protected void startUp()
	{
		drops.clear();
		// Item prices require the client thread; onGameTick will populate this value.
		natureRunePrice = 0L;
		updateNatureRunePriceDisplay();
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		drops.clear();
		natureRunePrice = 0L;
		configManager.setConfiguration(CONFIG_GROUP, RUNE_PRICE_KEY, "Plugin disabled");
	}

	Collection<GroundDrop> getDrops()
	{
		return drops.values();
	}

	long getNatureRunePrice()
	{
		return natureRunePrice;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		// Work outside the frame-render loop.
		natureRunePrice = itemManager.getItemPrice(ItemID.NATURERUNE);
		updateNatureRunePriceDisplay();
	}

	private void updateNatureRunePriceDisplay()
	{
		String value = natureRunePrice > 0 ? natureRunePrice + " gp" : "Waiting for game tick...";
		if (!value.equals(configManager.getConfiguration(CONFIG_GROUP, RUNE_PRICE_KEY)))
		{
			configManager.setConfiguration(CONFIG_GROUP, RUNE_PRICE_KEY, value);
		}
	}

	@Subscribe
	public void onWorldViewUnloaded(WorldViewUnloaded event)
	{
		drops.values().removeIf(drop -> drop.layer.getWorldView() == event.getWorldView());
	}

	@Subscribe
	public void onItemSpawned(ItemSpawned event)
	{
		Tile tile = event.getTile();
		TileItem item = event.getItem();
		WorldPoint worldPoint = tile.getWorldLocation();
		GroundDrop existing = drops.get(worldPoint, item.getId());
		if (existing != null)
		{
			existing.quantity += item.getQuantity();
			return;
		}

		ItemLayer layer = tile.getItemLayer();
		if (layer == null)
		{
			return;
		}
		ItemComposition composition = itemManager.getItemComposition(item.getId());
		int geId = composition.getNote() != -1 ? composition.getLinkedNoteId() : item.getId();
		long geEach = geId == ItemID.COINS ? 1 : itemManager.getItemPrice(geId);
		int haEach = geId == ItemID.COINS ? 1 : composition.getHaPrice();
		drops.put(worldPoint, item.getId(), new GroundDrop(
			item.getId(), composition.getName(), layer, haEach, geEach,
			composition.isGeTradeable(), item.getQuantity()));
	}

	@Subscribe
	public void onItemDespawned(ItemDespawned event)
	{
		GroundDrop drop = drops.get(event.getTile().getWorldLocation(), event.getItem().getId());
		if (drop == null)
		{
			return;
		}
		if (drop.quantity <= event.getItem().getQuantity())
		{
			drops.remove(event.getTile().getWorldLocation(), event.getItem().getId());
		}
		else
		{
			drop.quantity -= event.getItem().getQuantity();
		}
	}

	@Subscribe
	public void onItemQuantityChanged(ItemQuantityChanged event)
	{
		GroundDrop drop = drops.get(event.getTile().getWorldLocation(), event.getItem().getId());
		if (drop != null)
		{
			drop.quantity += event.getNewQuantity() - event.getOldQuantity();
		}
	}
}
