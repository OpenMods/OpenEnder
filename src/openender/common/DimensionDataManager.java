package openender.common;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import openender.Config;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Preconditions;

public class DimensionDataManager {

	private static final String DIMENSION_ID = "OEDims";

	public static final DimensionDataManager instance = new DimensionDataManager();

	public static class DimensionsData extends WorldSavedData {

		private static final String MANAGED_TAG = "AllDimensions";
		private static final String PRIVATE_TAG = "PrivateDimensions";

		private int[] ownedDims = new int[0];
		private NBTTagCompound playerDims = new NBTTagCompound();

		public DimensionsData(String id) {
			super(id);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
			ownedDims = tag.getIntArray(MANAGED_TAG).clone();
			playerDims = (NBTTagCompound)tag.getCompoundTag(PRIVATE_TAG).copy();
		}

		@Override
		public void writeToNBT(NBTTagCompound tag) {
			tag.setIntArray(MANAGED_TAG, ownedDims);
			tag.setTag(PRIVATE_TAG, playerDims.copy());
		}

		private void registerDims() {
			for (int dim : ownedDims)
				DimensionManager.registerDimension(dim, Config.enderDimensionProviderId);
		}

		private void addDimension(int id) {
			ownedDims = ArrayUtils.add(ownedDims, id);
			setDirty(true);
		}

		public int registerNewDimension() {
			int id = DimensionManager.getNextFreeDimId();
			addDimension(id);
			return id;
		}

		public int getDimensionForPlayer(String playerName) {
			NBTBase entry = playerDims.getTag(playerName);

			final int dimensionId;
			if (entry instanceof NBTTagInt) {
				dimensionId = ((NBTTagInt)entry).data;
			}
			else {
				dimensionId = registerNewDimension();
				playerDims.setInteger(playerName, dimensionId);
				setDirty(true);
			}

			if (!DimensionManager.isDimensionRegistered(dimensionId)) {
				DimensionManager.registerDimension(dimensionId, Config.enderDimensionProviderId);
			}

			return dimensionId;
		}
	}

	private DimensionsData data;

	public void registerDimensions(MinecraftServer srv) {
		final World overworld = srv.worldServerForDimension(0);
		DimensionsData newData = (DimensionsData)overworld.loadItemData(DimensionsData.class, DIMENSION_ID);

		if (newData != null) {
			newData.registerDims();
		} else {
			newData = new DimensionsData(DIMENSION_ID);
			overworld.setItemData(DIMENSION_ID, newData);
		}

		data = newData;
	}

	public int getNewDimensionId() {
		Preconditions.checkState(data != null, "Not yet initialized");
		return data.registerNewDimension();
	}

	public int getDimensionForPlayer(String playerName) {
		Preconditions.checkState(data != null, "Not yet initialized");
		return data.getDimensionForPlayer(playerName);
	}

}
