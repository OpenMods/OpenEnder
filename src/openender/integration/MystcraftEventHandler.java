package openender.integration;

import net.minecraftforge.event.ForgeSubscribe;
import openender.utils.WorldUtils;

import com.xcompwiz.mystcraft.api.linking.LinkEvent.LinkEventAllow;

public class MystcraftEventHandler {

	@ForgeSubscribe
	public void onLinkEventAllow(LinkEventAllow evt) {

		if (WorldUtils.isEnderDimension(evt.origin) ||
				WorldUtils.isEnderDimension(evt.options.getDimensionUID())) {
			evt.setCanceled(true);
		}
	}

}
