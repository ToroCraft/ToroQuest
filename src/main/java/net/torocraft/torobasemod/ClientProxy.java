package net.torocraft.torobasemod;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torobasemod.entities.ToroEntities;
import net.torocraft.torobasemod.item.ToroBaseModItems;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ToroBaseModItems.registerRenders();
		ToroEntities.registerRenders();
    }


    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

}