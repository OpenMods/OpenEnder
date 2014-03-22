package openender;

import net.minecraftforge.common.DimensionManager;
import openender.block.BlockUnbreakable;
import openender.common.WorldProviderEnder;
import openender.item.ItemEnderLocker;
import openmods.config.*;

public class Config {

	@ItemId(description = "The id of the ender locker item")
	public static int itemEnderLockerId = 23000;

	@BlockId(description = "The id of the unbreakable block")
	public static int blockUnbreakableId = 3140;

	@ConfigProperty(category = "general", name = "enderDimensionProviderId", comment = "The id for the ender dimension provider")
	public static int enderDimensionProviderId = 5000;

	@OnLineModifiable
	@ConfigProperty(category = "general", name = "enderKeyRange", comment = "Minimal distance to temple-like structure needed for key to work")
	public static int keyRange = 10;

	public static void register() {
		DimensionManager.registerProviderType(enderDimensionProviderId, WorldProviderEnder.class, false);

		OpenEnder.Blocks.unbreakable = new BlockUnbreakable();

		if (itemEnderLockerId > 0) {
			OpenEnder.Items.enderLocker = new ItemEnderLocker();
		}

		final String modId = "openender";
		ConfigProcessing.registerItems(OpenEnder.Items.class, modId);
		ConfigProcessing.registerBlocks(OpenEnder.Blocks.class, modId);

	}
}
