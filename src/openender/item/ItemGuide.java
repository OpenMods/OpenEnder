package openender.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openender.Config;
import openender.OpenEnder;
import openender.common.OpenEnderGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGuide extends Item {

	public ItemGuide() {
		super(Config.itemGuideId);
		setCreativeTab(OpenEnder.tabOpenEnder);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister registry) {
		itemIcon = registry.registerIcon("openender:guide");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote) {
			player.openGui(OpenEnder.instance, OpenEnderGuiHandler.GuiId.guide.ordinal(), world, 0, 0, 0);
		}
		return stack;
	}
}
