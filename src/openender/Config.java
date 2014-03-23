package openender;

import net.minecraftforge.common.DimensionManager;
import openender.block.BlockUnbreakable;
import openender.common.WorldProviderEnder;
import openender.item.ItemCipherKey;
import openender.item.ItemEnderKey;
import openender.item.ItemGuide;
import openmods.config.*;

public class Config {

	@ItemId(description = "The id of the ender key item")
	public static int itemEnderKeyId = 23000;

	@ItemId(description = "The id of the cipher key item")
	public static int itemCipherKeyId = 23001;

	@ItemId(description = "The id of the guide item")
	public static int itemGuideId = 23002;

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

		if (itemEnderKeyId > 0) {
			OpenEnder.Items.enderKey = new ItemEnderKey();
		}

		if (itemCipherKeyId > 0) {
			OpenEnder.Items.cipherKey = new ItemCipherKey();
		}

		if (itemGuideId > 0) {
			OpenEnder.Items.guide = new ItemGuide();
		}

		final String modId = "openender";
		ConfigProcessing.registerItems(OpenEnder.Items.class, modId);
		ConfigProcessing.registerBlocks(OpenEnder.Blocks.class, modId);

	}
}
