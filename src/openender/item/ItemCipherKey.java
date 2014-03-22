package openender.item;

import openender.Config;
import openender.OpenEnder;
import openender.common.DimensionDataManager;
import openender.common.EnderTeleporter;
import openender.common.OpenEnderGuiHandler;
import openender.utils.PlayerDataManager;
import openmods.GenericInventory;
import openmods.api.IInventoryCallback;
import openmods.utils.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemCipherKey extends Item {

	public static final String TAG_LOCKED = "locked";
	public static final String TAG_INVENTORY = "inventory";
	
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
			if (ItemEnderKey.canPlayerTeleport(world, player)) {
				NBTTagCompound inventoryTag = getInventoryTag(stack);
				final int lockedWorldId = DimensionDataManager.instance.getDimensionForKey(inventoryTag);
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
	
	public NBTTagCompound getInventoryTag(ItemStack stack) {
		final NBTTagCompound tag = ItemUtils.getItemTag(stack);
		final NBTTagCompound inventoryTag = tag.getCompoundTag(TAG_INVENTORY);
		return inventoryTag;
	}

	public IInventory getItemInventory(final EntityPlayer player) {

		final int slot = player.inventory.currentItem;
		final ItemStack stack = player.inventory.getStackInSlot(slot);
		
		final NBTTagCompound tag = ItemUtils.getItemTag(stack);
		final NBTTagCompound inventoryTag = getInventoryTag(stack);
		
		final GenericInventory inventory = new GenericInventory("", false, 6);
		inventory.readFromNBT(inventoryTag);
		
		inventory.addCallback(new IInventoryCallback() {
			@Override
			public void onInventoryChanged(IInventory inv, int slotNumber) {
				inventory.writeToNBT(inventoryTag);
				tag.setTag(TAG_INVENTORY, inventoryTag);
				stack.setTagCompound(tag);
				player.inventory.setInventorySlotContents(slot, stack);
			}
		});
		
		return inventory;
	}

}
