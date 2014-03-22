package openender.common;

import openblocks.OpenBlocksGuiHandler.GuiId;
import openender.container.ContainerCipherKey;
import openender.gui.GuiCipherKey;
import openmods.GenericInventory;
import openmods.Log;
import openmods.api.IInventoryCallback;
import openmods.utils.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class OpenEnderGuiHandler implements IGuiHandler {
	
	public static enum GuiId {
		cipherKey;
		public static final GuiId[] VALUES = GuiId.values();
	}
	
	private static GuiId getGuiId(int id) {
		try {
			return GuiId.VALUES[id];
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.warn("Invalid GUI id: %d", id);
			return null;
		}
	}
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer _player, World world, int x, int y, int z) {

		final GuiId guiId = getGuiId(id);
		if (guiId == null) return null;
		
		switch (guiId) {
			case cipherKey:
				
				final int slot = x;
				final EntityPlayer player = _player;
				
				final ItemStack stack = player.inventory.getStackInSlot(x);
				
				final GenericInventory inventory = new GenericInventory("", false, 6);
				final NBTTagCompound tag = ItemUtils.getItemTag(stack);
				
				inventory.readFromNBT(tag);
				
				inventory.addCallback(new IInventoryCallback() {
					@Override
					public void onInventoryChanged(IInventory inv, int slotNumber) {
						inventory.writeToNBT(tag);
						stack.setTagCompound(tag);
						player.inventory.setInventorySlotContents(slot, stack);
					}
				});
				
				return new ContainerCipherKey(player.inventory, inventory);
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		final GuiId guiId = getGuiId(id);
		if (guiId == null) return null;
		

		switch (guiId) {
			case cipherKey:
				return new GuiCipherKey((ContainerCipherKey)getServerGuiElement(id, player, world, x, y, z));
			default:
				return null;
		}
	}

}
