package openender;

import java.util.List;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.DimensionManager;
import openender.common.WorldProviderEnder;
import openender.common.item.ItemEnderLocker;
import openmods.config.ConfigProcessing;
import openmods.config.ConfigProperty;
import openmods.config.ItemId;


public class Config {

	@ItemId(description = "The id of the ender locker item")
	public static int itemEnderLockerId = 23000;

	@ConfigProperty(category = "general", name = "enderDimensionProviderId", comment = "The id for the ender dimension provider")
	public static int enderDimensionProviderId = 5000;
	
	public static void register() {
		
		DimensionManager.registerProviderType(enderDimensionProviderId, WorldProviderEnder.class, false);

		@SuppressWarnings("unchecked")
		final List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();
		
		if (itemEnderLockerId > 0) {
			OpenEnder.Items.enderLocker = new ItemEnderLocker();
		}
		
		final String modId = "openender";
		ConfigProcessing.registerItems(OpenEnder.Items.class, modId);
		ConfigProcessing.registerBlocks(OpenEnder.Blocks.class, modId);

	}
}
