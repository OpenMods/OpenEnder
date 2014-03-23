package openender;

import net.minecraftforge.common.DimensionManager;
import openender.block.BlockUnbreakable;
import openender.common.WorldProviderEnder;
import openender.item.ItemCipherStone;
import openender.item.ItemEnderStone;
import openender.item.ItemGuide;
import openmods.config.*;

public class Config {

	@ItemId(description = "The id of the ender stone item")
	public static int itemEnderStoneId = 23000;

	@ItemId(description = "The id of the cipher stone item")
	public static int itemCipherStoneId = 23001;

	@ItemId(description = "The id of the guide item")
	public static int itemGuideId = 23002;

	@BlockId(description = "The id of the unbreakable block")
	public static int blockUnbreakableId = 3140;

	@ConfigProperty(category = "general", name = "enderDimensionProviderId", comment = "The id for the ender dimension provider")
	public static int enderDimensionProviderId = 5000;

	@OnLineModifiable
	@ConfigProperty(category = "general", name = "stoneRange", comment = "Minimal distance to temple-like structure needed for key to work")
	public static int stoneRange = 10;

	@OnLineModifiable
	@ConfigProperty(category = "general", name = "giveGuideOnJoin", comment = "Should the player get a free guide when they join?")
	public static boolean giveGuideOnJoin = true;

	public static void register() {
		DimensionManager.registerProviderType(enderDimensionProviderId, WorldProviderEnder.class, false);

		OpenEnder.Blocks.unbreakable = new BlockUnbreakable();

		if (itemEnderStoneId > 0) {
			OpenEnder.Items.enderStone = new ItemEnderStone();
		}

		if (itemCipherStoneId > 0) {
			OpenEnder.Items.cipherStone = new ItemCipherStone();
		}

		if (itemGuideId > 0) {
			OpenEnder.Items.guide = new ItemGuide();
		}

		final String modId = "openender";
		ConfigProcessing.registerItems(OpenEnder.Items.class, modId);
		ConfigProcessing.registerBlocks(OpenEnder.Blocks.class, modId);

	}
}
