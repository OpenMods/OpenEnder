package openender.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import openender.OpenEnder.Items;
import openmods.container.ContainerBase;

public class ContainerCipherKey extends ContainerBase<Void> {

	private InventoryPlayer playerInventory;

	public ContainerCipherKey(IInventory playerInventory, IInventory ownerInventory) {
		super(playerInventory, ownerInventory, null);
		this.playerInventory = (InventoryPlayer)playerInventory;
		addInventoryGrid(33, 31, 6);
		addPlayerInventorySlots(85);
	}

	public void onButtonClicked(EntityPlayer player, int buttonId) {
		Items.cipherKey.setLocked(playerInventory.getCurrentItem());
		player.closeScreen();
	}

}
