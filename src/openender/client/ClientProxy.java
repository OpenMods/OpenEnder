package openender.client;

import openblocks.client.renderer.BlockRenderingHandler;
import openender.IOpenEnderProxy;
import openender.OpenEnder;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IOpenEnderProxy {

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void registerRenderInformation() {
		OpenEnder.renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());
	}

}
