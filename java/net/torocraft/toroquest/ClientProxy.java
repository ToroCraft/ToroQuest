package net.torocraft.toroquest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.toroquest.block.ToroQuestBlocks;
import net.torocraft.toroquest.civilization.CivilizationClientHandlers;
import net.torocraft.toroquest.entities.render.ToroQuestEntityRenders;
import net.torocraft.toroquest.item.ToroQuestItems;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ToroQuestEntityRenders.init();
		MinecraftForge.EVENT_BUS.register(new CivilizationClientHandlers());
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		ToroQuestItems.registerRenders();
		ToroQuestBlocks.registerRenders();
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

}