package net.torocraft.torobasemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torobasemod.entities.render.RenderMage;
import net.torocraft.torobasemod.entities.render.RenderMonolithEye;
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
    	//ToroBaseModBlocks.init();
    	//ToroBaseModRecipes.init();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		registerRender(Entity.class, new RenderMage(rm));
		registerRender(Entity.class, new RenderMonolithEye(rm));
    }

	private void registerRender(Class<? extends Entity> e, Render<? extends Entity> renderer) {
		RenderingRegistry.registerEntityRenderingHandler(e, renderer);
	}

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

}