package net.torocraft.toroquest.entities.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.torocraft.toroquest.entities.EntityFriendlyMage;
import net.torocraft.toroquest.entities.EntityMage;
import net.torocraft.toroquest.entities.EntityMonolithEye;
import net.torocraft.toroquest.entities.EntitySentry;

public class ToroEntityRenders {

	public static void init() {
		registerMageRenderer();
		registerFriendlyMageRenderer();
		registerMonolithEyeRenderer();
		registerSentryRenderer();
	}
	
	public static void registerMageRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMage.class, new IRenderFactory<EntityMage>() {
			@Override 
			public Render<EntityMage> createRenderFor(RenderManager manager) { 
				return new RenderMage(manager); 
			}
		});
	}
	
	public static void registerFriendlyMageRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFriendlyMage.class, new IRenderFactory<EntityFriendlyMage>() {
			@Override 
			public Render<EntityFriendlyMage> createRenderFor(RenderManager manager) { 
				return new RenderFriendlyMage(manager); 
			}
		});
	}
	
	public static void registerMonolithEyeRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMonolithEye.class, new IRenderFactory<EntityMonolithEye>() {
			@Override 
			public Render<EntityMonolithEye> createRenderFor(RenderManager manager) { 
				return new RenderMonolithEye(manager); 
			}
		});
	}
	
	public static void registerSentryRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySentry.class, new IRenderFactory<EntitySentry>() {
			@Override 
			public Render<EntitySentry> createRenderFor(RenderManager manager) { 
				return new RenderSentry(manager); 
			}
		});
	}
}
