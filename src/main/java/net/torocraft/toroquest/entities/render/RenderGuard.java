package net.torocraft.toroquest.entities.render;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.entities.EntityGuard;
import net.torocraft.toroquest.entities.model.ModelGuard;

@SideOnly(Side.CLIENT)
public class RenderGuard extends RenderBiped<EntityGuard> {

	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");

	private final ModelBiped defaultModel;
	private final ModelZombieVillager zombieVillagerModel;
	private final List<LayerRenderer<EntityGuard>> villagerLayers;
	private final List<LayerRenderer<EntityGuard>> defaultLayers;

	public RenderGuard(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelGuard(), 0.5F, 1.0F);
		LayerRenderer<?> layerrenderer = (LayerRenderer) this.layerRenderers.get(0);
		this.defaultModel = this.modelBipedMain;
		this.zombieVillagerModel = new ModelZombieVillager();
		this.addLayer(new LayerHeldItem(this));
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelLeggings = new ModelZombie(0.5F, true);
				this.modelArmor = new ModelZombie(1.0F, true);
			}
		};
		this.addLayer(layerbipedarmor);
		this.defaultLayers = Lists.newArrayList(this.layerRenderers);

		if (layerrenderer instanceof LayerCustomHead) {
			this.removeLayer(layerrenderer);
			this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
		}

		this.removeLayer(layerbipedarmor);
		this.addLayer(new LayerVillagerArmor(this));
		this.villagerLayers = Lists.newArrayList(this.layerRenderers);
	}

	/**
	 * Allows the render to do state modifications necessary before the model is
	 * rendered.
	 */
	protected void preRenderCallback(EntityGuard entitylivingbaseIn, float partialTickTime) {

		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityGuard entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.swapArmor(entity);
		this.setModelVisibilities(entity);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	private void setModelVisibilities(EntityGuard clientPlayer) {
		ModelGuard modelplayer = (ModelGuard) this.getMainModel();

		ItemStack itemstack = clientPlayer.getHeldItemMainhand();
		ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
		modelplayer.setInvisible(true);

		// modelplayer.bipedHeadwear.showModel =
		// clientPlayer.isWearing(EnumPlayerModelParts.HAT);
		/*
		 * modelplayer.bipedBodyWear.showModel =
		 * clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
		 * modelplayer.bipedLeftLegwear.showModel =
		 * clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
		 * modelplayer.bipedRightLegwear.showModel =
		 * clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
		 * modelplayer.bipedLeftArmwear.showModel =
		 * clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
		 * modelplayer.bipedRightArmwear.showModel =
		 * clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
		 */
		modelplayer.isSneak = clientPlayer.isSneaking();
		ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
		ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

		if (itemstack != null) {
			modelbiped$armpose = ModelBiped.ArmPose.ITEM;

			if (clientPlayer.getItemInUseCount() > 0) {
				EnumAction enumaction = itemstack.getItemUseAction();

				if (enumaction == EnumAction.BLOCK) {
					modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
				} else if (enumaction == EnumAction.BOW) {
					modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
				}
			}
		}

		if (itemstack1 != null) {
			modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

			if (clientPlayer.getItemInUseCount() > 0) {
				EnumAction enumaction1 = itemstack1.getItemUseAction();

				if (enumaction1 == EnumAction.BLOCK) {
					modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
				}
			}
		}

		if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
			modelplayer.rightArmPose = modelbiped$armpose;
			modelplayer.leftArmPose = modelbiped$armpose1;
		} else {
			modelplayer.rightArmPose = modelbiped$armpose1;
			modelplayer.leftArmPose = modelbiped$armpose;
		}

	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityGuard entity) {
		return ZOMBIE_TEXTURES;

	}

	private void swapArmor(EntityGuard zombie) {

		this.mainModel = this.defaultModel;
		this.layerRenderers = this.defaultLayers;

		this.modelBipedMain = (ModelBiped) this.mainModel;
	}

	protected void rotateCorpse(EntityGuard entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {

		super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
	}
}