package openender.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class EnderTeleporter extends Teleporter {

	private final WorldServer worldObj;

	public EnderTeleporter(WorldServer world) {
		super(world);
		this.worldObj = world;
	}

	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float r) {
		ChunkCoordinates spawnPoint = worldObj.getSpawnPoint();
		entity.fallDistance = 0;
		entity.setLocationAndAngles(spawnPoint.posX, spawnPoint.posY, spawnPoint.posZ, entity.rotationYaw, 0.0F);
	}

	public static void teleport(EntityPlayer player, int dimensionId) {
		if (player instanceof EntityPlayerMP) {

			EntityPlayerMP playerMP = (EntityPlayerMP)player;

			EnderTeleporter teleporter = new EnderTeleporter(playerMP.mcServer.worldServerForDimension(dimensionId));
			playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, dimensionId, teleporter);

		}
	}
}
