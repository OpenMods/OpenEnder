package openender.utils;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
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

	public static void strikeAreaAroundPlayer(World world, EntityPlayer player, int strikes, double distance) {
		for (int a = 0; a < 360; a += 360 / strikes) {
			double x = distance * Math.cos(a * (Math.PI / 180));
			double z = distance * Math.sin(a * (Math.PI / 180));
			world.addWeatherEffect(new EntityLightningBolt(world, player.posX + x, player.posY, player.posZ + z));
		}
	}

}
