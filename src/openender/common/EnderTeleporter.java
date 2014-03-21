package openender.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class EnderTeleporter extends Teleporter {

	public EnderTeleporter(WorldServer world) {
		super(world);
	}

	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float r) {
		entity.setLocationAndAngles(8, 12, 8, entity.rotationYaw, 0.0F);
	}
}
