package openender.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import openmods.container.ContainerBase;

public class ContainerCipherKey extends ContainerBase<Void> {

	public ContainerCipherKey(IInventory playerInventory, IInventory ownerInventory) {
		super(playerInventory, ownerInventory, null);
		addInventoryGrid(33, 31, 6);
		addPlayerInventorySlots(85);
	}

}
