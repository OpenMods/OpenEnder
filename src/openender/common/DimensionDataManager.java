package openender.common;

import net.minecraft.nbt.NBTTagCompound;
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

		private static final String OWNER_DIMS_TAG = "OwnerDims";
		private int[] ownedDims = new int[0];

		public DimensionsData(String id) {
			super(id);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
			ownedDims = tag.getIntArray(OWNER_DIMS_TAG);
		}

		@Override
		public void writeToNBT(NBTTagCompound tag) {
			tag.setIntArray(OWNER_DIMS_TAG, ownedDims);
		}

		private void registerDims() {
			for (int dim : ownedDims)
				DimensionManager.registerDimension(dim, Config.enderDimensionProviderId);
		}

		public void addDimension(int id) {
			ownedDims = ArrayUtils.add(ownedDims, id);
			setDirty(true);
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
		int id = DimensionManager.getNextFreeDimId();
		data.addDimension(id);
		return id;
	}

}
