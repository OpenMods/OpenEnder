package openender.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import openender.item.ItemGuide;
import openender.utils.PlayerDataManager;
import openender.utils.PlayerDataManager.SpawnLocation;
import openender.utils.WorldUtils;

public class EntityEventHandler {

	/**
	 * If the user falls into the void in an ender dimension, we remove them
	 * from the
	 * world and make sure they don't get hurt.
	 */
	@ForgeSubscribe
	public void onHurt(LivingHurtEvent e) {

		if (e.entity instanceof EntityPlayerMP && e.source == DamageSource.outOfWorld) {

			EntityPlayerMP player = (EntityPlayerMP)e.entity;

			if (WorldUtils.isEnderDimension(player.worldObj)) {
				removePlayerFromDimension(player);
				e.setCanceled(true);
			}
		}
	}

	/**
	 * If a user joins the world, give them a guide if they require once.
	 * If it's an ender dimension with an owner, but the owner isn't our player
	 * we send them back to the overworld
	 */
	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {

		if (!event.world.isRemote && event.entity instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer)event.entity;
			ItemGuide.giveGuideIfRequired(player);

			final World world = event.world;

			if (WorldUtils.isEnderDimension(world)) {

				final String owner = DimensionDataManager.instance.getDimensionOwner(world.provider.dimensionId);

				if (owner != null && player.username.equals(owner)) {
					event.setCanceled(true);
				}
			}
		}
	}

	private static void removePlayerFromDimension(EntityPlayer player) {
		SpawnLocation location = PlayerDataManager.popSpawnLocation(player);

		int dimensionId;
		ChunkCoordinates position;
		if (location != null) {
			dimensionId = location.dimensionId;
			position = location.position;
		} else {
			dimensionId = 0;
			position = player.getBedLocation(0);
		}

		EnderTeleporter.teleport(player, dimensionId, position);
	}
}
