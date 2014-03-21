package openender.common;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class EnderTeleporter extends Teleporter {

	private final WorldServer worldServerInstance;

	public EnderTeleporter(WorldServer world) {
		super(world);
		worldServerInstance = world;
	}

	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float r) {
		placeInExistingPortal(entity, x, y, z, r);
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float r) {
		entity.setLocationAndAngles(0, 50, 0, entity.rotationYaw, 0.0F);
		return true;
	}

	@Override
	public boolean makePortal(Entity entity) {
		entity.setLocationAndAngles(0, 50, 0, entity.rotationYaw, 0.0F);
		return true;
	}
}
