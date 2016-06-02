package net.torocraft.torobasemod.entities.model;


import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.torobasemod.entities.EntityMonolithEye;

@SideOnly(Side.CLIENT)
public class ModelMonolithEye extends ModelBase {
    private ModelRenderer guardianBody;
    private ModelRenderer guardianEye;

    public ModelMonolithEye()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.guardianBody = new ModelRenderer(this);
        this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12, 12, 16);
        this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2, 12, 12);
        this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, 10.0F, -6.0F, 2, 12, 12, true);
        this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12, 2, 12);
        this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12, 2, 12);

        this.guardianEye = new ModelRenderer(this, 8, 0);
        this.guardianEye.addBox(-1.0F, 15.0F, 0.0F, 2, 2, 1);
        this.guardianBody.addChild(this.guardianEye);
    }

    public int func_178706_a()
    {
        return 54;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(p_78088_2_, limbSwing, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.guardianBody.render(scale);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	EntityMonolithEye entityMonolithEye = (EntityMonolithEye)entityIn;
    	this.guardianBody.setRotationPoint(0.0F, 0.0F, 0.0F);
    	this.guardianBody.rotateAngleY = netHeadYaw * 0.017453292F;
        this.guardianBody.rotateAngleX = headPitch * 0.017453292F;

        this.guardianEye.rotationPointZ = -8.25F;
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();

        if (entityMonolithEye.hasTargetedEntity())
        {
            entity = entityMonolithEye.getTargetedEntity();
        }

        if (entity != null)
        {
            Vec3d vec3d = entity.getPositionEyes(0.0F);
            Vec3d vec3d1 = entityIn.getPositionEyes(0.0F);
            double d0 = vec3d.yCoord - vec3d1.yCoord;

            if (d0 > 0.0D)
            {
                this.guardianEye.rotationPointY = 0.0F;
            }
            else
            {
                this.guardianEye.rotationPointY = 1.0F;
            }

            Vec3d vec3d2 = entityIn.getLook(0.0F);
            vec3d2 = new Vec3d(vec3d2.xCoord, 0.0D, vec3d2.zCoord);
            Vec3d vec3d3 = (new Vec3d(vec3d1.xCoord - vec3d.xCoord, 0.0D, vec3d1.zCoord - vec3d.zCoord)).normalize().rotateYaw(((float)Math.PI / 2F));
            double d1 = vec3d2.dotProduct(vec3d3);
            this.guardianEye.rotationPointX = MathHelper.sqrt_float((float)Math.abs(d1)) * 2.0F * (float)Math.signum(d1);
        }

        this.guardianEye.showModel = true;
    }
}