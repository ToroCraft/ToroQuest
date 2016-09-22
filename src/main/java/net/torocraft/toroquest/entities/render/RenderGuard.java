package net.torocraft.toroquest.entities.render;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.EntityGuard;
import net.torocraft.toroquest.entities.model.ModelGuard;

@SideOnly(Side.CLIENT)
public class RenderGuard extends RenderBiped<EntityGuard> {

	private static final ResourceLocation TEXTURES = new ResourceLocation(ToroQuest.MODID + ":textures/entity/guard/guard.png");

	private final ModelBiped defaultModel;

	/*
	 * private final ModelZombieVillager zombieVillagerModel; private final
	 * List<LayerRenderer<EntityGuard>> villagerLayers;
	 */

	private final List<LayerRenderer<EntityGuard>> defaultLayers;

	public RenderGuard(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelGuard(), 0.5F, 1.0F);

		LayerRenderer<?> layerrenderer = (LayerRenderer) this.layerRenderers.get(0);
		defaultModel = this.modelBipedMain;
		// zombieVillagerModel = new ModelZombieVillager();

		addLayer(new LayerHeldItem(this));

		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				modelLeggings = new ModelZombie(0.5F, true);
				modelArmor = new ModelZombie(1.0F, true);
			}
		};
		addLayer(layerbipedarmor);

		addLayer(new LayerCape(this));

		this.defaultLayers = Lists.newArrayList(this.layerRenderers);

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
	/*
	 * private void renderCape() { GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	 * 
	 * this.bindTexture(TEXTURES);
	 * 
	 * GlStateManager.pushMatrix(); GlStateManager.translate(0.0F, 0.0F,
	 * 0.125F); double d0 = entitylivingbaseIn.prevChasingPosX +
	 * (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) *
	 * (double) partialTicks - (entitylivingbaseIn.prevPosX +
	 * (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)
	 * partialTicks); double d1 = entitylivingbaseIn.prevChasingPosY +
	 * (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) *
	 * (double) partialTicks - (entitylivingbaseIn.prevPosY +
	 * (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)
	 * partialTicks); double d2 = entitylivingbaseIn.prevChasingPosZ +
	 * (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) *
	 * (double) partialTicks - (entitylivingbaseIn.prevPosZ +
	 * (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)
	 * partialTicks); float f = entitylivingbaseIn.prevRenderYawOffset +
	 * (entitylivingbaseIn.renderYawOffset -
	 * entitylivingbaseIn.prevRenderYawOffset) * partialTicks; double d3 =
	 * (double) MathHelper.sin(f * 0.017453292F); double d4 = (double)
	 * (-MathHelper.cos(f * 0.017453292F)); float f1 = (float) d1 * 10.0F; f1 =
	 * MathHelper.clamp_float(f1, -6.0F, 32.0F); float f2 = (float) (d0 * d3 +
	 * d2 * d4) * 100.0F; float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
	 * 
	 * if (f2 < 0.0F) { f2 = 0.0F; }
	 * 
	 * float f4 = entitylivingbaseIn.prevCameraYaw +
	 * (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) *
	 * partialTicks; f1 = f1 +
	 * MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified +
	 * (entitylivingbaseIn.distanceWalkedModified -
	 * entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) *
	 * 32.0F * f4;
	 * 
	 * if (entitylivingbaseIn.isSneaking()) { f1 += 25.0F; }
	 * 
	 * GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
	 * GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
	 * GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
	 * GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
	 * this.playerRenderer.getMainModel().renderCape(0.0625F);
	 * GlStateManager.popMatrix(); }
	 */

	private void setModelVisibilities(EntityGuard clientPlayer) {
		ModelGuard modelplayer = (ModelGuard) this.getMainModel();

		ItemStack itemstack = clientPlayer.getHeldItemMainhand();
		ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
		modelplayer.setInvisible(true);

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
		return TEXTURES;

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