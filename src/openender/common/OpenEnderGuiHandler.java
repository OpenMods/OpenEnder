package openender.common;

import openblocks.OpenBlocksGuiHandler.GuiId;
import openender.OpenEnder;
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
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		final GuiId guiId = getGuiId(id);
		if (guiId == null) return null;
		
		switch (guiId) {
			case cipherKey:
				IInventory inventory = OpenEnder.Items.cipherKey.getItemInventory(player);
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
