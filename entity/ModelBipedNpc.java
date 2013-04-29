package takap.mods.nnnpc.entity;

import takap.mods.nnnpc.utility.Utility;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;


public class ModelBipedNpc extends net.minecraft.client.model.ModelBiped implements IModelWithHands
{
    protected final float headRotationPointY = 0f;
    protected final float headWearRotationPointY = 0f;
    protected final float bodyRotationPointY = 0f;
    protected final float armsRotationPointY = 2f;
    protected final float legsRotationPointY = 12f;
    protected final float sittingOffset = 10f;
    
    public ModelBipedNpc()
    {
        this(0.0F);
    }

    public ModelBipedNpc(float f)
    {
        this(f, 0.0F);
    }

    public ModelBipedNpc(float par1, float par2)
    {
        super(par1);
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
//        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        
        EntityNpc npc = (EntityNpc)par7Entity;
        
        this.bipedHead.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.bipedHead.rotateAngleX = par5 / (180F / (float)Math.PI);
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2 * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;
        
        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
        
        if ( (npc.getMode() != null) && (npc.isMode(EnumMode.SIT)) )
        {
            this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.bipedLeftLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
            
            this.bipedHead.rotationPointY = this.headRotationPointY + this.sittingOffset;
            this.bipedHeadwear.rotationPointY = this.headWearRotationPointY + this.sittingOffset;
            this.bipedBody.rotationPointY = this.bodyRotationPointY + this.sittingOffset;
            this.bipedRightArm.rotationPointY = this.armsRotationPointY + this.sittingOffset;
            this.bipedLeftArm.rotationPointY = this.armsRotationPointY + this.sittingOffset;
            this.bipedRightLeg.rotationPointY = this.legsRotationPointY + this.sittingOffset;
            this.bipedLeftLeg.rotationPointY = this.legsRotationPointY + this.sittingOffset;
        }
        else
        {
        	this.bipedHead.rotationPointY = this.headRotationPointY;
        	this.bipedHeadwear.rotationPointY = this.headWearRotationPointY;
        	this.bipedBody.rotationPointY = this.bodyRotationPointY;
        	this.bipedRightArm.rotationPointY = this.armsRotationPointY;
        	this.bipedLeftArm.rotationPointY = this.armsRotationPointY;
        	this.bipedRightLeg.rotationPointY = this.legsRotationPointY;
        	this.bipedLeftLeg.rotationPointY = this.legsRotationPointY;
            
            if ( this.onGround > -9990.0F )
            {
                if ( (npc.getForm() != null) && (npc.getDominantHand().equals(Utility.leftHanded)) )
                {
                    this.bipedBody.rotateAngleY = -MathHelper.sin(MathHelper.sqrt_float(this.onGround) * (float)Math.PI * 2.0F) * 0.2F;
                    this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                    this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                    this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                    this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                    this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                    this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                    this.bipedRightArm.rotateAngleX += this.bipedBody.rotateAngleY;
                    float var8 = 1.0f - this.onGround;
                    var8 *= var8;
                    var8 *= var8;
                    var8 = 1.0F - var8;
                    float var9 = MathHelper.sin(var8 * (float)Math.PI);
                    float var10 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                    this.bipedLeftArm.rotateAngleX = (float)((double)this.bipedLeftArm.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
                    this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                    this.bipedLeftArm.rotateAngleZ = -MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
                }
                else
                {
                    this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(this.onGround) * (float)Math.PI * 2.0F) * 0.2F;
                    this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                    this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                    this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                    this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                    this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                    this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                    this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
                    float var8 = 1.0F - this.onGround;
                    var8 *= var8;
                    var8 *= var8;
                    var8 = 1.0F - var8;
                    float var9 = MathHelper.sin(var8 * (float)Math.PI);
                    float var10 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                    this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
                    this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                    this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
                }
            }
            
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
        }
    }
    
    public void postRenderLeftArm(float par1)
    {
    	this.bipedLeftArm.postRender(par1);
    }

    public void postRenderRightArm(float par1)
    {
    	this.bipedRightArm.postRender(par1);
    }
}
