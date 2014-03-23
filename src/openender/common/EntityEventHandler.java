package openender.common;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import openender.item.ItemGuide;
import openender.utils.PlayerDataManager;
import openender.utils.PlayerDataManager.SpawnLocation;

public class EntityEventHandler {

	@ForgeSubscribe
	public void onHurt(LivingHurtEvent e) {

		if (e.entity instanceof EntityPlayerMP && e.source == DamageSource.outOfWorld) {

			EntityPlayerMP player = (EntityPlayerMP)e.entity;

			if (player.worldObj.provider instanceof WorldProviderEnder) {

				int dimensionId = 0;
				ChunkCoordinates position = player.getBedLocation(0);

				SpawnLocation location = PlayerDataManager.popSpawnLocation(player);

				if (location != null) {
					dimensionId = location.dimensionId;
					position = location.position;
				}

				EnderTeleporter.teleport(player, dimensionId, position);

				e.setCanceled(true);

			}
		}
	}

	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		ItemGuide.giveGuideIfRequired(event.entity);
	}
}
