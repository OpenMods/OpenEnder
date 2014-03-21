package openender.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openender.Config;
import openender.OpenEnder;
import openender.common.EnderTeleporter;
import openender.common.utils.PlayerDataUtils;

public class ItemEnderLocker extends Item {

	public ItemEnderLocker() {
		super(Config.itemEnderLockerId);
		setCreativeTab(OpenEnder.tabOpenEnder);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (player instanceof EntityPlayerMP) {
			int enderId = PlayerDataUtils.getPlayerDimensionId(player);

			EntityPlayerMP thePlayer = (EntityPlayerMP)player;

			EnderTeleporter teleporter = new EnderTeleporter(thePlayer.mcServer.worldServerForDimension(enderId));
			thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, enderId, teleporter);
		}

		return stack;

	}

}
