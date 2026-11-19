package lt.chupchik.runelite.haprofit;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HaProfitGroundPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HaProfitGroundPlugin.class);
		RuneLite.main(args);
	}
}
