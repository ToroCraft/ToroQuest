package net.torocraft.fishing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.fishing.enchantments.EnchantmentJuicy;
import net.torocraft.fishing.events.EventHooks;
import net.torocraft.fishing.items.Worms;

@Mod(modid=FishingMod.MODID, name=FishingMod.MODNAME, version=FishingMod.VERSION)
public class FishingMod {

	public static final String MODID = "fishingmod";
	public static final String MODNAME = "Fishing Mod";
	public static final String VERSION = "0.0.0";
	
	@Instance(value = FishingMod.MODID)
	public static FishingMod instance;
	
	public static Item worms;
	
	public static final EntityEquipmentSlot[] wormSlots = {EntityEquipmentSlot.OFFHAND};
	public static final Enchantment juicy = new EnchantmentJuicy(Rarity.COMMON, 
			EnumEnchantmentType.WEAPON, wormSlots);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		worms = new Worms();
		GameRegistry.registerItem(worms, Worms.NAME);
		
		//ModelResourceLocation wormsModel = new ModelResourceLocation("fishingmod:worms", "inventory");
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(worms, 0, wormsModel);
		
		if(event.getSide() == Side.CLIENT){
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			renderItem.getItemModelMesher().register(worms, 0, new ModelResourceLocation(FishingMod.MODID + ":" + Worms.NAME, "inventory"));
		}
		
		Enchantment.enchantmentRegistry.register(300, new ResourceLocation(FishingMod.MODID + ":" + EnchantmentJuicy.NAME), juicy);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHooks());
	}
	
}
