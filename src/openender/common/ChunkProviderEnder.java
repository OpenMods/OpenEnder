package openender.common;

import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderFlat;

public class ChunkProviderEnder implements IChunkProvider {

	private World world;
	
	public ChunkProviderEnder(World world) {
		this.world = world;
	}

	@Override
	public boolean chunkExists(int i, int j) {
		return true;
	}

	@Override
	public Chunk provideChunk(int x, int z) {
		
        Chunk chunk = new Chunk(world, x, z);

        for (int y = 0; y < 255; ++y) {
        	
            int l = y >> 4;
        
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];

            if (extendedblockstorage == null) {
                extendedblockstorage = new ExtendedBlockStorage(y, !world.provider.hasNoSky);
                chunk.getBlockStorageArray()[l] = extendedblockstorage;
            }

            for (int _x = 0; _x < 16; ++_x) {
                for (int _z = 0; _z < 16; ++_z)  {
                    extendedblockstorage.setExtBlockID(_x, y & 15, _z, y < 10 ? 1 : 0);
                    extendedblockstorage.setExtBlockMetadata(_x, y & 15, _z, 0);
                }
            }
        }
        
        return chunk;
	}

	@Override
	public Chunk loadChunk(int x, int z) {
		return provideChunk(x, z);
	}

	@Override
	public void populate(IChunkProvider ichunkprovider, int i, int j) {

	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
        return "EnderSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k) {
		return null;
	}

	@Override
	public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k) {
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int i, int j) {}

	@Override
	public void saveExtraData() {}

}
