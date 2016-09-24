package net.torocraft.toroquest.entities.model;

import java.util.Random;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.civilization.CivilizationType;

@SideOnly(Side.CLIENT)
public class ModelGuard extends ModelBiped {

	private ModelRenderer bipedCape;
	private final float modelSize;
	private final float yOffset = 0f;

	private float capeAni = 0;
	private boolean capeAniUp = true;

	private CivilizationType civ;
	private CivilizationType prevCiv;

	private static final float DEFAULT_CAPE_ANGLE = 0.08f;

	public ModelGuard() {
		this(0.0F);
	}

	public ModelGuard(float modelSize) {
		super(modelSize, 0, 64, 64);

		this.modelSize = modelSize;

	}

	public void setCivilization(CivilizationType civ) {
		prevCiv = this.civ;
		this.civ = civ;
	}

	protected CivilizationType randomCivilizationType() {
		Random rand = new Random();

		return CivilizationType.values()[rand.nextInt(CivilizationType.values().length)];
	}

	protected void buildCape() {
		bipedCape = new ModelRenderer(this, 0, 0);
		bipedCape.setTextureSize(64, 64);
		setCapeTexture();
		bipedCape.addBox(-4.5F, 0.0F, 0F, 9, 14, 1, modelSize);
		bipedCape.setRotationPoint(0, 0, 0);

		bipedCape.offsetZ = 0.17f;
		bipedCape.offsetY = +0.05f;
		bipedCape.rotateAngleX = DEFAULT_CAPE_ANGLE;
		bipedBody.addChild(bipedCape);
	}

	protected void setCapeTexture() {
		bipedCape.isHidden = false;
		bipedCape.setTextureOffset(0, 32);
		System.out.println("setCapeTexture()  " + civ);

		if (civ == null) {
			bipedCape.isHidden = true;
			return;
		}

		bipedCape.setTextureSize(64, 64);

		switch (civ) {
		case SUN:
			bipedCape.setTextureOffset(0, 32);
			break;
		case EARTH:
			bipedCape.setTextureOffset(20, 32);
			break;
		case WATER:
			bipedCape.setTextureOffset(40, 32);
			break;
		case WIND:
			bipedCape.setTextureOffset(0, 47);
			break;
		case MOON:
			bipedCape.setTextureOffset(20, 47);
			break;
		case FIRE:
			bipedCape.setTextureOffset(40, 47);
			break;
		default:

			bipedCape.isHidden = true;
			break;
		}
	}

	private static final float MAX_CAPE_ANI = 0.07f;

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		if (civ != null && !civ.equals(prevCiv)) {
			System.out.println("building cape for " + civ);
			buildCape();
		}

		capeAnimation(entityIn);

	}

	protected void capeAnimation(Entity entityIn) {

		if (bipedCape == null) {
			return;
		}

		float speed = (float) (entityIn.motionX * entityIn.motionX + entityIn.motionZ * entityIn.motionZ);

		if (speed < 0.01) {
			bipedCape.rotateAngleX = DEFAULT_CAPE_ANGLE;
			// return;
		}

		float rot = speed * 40;

		if (rot > 0.6f) {
			rot = 0.6f;
		}

		if (capeAniUp && capeAni > MAX_CAPE_ANI) {
			capeAniUp = false;

		} else if (!capeAniUp && capeAni < 0) {
			capeAniUp = true;
		}

		if (capeAniUp) {
			capeAni += 0.00025;
		} else {
			capeAni -= 0.00025;
		}

		rot += capeAni;

		bipedCape.rotateAngleX = rot;
	}

	public void renderCape(float scale) {
		this.bipedCape.render(scale);
	}

}