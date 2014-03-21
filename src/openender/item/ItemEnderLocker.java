package openender.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openender.Config;
import openender.OpenEnder;
import openender.common.DimensionDataManager;
import openender.common.EnderTeleporter;

public class ItemEnderLocker extends Item {

	public ItemEnderLocker() {
		super(Config.itemEnderLockerId);
		setCreativeTab(OpenEnder.tabOpenEnder);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

		if (player instanceof EntityPlayerMP) {
			int enderId = DimensionDataManager.instance.getDimensionForPlayer(player.username);
			EnderTeleporter.teleport(player, enderId);
		}

		return stack;

	}

}
