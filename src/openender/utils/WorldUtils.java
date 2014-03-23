package openender.utils;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import openender.Config;

public class WorldUtils {

	public static boolean isEnderDimension(World world) {
		if (world == null) return false;
		return isEnderDimension(world.provider.dimensionId);
	}
	
	public static boolean isEnderDimension(int dimId) {
		int providerId = DimensionManager.getProviderType(dimId);
		return providerId == Config.enderDimensionProviderId;
	}
	
}
