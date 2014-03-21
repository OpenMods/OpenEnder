package openender.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class EnderTeleporter extends Teleporter {

	private final ChunkCoordinates coords;
	private final WorldServer worldObj;

	public EnderTeleporter(WorldServer world, ChunkCoordinates coords) {
		super(world);
		if (coords == null) {
			coords = world.getSpawnPoint();
		}
		this.coords = coords;
		this.worldObj = world;
	}

	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float r) {
		entity.fallDistance = 0;
		entity.setLocationAndAngles(coords.posX, coords.posY, coords.posZ, entity.rotationYaw, 0.0F);
	}

	public static void teleport(EntityPlayer player, int dimensionId) {
		teleport(player, dimensionId, null);
	}

	public static void teleport(EntityPlayer player, int dimensionId, ChunkCoordinates location) {

		if (player instanceof EntityPlayerMP) {

			EntityPlayerMP playerMP = (EntityPlayerMP)player;

			EnderTeleporter teleporter = new EnderTeleporter(playerMP.mcServer.worldServerForDimension(dimensionId), location);
			playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, dimensionId, teleporter);

		}
	}
}
