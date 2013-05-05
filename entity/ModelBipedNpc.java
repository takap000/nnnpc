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
        EntityNpc npc = (EntityNpc)par7Entity;
        
        this.bipedHead.rotateAngleY = par4 / (180f / (float)Math.PI);
        this.bipedHead.rotateAngleX = par5 / (180f / (float)Math.PI);
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662f + (float)Math.PI) * 2.0f * par2 * 0.5f;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662f) * 2.0f * par2 * 0.5f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662f) * 1.4f * par2;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662f + (float)Math.PI) * 1.4f * par2;
        this.bipedRightLeg.rotateAngleY = 0.0f;
        this.bipedLeftLeg.rotateAngleY = 0.0f;
        
        this.bipedRightArm.rotateAngleY = 0.0f;
        this.bipedLeftArm.rotateAngleY = 0.0f;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09f) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09f) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067f) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067f) * 0.05f;
        
        if ( (npc.getMode() != null) && (npc.isMode(EnumMode.SIT)) )
        {
            this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5f);
            this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5f);
            this.bipedRightLeg.rotateAngleX = -((float)Math.PI * 2f / 5f);
            this.bipedLeftLeg.rotateAngleX = -((float)Math.PI * 2f / 5f);
            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10f);
            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10f);
            
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
            
            if ( this.onGround > -9990.0f )
            {
                if ( (npc.getForm() != null) && (npc.getDominantHand().equals(Utility.leftHanded)) )
                {
                    this.bipedBody.rotateAngleY = -MathHelper.sin(MathHelper.sqrt_float(this.onGround) * (float)Math.PI * 2.0f) * 0.2f;
                    this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
                    this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
                    this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
                    this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
                    this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                    this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                    this.bipedRightArm.rotateAngleX += this.bipedBody.rotateAngleY;
                    float var8 = 1.0f - this.onGround;
                    var8 *= var8;
                    var8 *= var8;
                    var8 = 1.0F - var8;
                    float var9 = MathHelper.sin(var8 * (float)Math.PI);
                    float var10 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7f) * 0.75F;
                    this.bipedLeftArm.rotateAngleX = (float)(this.bipedLeftArm.rotateAngleX - (var9 * 1.2D + var10));
                    this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0f;
                    this.bipedLeftArm.rotateAngleZ = -MathHelper.sin(this.onGround * (float)Math.PI) * -0.4f;
                }
                else
                {
                    this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(this.onGround) * (float)Math.PI * 2.0F) * 0.2f;
                    this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
                    this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
                    this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
                    this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
                    this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                    this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                    this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
                    float var8 = 1.0f - this.onGround;
                    var8 *= var8;
                    var8 *= var8;
                    var8 = 1.0f - var8;
                    float var9 = MathHelper.sin(var8 * (float)Math.PI);
                    float var10 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7f) * 0.75f;
                    this.bipedRightArm.rotateAngleX = (float)(this.bipedRightArm.rotateAngleX - (var9 * 1.2D + var10));
                    this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0f;
                    this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4f;
                }
            }
            
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09f) * 0.05f + 0.05f;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09f) * 0.05f + 0.05f;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067f) * 0.05f;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067f) * 0.05f;
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
