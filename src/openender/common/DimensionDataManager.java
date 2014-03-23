package openender.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import openender.Config;
import openender.utils.WorldUtils;
import openmods.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

public class DimensionDataManager {

	private static final String DIMENSION_ID = "OEDims";

	public static final DimensionDataManager instance = new DimensionDataManager();

	public static class DimensionsData extends WorldSavedData {

		private static final String MANAGED_TAG = "AllDimensions";
		private static final String PRIVATE_TAG = "PrivateDimensions";
		private static final String CODED_TAG = "CodedDimensions";
		private static final String DIMENSION_TAG = "Dimension";
		private static final String CODE_TAG = "Code";

		private final Set<Integer> ownedDims = Sets.newHashSet();
		private NBTTagCompound playerDims = new NBTTagCompound();
		private Map<NBTTagCompound, Integer> codedDims = Maps.newHashMap();

		public DimensionsData(String id) {
			super(id);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
			for (int dim : tag.getIntArray(MANAGED_TAG))
				ownedDims.add(dim);

			playerDims = (NBTTagCompound)tag.getCompoundTag(PRIVATE_TAG).copy();

			NBTTagList codedDims = tag.getTagList(CODED_TAG);
			for (int i = 0; i < codedDims.tagCount(); i++) {
				NBTTagCompound entry = (NBTTagCompound)codedDims.tagAt(i);
				NBTTagCompound code = entry.getCompoundTag(CODE_TAG);
				int dimension = entry.getInteger(DIMENSION_TAG);

				if (ownedDims.contains(dimension)) {
					this.codedDims.put((NBTTagCompound)code.copy(), dimension);
				} else {
					Log.severe("Data inconsistency: dimension %d (code %s) is not registered as ours!", dimension, code);
				}
			}
		}

		@Override
		public void writeToNBT(NBTTagCompound tag) {
			tag.setIntArray(MANAGED_TAG, Ints.toArray(ownedDims));
			tag.setTag(PRIVATE_TAG, playerDims.copy());

			NBTTagList codedDims = new NBTTagList();
			for (Map.Entry<NBTTagCompound, Integer> e : this.codedDims.entrySet()) {
				NBTTagCompound entry = new NBTTagCompound();
				entry.setCompoundTag(CODE_TAG, e.getKey());
				entry.setInteger(DIMENSION_TAG, e.getValue());
				codedDims.appendTag(entry);
			}

			tag.setTag(CODED_TAG, codedDims);
		}

		private void registerDims() {
			for (int dim : ownedDims) {
				if (DimensionManager.isDimensionRegistered(dim)) {
					if (!WorldUtils.isEnderDimension(dim)) {
						Log.severe("Dimension %s is marked as OpenEnder world,  but is already registered with different provider. Strange things may happen",
								dim);
					}
				} else DimensionManager.registerDimension(dim, Config.enderDimensionProviderId);
			}
		}

		private void unregisterDims() {
			for (int dim : ownedDims)
				if (isValidEnderDimension(dim)) DimensionManager.unregisterDimension(dim);
		}

		private void addDimension(int id) {
			ownedDims.add(id);
			setDirty(true);
		}

		public int registerNewDimension() {
			int id = DimensionManager.getNextFreeDimId();
			DimensionManager.registerDimension(id, Config.enderDimensionProviderId);
			addDimension(id);
			return id;
		}

		public int getDimensionForCode(NBTTagCompound keyCode) {

			// need to ensure names match
			if (keyCode != null) {
				keyCode = (NBTTagCompound)keyCode.copy();
				keyCode.setName(CODE_TAG);
			}

			Integer dimensionId = codedDims.get(keyCode);

			if (dimensionId != null && isValidEnderDimension(dimensionId)) return dimensionId;

			int newDimensionId = registerNewDimension();
			Log.info("No valid dimension for code %s, registering new %d", keyCode, newDimensionId);
			codedDims.put((NBTTagCompound)keyCode.copy(), newDimensionId);
			setDirty(true);
			return newDimensionId;
		}

		public String getDimensionOwner(int dimensionId) {

			Collection<NBTBase> entries = playerDims.getTags();
			for (NBTBase entry : entries) {
				if (entry instanceof NBTTagInt) {
					int dimId = ((NBTTagInt)entry).data;
					if (dimId == dimensionId) return entry.getName();
				}
			}

			return null;
		}

		public int getDimensionForPlayer(String playerName) {
			NBTBase entry = playerDims.getTag(playerName);

			if (entry instanceof NBTTagInt) {
				int dimensionId = ((NBTTagInt)entry).data;
				if (isValidEnderDimension(dimensionId)) return dimensionId;
			}

			int newDimensionId = registerNewDimension();
			Log.info("No valid dimension for player %s, registering new %d", playerName, newDimensionId);
			playerDims.setInteger(playerName, newDimensionId);
			setDirty(true);
			return newDimensionId;
		}

		private boolean isValidEnderDimension(int dimensionId) {
			if (!DimensionManager.isDimensionRegistered(dimensionId)) {
				Log.warn("Known dimensions %s is not registered! Possible world corruption", dimensionId);
				return false;
			}

			if (!WorldUtils.isEnderDimension(dimensionId)) {
				Log.warn("Known dimension was registered with unknown provider! Possible world corruption", dimensionId);
				return false;
			}

			if (!ownedDims.contains(dimensionId)) {
				Log.warn("Dimension %d was registered with our provider, but not in owned list! Possible world corruption", dimensionId);
				addDimension(dimensionId);
				// still valid situation, returning true
			}

			return true;
		}
	}

	private DimensionsData data;

	public void onServerStart(MinecraftServer srv) {
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

	public void onServerStop() {
		if (data == null) Log.warn("Server not loaded");
		else data.unregisterDims();

		data = null;
	}

	public int getNewDimensionId() {
		Preconditions.checkState(data != null, "Server not loaded");
		return data.registerNewDimension();
	}

	public int getDimensionForPlayer(String playerName) {
		Preconditions.checkState(data != null, "Server not loaded");
		return data.getDimensionForPlayer(playerName);
	}

	public int getDimensionForKey(NBTTagCompound key) {
		Preconditions.checkState(data != null, "Not yet initialized");
		return data.getDimensionForCode(key);
	}

	public String getDimensionOwner(int dimensionId) {
		Preconditions.checkState(data != null, "Not yet initialized");
		return data.getDimensionOwner(dimensionId);
	}

}
