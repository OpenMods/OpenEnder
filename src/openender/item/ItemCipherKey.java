package openender.item;

import openender.Config;
import openender.OpenEnder;
import openender.common.DimensionDataManager;
import openender.common.EnderTeleporter;
import openender.common.OpenEnderGuiHandler;
import openender.utils.PlayerDataManager;
import openmods.utils.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemCipherKey extends Item {

	public static final String TAG_LOCKED = "locked";
	
	public ItemCipherKey() {
		super(Config.itemCipherKeyId);
		setCreativeTab(OpenEnder.tabOpenEnder);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		
		boolean locked = isLocked(stack);
		
		if (!world.isRemote) {
			if (!locked) {
				player.openGui(
						OpenEnder.instance,
						OpenEnderGuiHandler.GuiId.cipherKey.ordinal(),
						player.worldObj,
						player.inventory.currentItem,
						0, 0
				);
			}
		}
		
		if (locked) {
			if (ItemEnderLocker.canPlayerTeleport(world, player)) {
				final int lockedWorldId = DimensionDataManager.instance.getDimensionForKey(stack);
				if (lockedWorldId != world.provider.dimensionId) {
					PlayerDataManager.pushSpawnLocation(player);
					EnderTeleporter.teleport(player, lockedWorldId);
				}
			}
		}
		
		return stack;
	}

	public boolean isLocked(ItemStack currentItem) {
		if (currentItem == null) return false;
		NBTTagCompound tag = ItemUtils.getItemTag(currentItem);
		return tag.getBoolean(TAG_LOCKED);
	}
	
	public void setLocked(ItemStack currentItem) {
		if (currentItem == null) return;
		NBTTagCompound tag = ItemUtils.getItemTag(currentItem);
		tag.setBoolean(TAG_LOCKED, true);
	}

}
