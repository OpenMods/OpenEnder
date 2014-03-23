package openender;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import openender.block.BlockUnbreakable;
import openender.common.DimensionDataManager;
import openender.common.EntityEventHandler;
import openender.common.OpenEnderGuiHandler;
import openender.item.ItemCipherKey;
import openender.item.ItemEnderKey;
import openender.item.ItemGuide;
import openmods.OpenMods;
import openmods.api.IProxy;
import openmods.config.ConfigProcessing;
import openmods.config.RegisterBlock;
import openmods.config.RegisterItem;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
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
		@RegisterItem(name = "enderstone")
		public static ItemEnderKey enderKey;

		@RegisterItem(name = "cipherkey")
		public static ItemCipherKey cipherKey;

		@RegisterItem(name = "guide")
		public static ItemGuide guide;
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

		NetworkRegistry.instance().registerGuiHandler(instance, OpenMods.proxy.wrapHandler(new OpenEnderGuiHandler()));

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt) {

		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());

		proxy.init();
		proxy.registerRenderInformation();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		proxy.postInit();
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent evt) {
		DimensionDataManager.instance.onServerStart(evt.getServer());
	}

	@EventHandler
	public void onServerStop(FMLServerStoppedEvent evt) {
		DimensionDataManager.instance.onServerStop();
	}
}