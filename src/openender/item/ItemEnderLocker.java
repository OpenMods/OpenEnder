package openender.item;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import openender.Config;
import openender.OpenEnder;
import openender.common.DimensionDataManager;
import openender.common.EnderTeleporter;
import openender.utils.PlayerDataManager;
import openmods.utils.Coord;
import openmods.world.StructureRegistry;

public class ItemEnderLocker extends Item {

	public ItemEnderLocker() {
		super(Config.itemEnderLockerId);
		setCreativeTab(OpenEnder.tabOpenEnder);
	}

	private static boolean canPlayerTeleport(World world, EntityPlayer player) {
		if (player instanceof EntityPlayerMP && world instanceof WorldServer) {
			if (player.capabilities.isCreativeMode) return true;
			Coord playerPos = new Coord(player.posX, player.posY, player.posZ);
			Set<ChunkPosition> coords = StructureRegistry.instance.getNearestInstance("Temple", (WorldServer)world, playerPos.x, playerPos.y, playerPos.z);

			final int rangeSq = Config.keyRange * Config.keyRange;

			for (ChunkPosition chunkPos : coords) {
				Coord tmp = new Coord(chunkPos);
				final int distSq = tmp.substract(playerPos).lengthSq();
				if (distSq < rangeSq) return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

		if (canPlayerTeleport(world, player)) {
			final int privateWorldId = DimensionDataManager.instance.getDimensionForPlayer(player.username);
			if (privateWorldId != world.provider.dimensionId) {
				PlayerDataManager.pushSpawnLocation(player);
				EnderTeleporter.teleport(player, privateWorldId);
			}
		}

		return stack;

	}
}
