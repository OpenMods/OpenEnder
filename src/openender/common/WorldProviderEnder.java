package openender.common;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderEnder extends WorldProvider {

	@Override
	public String getDimensionName() {
		return "Ender";
	}

	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderEnder(worldObj);
	}

}
