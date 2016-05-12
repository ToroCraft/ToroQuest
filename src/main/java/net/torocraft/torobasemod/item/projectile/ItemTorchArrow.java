package net.torocraft.torobasemod.item.projectile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.torocraft.torobasemod.ToroBaseMod;
import net.torocraft.torobasemod.entity.projectile.EntityTorchArrow;
import net.torocraft.torobasemod.util.ToroBaseUtils;

public class ItemTorchArrow extends ItemSpectralArrow {
	
	public static final String NAME = "torch_arrow";
	
	public static ItemTorchArrow torchArrow;
	
	public ItemTorchArrow(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super();
		this.setUnlocalizedName(unlocalizedName);
	}
	
	public static void init() {
		torchArrow = new ItemTorchArrow(NAME, 1, EntityEquipmentSlot.OFFHAND);
		ToroBaseUtils.registerItem(torchArrow, NAME);
	}

	public static void registerRenders() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(torchArrow, 0, new ModelResourceLocation(ToroBaseMod.MODID + ":" + NAME, "inventory"));
	}
	
	public EntityArrow makeTippedArrow(World worldIn, ItemStack itemStack, EntityLivingBase shooter) {
		return new EntityTorchArrow(worldIn, shooter);
	}
	
}
