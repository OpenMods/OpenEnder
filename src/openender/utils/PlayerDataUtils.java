package openender.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import openender.Config;
import openender.common.DimensionDataManager;
import openmods.utils.PlayerUtils;

public class PlayerDataUtils {

	public static final String ENDER_ID_TAG = "enderId";

	public static int getPlayerDimensionId(EntityPlayer player) {

		NBTTagCompound persistTag = PlayerUtils.getModPlayerPersistTag(player, "OpenEnder");

		if (!persistTag.hasKey(ENDER_ID_TAG)) {
			persistTag.setInteger(ENDER_ID_TAG, DimensionDataManager.instance.getNewDimensionId());
		}

		int dimensionId = persistTag.getInteger(ENDER_ID_TAG);

		if (!DimensionManager.isDimensionRegistered(dimensionId)) {
			DimensionManager.registerDimension(dimensionId, Config.enderDimensionProviderId);
		}

		return dimensionId;
	}

}
