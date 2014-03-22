package openender.common;

import java.util.Arrays;

import net.minecraft.nbt.*;
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
		private static final String CODED_TAG = "CodedDimensions";
		private static final String DIMENSION_TAG = "Dimension";
		private static final String CODE_TAG = "Code";

		private int[] ownedDims = new int[0];
		private NBTTagCompound playerDims = new NBTTagCompound();
		private NBTTagList codedDims = new NBTTagList();

		public DimensionsData(String id) {
			super(id);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
			ownedDims = tag.getIntArray(MANAGED_TAG).clone();
			playerDims = (NBTTagCompound)tag.getCompoundTag(PRIVATE_TAG).copy();
			codedDims = (NBTTagList)tag.getTagList(CODED_TAG).copy();
		}

		@Override
		public void writeToNBT(NBTTagCompound tag) {
			tag.setIntArray(MANAGED_TAG, ownedDims);
			tag.setTag(PRIVATE_TAG, playerDims.copy());
			tag.setTag(CODED_TAG, codedDims.copy());
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

		public int getDimensionForCode(byte[] keyCode) {

			for (int i = 0; i < codedDims.tagCount(); i++) {

				NBTBase entry = codedDims.tagAt(i);

				if (entry instanceof NBTTagCompound) {

					NBTTagCompound entryTag = (NBTTagCompound)entry;

					NBTBase codeEntry = entryTag.getTag(CODE_TAG);
					NBTBase dimensionEntry = entryTag.getTag(DIMENSION_TAG);

					if (codeEntry instanceof NBTTagInt &&
							dimensionEntry instanceof NBTTagByteArray) {

						byte[] code = ((NBTTagByteArray)codeEntry).byteArray;

						if (Arrays.equals(code, keyCode)) {

							final int dimensionId = ((NBTTagInt)dimensionEntry).data;

							ensureDimensionIsRegistered(dimensionId);

							return dimensionId;
						}
					}
				}
			}

			return createDimensionForCode(keyCode);
		}

		private int createDimensionForCode(byte[] keyCode) {
			final int dimensionId = registerNewDimension();

			NBTTagCompound entry = new NBTTagCompound();
			entry.setInteger(DIMENSION_TAG, dimensionId);
			entry.setByteArray(CODE_TAG, keyCode);
			codedDims.appendTag(entry);

			setDirty(true);
			ensureDimensionIsRegistered(dimensionId);

			return dimensionId;
		}

		public int getDimensionForPlayer(String playerName) {
			NBTBase entry = playerDims.getTag(playerName);

			final int dimensionId;
			if (entry instanceof NBTTagInt) {
				dimensionId = ((NBTTagInt)entry).data;
			} else {
				dimensionId = registerNewDimension();
				playerDims.setInteger(playerName, dimensionId);
				setDirty(true);
			}

			ensureDimensionIsRegistered(dimensionId);

			return dimensionId;
		}

		private void ensureDimensionIsRegistered(int dimensionId) {
			if (!DimensionManager.isDimensionRegistered(dimensionId)) {
				DimensionManager.registerDimension(dimensionId, Config.enderDimensionProviderId);
			}
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

	public int getDimensionForCode(byte[] code) {
		Preconditions.checkState(data != null, "Not yet initialized");
		return data.getDimensionForCode(code);
	}

}
