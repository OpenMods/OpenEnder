package openender.utils;

import java.util.LinkedList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import openmods.utils.PlayerUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class PlayerDataManager {

	public static final String OPEN_ENDER_TAG = "OpenEnder";
	public static final String SPAWNS_TAG = "spawns";

	public static class SpawnList {
		private final LinkedList<SpawnLocation> locations = Lists.newLinkedList();

		private final NBTTagCompound persistTag;

		public SpawnList(EntityPlayer player) {
			persistTag = PlayerUtils.getModPlayerPersistTag(player, OPEN_ENDER_TAG);
			load();
		}

		protected void load() {
			NBTTagList spawnsTag = persistTag.getTagList(SPAWNS_TAG);
			for (int i = 0; i < spawnsTag.tagCount(); i++) {
				NBTTagCompound spawnLocationTag = (NBTTagCompound)spawnsTag.tagAt(i);
				locations.add(SpawnLocation.readFromNBT(spawnLocationTag));
			}
		}

		private void save() {
			NBTTagList spawnsTag = new NBTTagList();

			for (SpawnLocation location : locations)
				spawnsTag.appendTag(location.writeToNBT());

			persistTag.setTag(SPAWNS_TAG, spawnsTag);
		}

		public SpawnLocation pop() {
			if (locations.isEmpty()) return null;
			SpawnLocation location = locations.pop();
			save();
			return location;
		}

		public void push(SpawnLocation location) {
			locations.push(location);
			save();
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

		@Override
		public String toString() {
			return Objects.toStringHelper("spawn location")
					.add("dimensionId", dimensionId)
					.add("x", position.posX)
					.add("y", position.posY)
					.add("z", position.posZ)
					.toString();
		}

	}

	public static void pushSpawnLocation(EntityPlayer player) {
		ChunkCoordinates chunkCoord = new ChunkCoordinates(
				MathHelper.floor_double(player.posX),
				MathHelper.floor_double(player.posY),
				MathHelper.floor_double(player.posZ));

		new SpawnList(player).push(new SpawnLocation(player.dimension, chunkCoord));
	}

	public static SpawnLocation popSpawnLocation(EntityPlayer player) {
		return new SpawnList(player).pop();
	}

}
