package openender.common;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;

public class ChunkProviderEnder extends ChunkProviderFlat implements IChunkProvider {

	public ChunkProviderEnder(World par1World, long par2, boolean par4) {
		super(par1World, par2, par4, "2;7,80x1,4,69x0,100x1,7;3;stronghold,dungeon,mineshaft,decoration");
	}

}
