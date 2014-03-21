package openender.client;

import openender.OpenEnder;
import openmods.renderer.BlockRenderingHandlerBase;

public class BlockRenderingHandler extends BlockRenderingHandlerBase {

	@Override
	public int getRenderId() {
		return OpenEnder.renderId;
	}

}
