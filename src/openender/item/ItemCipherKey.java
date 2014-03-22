package openender.item;

import openender.Config;
import openender.OpenEnder;
import openender.common.OpenEnderGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCipherKey extends Item {

	public ItemCipherKey() {
		super(Config.itemCipherKeyId);
		setCreativeTab(OpenEnder.tabOpenEnder);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		
		if (!world.isRemote) {
			player.openGui(
					OpenEnder.instance,
					OpenEnderGuiHandler.GuiId.cipherKey.ordinal(),
					player.worldObj,
					player.inventory.currentItem,
					0, 0
			);
		}
		
		return stack;
	}

}
