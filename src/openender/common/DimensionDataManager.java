package openender.common;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import openender.Config;
import openender.utils.WorldUtils;
import openmods.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;

public class DimensionDataManager {

	private static final String DIMENSION_ID = "open-ender-dims";

	public static final DimensionDataManager instance = new DimensionDataManager();

	public static class DimensionsData extends WorldSavedData {

		private static final String ROOT_TAG = "Dimensions";

		private static final String PLAYER_TAG = "Player";
		private static final String DIMENSION_TAG = "Dimension";
		private static final String CODE_TAG = "Code";

		private final Set<Integer> ownedDims = Sets.newHashSet();
		private BiMap<String, Integer> playerDims = HashBiMap.create();
		private BiMap<NBTTagCompound, Integer> codedDims = HashBiMap.create();

		public DimensionsData(String id) {
			super(id);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
			NBTTagList dimensionsData = tag.getTagList(ROOT_TAG);
			for (int i = 0; i < dimensionsData.tagCount(); i++) {
				NBTTagCompound entry = (NBTTagCompound)dimensionsData.tagAt(i);

				int dimensionId = entry.getInteger(DIMENSION_TAG);
				boolean isNew = ownedDims.add(dimensionId);
				if (!isNew) {
					Log.severe("Dimension id %s occured more than once in data list", dimensionId);
					continue;
				}

				if (entry.hasKey(PLAYER_TAG)) {
					String playerName = entry.getString(PLAYER_TAG);
					try {
						Integer prev = playerDims.put(playerName, dimensionId);
						if (prev != null) {
							Log.severe("Player %s has more than two private dimensions: %s, %s", playerName, dimensionId, prev);
							continue;
						}
					} catch (IllegalArgumentException e) {
						Log.severe("Dimension %d is marked as private dimension for more than two players", dimensionId);
					}
				}

				if (entry.hasKey(CODE_TAG)) {
					NBTTagCompound key = entry.getCompoundTag(CODE_TAG);

					try {
						Integer prev = codedDims.put(key, dimensionId);
						if (prev != null) {
							Log.severe("Key %s is associated with more than two private dimensions: %s, %s", key, dimensionId, prev);
							continue;
						}
					} catch (IllegalArgumentException e) {
						Log.severe("Dimension %d has more than two keys", dimensionId);
					}
				}
			}

		}

		@Override
		public void writeToNBT(NBTTagCompound tag) {
			NBTTagList dimensionData = new NBTTagList();

			for (Integer dimensionId : ownedDims) {
				NBTTagCompound entry = new NBTTagCompound();
				entry.setInteger(DIMENSION_TAG, dimensionId);

				NBTTagCompound code = codedDims.inverse().get(dimensionId);
				if (code != null) entry.setTag(CODE_TAG, code.copy());

				String playerName = playerDims.inverse().get(dimensionId);
				if (playerName != null) entry.setString(PLAYER_TAG, playerName);
				dimensionData.appendTag(entry);
			}

			tag.setTag(ROOT_TAG, dimensionData);
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
			return playerDims.inverse().get(dimensionId);
		}

		public int getDimensionForPlayer(String playerName) {
			Integer dimensionId = playerDims.get(playerName);

			if (dimensionId != null) return dimensionId;

			int newDimensionId = registerNewDimension();
			Log.info("No valid dimension for player %s, registering new %d", playerName, newDimensionId);
			playerDims.put(playerName, newDimensionId);
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
