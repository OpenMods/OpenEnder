package openender.common;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import openender.utils.PlayerDataManager;
import openender.utils.PlayerDataManager.SpawnLocation;

public class EntityEventHandler {

	@ForgeSubscribe
	public void onHurt(LivingHurtEvent e) {

		if (e.entity instanceof EntityPlayerMP && e.source == DamageSource.outOfWorld) {
			EntityPlayerMP player = (EntityPlayerMP)e.entity;
			final int privateDimension = DimensionDataManager.instance.getDimensionForPlayer(player.username);

			if (player.dimension == privateDimension) {
				SpawnLocation location = PlayerDataManager.popSpawnLocation(player);
				if (location != null) {
					EnderTeleporter.teleport(player, location.dimensionId, location.position);
				} else {
					EnderTeleporter.teleport(player, 0, player.getBedLocation(0));
				}
				e.setCanceled(true);
			}
		}
	}
}
