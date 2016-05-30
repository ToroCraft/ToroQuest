package net.torocraft.torobasemod;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.torobasemod.block.ToroBaseModBlocks;
import net.torocraft.torobasemod.crafting.ToroBaseModRecipes;
import net.torocraft.torobasemod.entities.EntityMage;
import net.torocraft.torobasemod.generation.WorldGenMageTowerPlacer;
import net.torocraft.torobasemod.item.ToroBaseModItems;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {

    }

    public void init(FMLInitializationEvent e) {
        ToroBaseModItems.init();
    	ToroBaseModBlocks.init();
    	ToroBaseModRecipes.init();
		WorldGenMageTowerPlacer.init();
		int id = 50;
		registerEntity(EntityMage.class, "Mage", id++);
    }

	private void registerEntity(Class<? extends Entity> entity, String name, int id) {
		EntityRegistry.registerModEntity(entity, name, id, ToroBaseMod.instance, 40, 2, true);
	}

    public void postInit(FMLPostInitializationEvent e) {

    }
}

