package openender.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import openender.Config;
import openender.OpenEnder;
import openmods.block.OpenBlock;

public class BlockUnbreakable extends OpenBlock {

	public BlockUnbreakable() {
		super(Config.blockUnbreakableId, Material.rock);
		setCreativeTab(OpenEnder.tabOpenEnder);
		setBlockUnbreakable();
		setResistance(6000000F);
	}
	
	@Override
    public boolean isOpaqueCube() {
        return false;
    }


	@Override
	public boolean shouldRenderBlock() {
		return false;
	}

	@Override
	protected Object getModInstance() {
		return OpenEnder.instance;
	}

	@Override
	public int getRenderType() {
		return OpenEnder.renderId;
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vec, Vec3 vec2) {
		return null;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {}
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		return false;
	}
}
