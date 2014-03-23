package openender.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import openender.Config;
import openender.OpenEnder;
import openender.common.OpenEnderGuiHandler;
import openmods.utils.BlockUtils;
import openmods.utils.PlayerUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGuide extends Item {

	public static final String GIVEN_GUIDE_TAG = "givenGuide";

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

	public static void giveGuideIfRequired(Entity entity) {

		if (entity instanceof EntityPlayer && !entity.worldObj.isRemote) {

			EntityPlayer player = (EntityPlayer)entity;

			NBTTagCompound persistTag = PlayerUtils.getModPlayerPersistTag(player, "OpenEnder");

			boolean shouldGiveGuide = Config.giveGuideOnJoin &&
					OpenEnder.Items.guide != null &&
					!persistTag.getBoolean(GIVEN_GUIDE_TAG);

			if (shouldGiveGuide) {
				ItemStack guide = new ItemStack(OpenEnder.Items.guide);
				if (!player.inventory.addItemStackToInventory(guide)) {
					BlockUtils.dropItemStackInWorld(player.worldObj, player.posX, player.posY, player.posZ, guide);
				}
				persistTag.setBoolean(GIVEN_GUIDE_TAG, true);
			}

		}
	}
}
