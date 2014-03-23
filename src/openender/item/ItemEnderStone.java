package openender.item;

import java.util.Set;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import openender.Config;
import openender.OpenEnder;
import openender.common.DimensionDataManager;
import openender.common.EnderTeleporter;
import openender.utils.PlayerDataManager;
import openender.utils.WorldUtils;
import openmods.utils.Coord;
import openmods.world.StructureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEnderStone extends Item {

	public ItemEnderStone() {
		super(Config.itemEnderStoneId);
		setCreativeTab(OpenEnder.tabOpenEnder);
	}

	public static boolean canPlayerTeleport(World world, EntityPlayer player) {
		if (player instanceof EntityPlayerMP && world instanceof WorldServer) {
			if (player.capabilities.isCreativeMode) return true;

			int currentProvider = DimensionManager.getProviderType(world.provider.dimensionId);
			if (currentProvider == Config.enderDimensionProviderId) return true;

			Coord playerPos = new Coord(player.posX, player.posY, player.posZ);
			Set<ChunkPosition> coords = StructureRegistry.instance.getNearestInstance("Temple", (WorldServer)world, playerPos.x, playerPos.y, playerPos.z);

			final int rangeSq = Config.stoneRange * Config.stoneRange;

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
				WorldUtils.strikeAreaAroundPlayer(world, player, 10, 2);
				EnderTeleporter.teleport(player, privateWorldId);
			}
		}

		return stack;

	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean held) {
		if (world.isRemote && held) {
			world.spawnParticle("portal", entity.posX, entity.posY, entity.posZ, (world.rand.nextDouble() - 0.5) * 2, world.rand.nextDouble() - 1.0, (world.rand.nextDouble() - 0.5) * 2);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister registry) {
		itemIcon = registry.registerIcon("openender:enderstone");
	}
}
