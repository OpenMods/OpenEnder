package openender;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import openender.block.BlockUnbreakable;
import openender.item.ItemEnderLocker;
import openmods.OpenMods;
import openmods.api.IProxy;
import openmods.config.ConfigProcessing;
import openmods.config.RegisterBlock;
import openmods.config.RegisterItem;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCIES)
@NetworkMod(serverSideRequired = true, clientSideRequired = true)
public class OpenEnder {

	@Instance(value = ModInfo.ID)
	public static OpenEnder instance;

	@SidedProxy(clientSide = ModInfo.PROXY_CLIENT, serverSide = ModInfo.PROXY_SERVER)
	public static IProxy proxy;

	public static class Blocks {

		@RegisterBlock(name = "unbreakable")
		public static BlockUnbreakable unbreakable;
	}

	public static class Items {
		@RegisterItem(name = "enderlocker")
		public static ItemEnderLocker enderLocker;
	}

	public static CreativeTabs tabOpenEnder = new CreativeTabs("tabOpenEnder") {
		@Override
		public ItemStack getIconItemStack() {
			return new ItemStack(Block.enderChest);
		}
	};

	public static int renderId;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {

		final File configFile = evt.getSuggestedConfigurationFile();
		Configuration config = new Configuration(configFile);
		ConfigProcessing.processAnnotations(configFile, "OpenEnder", config, Config.class);

		if (config.hasChanged()) config.save();
		Config.register();

		NetworkRegistry.instance().registerGuiHandler(instance, OpenMods.proxy.wrapHandler(null));

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt) {
		proxy.init();
		proxy.registerRenderInformation();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		proxy.postInit();
	}
}