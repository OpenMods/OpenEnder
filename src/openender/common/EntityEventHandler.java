package openender.common;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import openender.utils.PlayerDataUtils;

public class EntityEventHandler {

	@ForgeSubscribe
	public void onHurt(LivingHurtEvent e) {

		if (e.entity instanceof EntityPlayerMP && e.source == DamageSource.outOfWorld) {

			EntityPlayerMP player = (EntityPlayerMP)e.entity;

			if (player.dimension == PlayerDataUtils.getPlayerDimensionId(player)) {
				EnderTeleporter.teleport(player, 0);
				e.setCanceled(true);
			}
		}
	}
}
