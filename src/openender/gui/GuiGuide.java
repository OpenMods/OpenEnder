package openender.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import openender.OpenEnder.Items;
import openender.gui.pages.BlankPage;
import openender.gui.pages.TitledPage;
import openmods.gui.component.GuiComponentBook;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiGuide extends GuiScreen {

	private int centerX;
	private int guiLeft;
	private int guiTop;

	private GuiComponentBook book;

	public GuiGuide() {
		book = new GuiComponentBook();
		book.addPage(new BlankPage());
		book.addPage(new BlankPage());
		book.addPage(new BlankPage());
		book.addPage(new TitledPage("openender.gui.welcome.title", "openender.gui.welcome.content"));
		book.addStandardRecipePage("openender", "enderdimension", Items.enderStone);
		book.addStandardRecipePage("openender", "lockeddimensions", Items.cipherStone);
		book.enablePages();
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		book.mouseClicked(x - this.guiLeft, y - this.guiTop, button);
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int button) {
		super.mouseMovedOrUp(x, y, button);
		book.mouseMovedOrUp(x - this.guiLeft, y - this.guiTop, button);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int button, long time) {
		super.mouseClickMove(mouseX, mouseY, button, time);
		book.mouseClickMove(mouseX - this.guiLeft, mouseY - this.guiTop, button, time);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		super.drawScreen(mouseX, mouseY, par3);
		centerX = this.width / 2;
		guiLeft = centerX - 211;
		guiTop = (height - 200) / 2;

		GL11.glPushMatrix();
		book.render(this.mc, guiLeft, guiTop, mouseX - this.guiLeft, mouseY - this.guiTop);
		GL11.glPopMatrix();

		// second pass
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPushMatrix();
		book.renderOverlay(this.mc, guiLeft, guiTop, mouseX - this.guiLeft, mouseY - this.guiTop);
		GL11.glPopMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}

}
