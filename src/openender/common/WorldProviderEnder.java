package openender.common;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderEnder extends WorldProvider {

	@Override
	public String getDimensionName() {
		return "Ender";
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderEnder(worldObj);
	}

	@Override
	public boolean canRespawnHere() {
		return false;
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	@Override
	public boolean canDoLightning(Chunk chunk) {
		return false;
	}

	@Override
	public boolean isBlockHighHumidity(int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isDaytime() {
		return false;
	}

	@Override
	public ChunkCoordinates getSpawnPoint() {
		return new ChunkCoordinates(8, 14, 8);
	}
}
