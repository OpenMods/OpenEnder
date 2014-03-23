package openender.gui;

import openender.container.ContainerCipherKey;
import openmods.gui.BaseGuiContainer;
import openmods.gui.component.BaseComponent;
import openmods.gui.component.GuiComponentTextButton;
import openmods.gui.component.IComponentListener;

public class GuiCipherKey extends BaseGuiContainer<ContainerCipherKey> implements IComponentListener {

	private GuiComponentTextButton buttonLock;

	public GuiCipherKey(ContainerCipherKey container) {
		super(container, 176, 167, "item.openender.cipherstone.name");
		root.addComponent((buttonLock = new GuiComponentTextButton(68, 57, 40, 13, 0xFFFFFF))
				.setText("Lock")
				.setName("btnLock")
				.addListener(this));
	}

	@Override
	public void componentMouseDown(BaseComponent component, int offsetX, int offsetY, int button) {
		if (component == buttonLock) {
			sendButtonClick(0);
		}
	}

	@Override
	public void componentMouseDrag(BaseComponent component, int offsetX, int offsetY, int button, long time) {}

	@Override
	public void componentMouseMove(BaseComponent component, int offsetX, int offsetY) {}

	@Override
	public void componentMouseUp(BaseComponent component, int offsetX, int offsetY, int button) {}

	@Override
	public void componentKeyTyped(BaseComponent component, char par1, int par2) {}

}
