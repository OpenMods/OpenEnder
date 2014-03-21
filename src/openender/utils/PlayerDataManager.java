package openender.utils;

import java.util.Stack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import openmods.utils.PlayerUtils;

public class PlayerDataManager {

	public static final String SPAWNS_TAG = "spawns";
	public static final String SPAWNS_TAG_COUNT = "count";
	public static final String SPAWNS_TAG_PREFIX = "spawn_";

	public static class SpawnList extends Stack<SpawnLocation> {

		private NBTTagCompound persistTag;

		public SpawnList(EntityPlayer player) {
			persistTag = PlayerUtils.getModPlayerPersistTag(player, "OpenEnder");
			initialize();
		}

		private void initialize() {

			clear();

			if (persistTag.hasKey(SPAWNS_TAG)) {

				NBTTagCompound spawnsTag = persistTag.getCompoundTag(SPAWNS_TAG);
				int amount = spawnsTag.getInteger(SPAWNS_TAG_COUNT);

				for (int i = 0; i < amount; i++) {
					NBTTagCompound spawnLocationTag = spawnsTag.getCompoundTag(SPAWNS_TAG_PREFIX + i);
					add(SpawnLocation.readFromNBT(spawnLocationTag));
				}

			}
		}

		public void save() {

			NBTTagCompound spawnsTag = new NBTTagCompound();

			spawnsTag.setInteger(SPAWNS_TAG_COUNT, size());
			for (int i = 0; i < size(); i++) {
				spawnsTag.setCompoundTag(SPAWNS_TAG_PREFIX + i, get(i).writeToNBT());
			}

			persistTag.setCompoundTag(SPAWNS_TAG, spawnsTag);
		}
	}

	public static class SpawnLocation {

		public int dimensionId;
		public ChunkCoordinates position;

		public SpawnLocation(int dimensionId, ChunkCoordinates position) {
			this.dimensionId = dimensionId;
			this.position = position;
		}

		public NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("dimensionId", dimensionId);
			tag.setInteger("x", position.posX);
			tag.setInteger("y", position.posY);
			tag.setInteger("z", position.posZ);
			return tag;
		}

		public static SpawnLocation readFromNBT(NBTTagCompound tag) {
			return new SpawnLocation(
					tag.getInteger("dimensionId"),
					new ChunkCoordinates(
							tag.getInteger("x"),
							tag.getInteger("y"),
							tag.getInteger("z")
					));
		}

	}

	public static void pushSpawnLocation(EntityPlayer player) {

		SpawnList spawnList = new SpawnList(player);

		ChunkCoordinates chunkCoord = new ChunkCoordinates(
				(int)player.posX,
				(int)player.posY,
				(int)player.posZ);

		spawnList.push(new SpawnLocation(player.dimension, chunkCoord));

		spawnList.save();
	}

	public static SpawnLocation popSpawnLocation(EntityPlayer player) {
		SpawnList spawnList = new SpawnList(player);
		if (spawnList.empty()) { return null; }
		SpawnLocation location = spawnList.pop();
		spawnList.save();
		return location;
	}

}
