package takap.mods.nnnpc.extension.slim;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import takap.mods.nnnpc.entity.IModelWithHands;
import takap.mods.nnnpc.utility.Utility;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;


public class ModelSlim extends ModelBase implements IModelWithHands
{
    protected ModelRenderer head;
    protected ModelRenderer headWear;
    protected ModelRenderer body;
    protected ModelRenderer neck;
    protected ModelRenderer rightUpperArm;
    protected ModelRenderer rightLowerArm;
    protected ModelRenderer leftUpperArm;
    protected ModelRenderer leftLowerArm;
    protected ModelRenderer rightUpperLeg;
    protected ModelRenderer rightLowerLeg;
    protected ModelRenderer leftUpperLeg;
    protected ModelRenderer leftLowerLeg;
    protected final float bodyRotationPointY = -8f;
    protected final float armsRotationPointY = -6f;
    protected final float legsRotationPointY = 8f;
    protected final float sittingOffset = 14f;
  
    public ModelSlim()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
    
        this.head = new ModelRenderer(this, 0, 0).setTextureSize(this.textureWidth, this.textureHeight);
        this.head.addBox(-4F, -8F, -4F, 8, 8, 8);
        this.head.setRotationPoint(0F, -0.2F, 0F);
        this.headWear = new ModelRenderer(this, 0, 16).setTextureSize(this.textureWidth, this.textureHeight);
        this.headWear.addBox(-4F, -8F, -4F, 8, 8, 8, 0.5f);
        this.headWear.setRotationPoint(0F, 0F, 0F);
        this.body = new ModelRenderer(this, 0, 32).setTextureSize(this.textureWidth, this.textureHeight);
        this.body.addBox(-4F, 0F, -2F, 8, 16, 4);
        this.body.setRotationPoint(0F, -8F, 0F);
        this.neck = new ModelRenderer(this, 0, 52).setTextureSize(this.textureWidth, this.textureHeight);
        this.neck.addBox(-1.5F, -3F, -1.5F, 3, 3, 3);
        this.neck.setRotationPoint(0F, 0F, 0F);
        this.rightUpperArm = new ModelRenderer(this, 32, 0).setTextureSize(this.textureWidth, this.textureHeight);
        this.rightUpperArm.addBox(-2F, -1.8F, -2F, 4, 8, 4);
        this.rightUpperArm.setRotationPoint(-6F, -6F, 0F);
        this.rightLowerArm = new ModelRenderer(this, 32, 12).setTextureSize(this.textureWidth, this.textureHeight);
        this.rightLowerArm.addBox(-2F, 0F, -2F, 4, 8, 4);
        this.rightLowerArm.setRotationPoint(0F, 6.2F, 0F);
        this.leftUpperArm = new ModelRenderer(this, 48, 0).setTextureSize(this.textureWidth, this.textureHeight);
        this.leftUpperArm.addBox(-2F, -1.8F, -2F, 4, 8, 4);
        this.leftUpperArm.setRotationPoint(6F, -6F, 0F);
        this.leftLowerArm = new ModelRenderer(this, 48, 12).setTextureSize(this.textureWidth, this.textureHeight);
        this.leftLowerArm.addBox(-2F, 0F, -2F, 4, 8, 4);
        this.leftLowerArm.setRotationPoint(0F, 6.2F, 0F);
        this.rightUpperLeg = new ModelRenderer(this, 32, 24).setTextureSize(this.textureWidth, this.textureHeight);
        this.rightUpperLeg.addBox(-2F, 0F, -2F, 4, 8, 4);
        this.rightUpperLeg.setRotationPoint(-2F, 8F, 0F);
        this.rightLowerLeg = new ModelRenderer(this, 32, 36).setTextureSize(this.textureWidth, this.textureHeight);
        this.rightLowerLeg.addBox(-2F, 0F, -2F, 4, 8, 4);
        this.rightLowerLeg.setRotationPoint(0F, 8F, 0F);
        this.leftUpperLeg = new ModelRenderer(this, 48, 24).setTextureSize(this.textureWidth, this.textureHeight);
        this.leftUpperLeg.addBox(-2F, 0F, -2F, 4, 8, 4);
        this.leftUpperLeg.setRotationPoint(2F, 8F, 0F);
        this.leftLowerLeg = new ModelRenderer(this, 48, 36).setTextureSize(this.textureWidth, this.textureHeight);
        this.leftLowerLeg.addBox(-2F, 0F, -2F, 4, 8, 4);
        this.leftLowerLeg.setRotationPoint(0F, 8F, 0F);

        this.body.addChild(this.head);
        this.body.addChild(this.neck);
        this.head.addChild(this.headWear);
        this.rightUpperArm.addChild(this.rightLowerArm);
        this.leftUpperArm.addChild(this.leftLowerArm);
        this.rightUpperLeg.addChild(this.rightLowerLeg);
        this.leftUpperLeg.addChild(this.leftLowerLeg);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.body.render(f5);
        this.rightUpperArm.render(f5);
        this.leftUpperArm.render(f5);
        this.rightUpperLeg.render(f5);
        this.leftUpperLeg.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        EntityNpc npc = (EntityNpc)entity;
        this.head.rotateAngleY = f3 / 57.29578F;
        this.head.rotateAngleX = f4 / 57.29578F;
        float naturalArmAngle = 0.01f;
        float rightArmAngle = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
        if ( rightArmAngle < 0 )
        {
            this.rightUpperArm.rotateAngleX = rightArmAngle + naturalArmAngle;
            this.rightLowerArm.rotateAngleX = rightArmAngle - naturalArmAngle * 2;
        }
        else
        {
            this.rightUpperArm.rotateAngleX = rightArmAngle + naturalArmAngle;
            this.rightLowerArm.rotateAngleX = - naturalArmAngle * 3;
        }
        float leftArmAngle = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
        if ( leftArmAngle < 0 )
        {
            this.leftUpperArm.rotateAngleX = leftArmAngle + naturalArmAngle;
            this.leftLowerArm.rotateAngleX = leftArmAngle - naturalArmAngle * 2;
        }
        else
        {
            this.leftUpperArm.rotateAngleX = leftArmAngle + naturalArmAngle;
            this.leftLowerArm.rotateAngleX = - naturalArmAngle * 3;
        }
        this.rightUpperArm.rotateAngleY = 0f;
        this.rightLowerArm.rotateAngleY = 0f;
        this.rightUpperArm.rotateAngleZ = 0f;
        this.leftUpperArm.rotateAngleY = 0f;
        this.leftLowerArm.rotateAngleY = 0f;
        this.leftUpperArm.rotateAngleZ = 0f;
        this.rightUpperLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.0F * f1;
        this.leftUpperLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.0F * f1;
        this.rightUpperLeg.rotateAngleY = 0f;
        this.leftUpperLeg.rotateAngleY = 0f;

        if ( onGround > -9990F )
        {
            if ( (npc.getForm() != null) && (npc.getDominantHand().equals(Utility.leftHanded)) )
            {
                float f6 = onGround;
                this.body.rotateAngleY = -MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
                this.rightUpperArm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 2.5F;
                this.leftUpperArm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 2.5F;
                this.rightUpperArm.rotateAngleY += this.body.rotateAngleY;
                this.leftUpperArm.rotateAngleY += this.body.rotateAngleY;
                this.rightUpperArm.rotateAngleX -= this.body.rotateAngleY;
                f6 = 1.0F - onGround;
                f6 *= f6;
                f6 *= f6;
                f6 = 1.0F - f6;
                float f8 = MathHelper.sin(f6 * 3.141593F);
                float f10 = MathHelper.sin(onGround * 3.141593F) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
                this.leftUpperArm.rotateAngleX -= (double)f8 * 1.2D + (double)f10;
                this.leftUpperArm.rotateAngleY += this.body.rotateAngleY * 2.0F;
                this.leftUpperArm.rotateAngleZ = -MathHelper.sin(onGround * 3.141593F) * -0.4F;
            }
            else
            {
                float f6 = onGround;
                this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
                this.rightUpperArm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 2.5F;
                this.leftUpperArm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 2.5F;
                this.rightUpperArm.rotateAngleY += this.body.rotateAngleY;
                this.leftUpperArm.rotateAngleY += this.body.rotateAngleY;
                this.leftUpperArm.rotateAngleX += this.body.rotateAngleY;
                f6 = 1.0F - onGround;
                f6 *= f6;
                f6 *= f6;
                f6 = 1.0F - f6;
                float f8 = MathHelper.sin(f6 * 3.141593F);
                float f10 = MathHelper.sin(onGround * 3.141593F) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
                this.rightUpperArm.rotateAngleX -= (double)f8 * 1.2D + (double)f10;
                this.rightUpperArm.rotateAngleY += this.body.rotateAngleY * 2.0F;
                this.rightUpperArm.rotateAngleZ = MathHelper.sin(onGround * 3.141593F) * -0.4F;
            }
        }
        this.rightUpperArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.02F + 0.02F;
        this.leftUpperArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.02F + 0.02F;
        
        if ( npc.getMode() != null && npc.isMode(EnumMode.SIT) )
        {
            this.rightUpperArm.rotateAngleX += -((float)Math.PI / 5f) + naturalArmAngle * 50f;
            this.rightLowerArm.rotateAngleX -= naturalArmAngle * 100f;
            this.rightUpperArm.rotateAngleY = -((float)Math.PI * 1f / 16f);
            this.rightLowerArm.rotateAngleY = -((float)Math.PI * 2f / 16f);
            this.leftUpperArm.rotateAngleX += -((float)Math.PI / 5f) + naturalArmAngle * 50f;
            this.leftLowerArm.rotateAngleX -= naturalArmAngle * 100f;
            this.leftUpperArm.rotateAngleY = ((float)Math.PI * 1f / 16f);
            this.leftLowerArm.rotateAngleY = ((float)Math.PI * 2f / 16f);
            this.rightUpperLeg.rotateAngleX = -((float)Math.PI * 11f / 16f);
            this.leftUpperLeg.rotateAngleX = -((float)Math.PI * 11f / 16f);
            this.rightLowerLeg.rotateAngleX = ((float)Math.PI * 6f / 16f);
            this.leftLowerLeg.rotateAngleX = ((float)Math.PI * 6f / 16f);
            this.rightUpperLeg.rotateAngleY = ((float)Math.PI / 16f);
            this.leftUpperLeg.rotateAngleY = -((float)Math.PI / 16f);
            
            this.body.rotationPointY = this.bodyRotationPointY + this.sittingOffset;
            this.rightUpperArm.rotationPointY = this.armsRotationPointY + this.sittingOffset;
            this.leftUpperArm.rotationPointY = this.armsRotationPointY + this.sittingOffset;
            this.rightUpperLeg.rotationPointY = this.legsRotationPointY + this.sittingOffset;
            this.leftUpperLeg.rotationPointY = this.legsRotationPointY + this.sittingOffset;
        }
        else
        {
            this.body.rotationPointY = this.bodyRotationPointY;
            this.rightUpperArm.rotationPointY = this.armsRotationPointY;
            this.leftUpperArm.rotationPointY = this.armsRotationPointY;
            this.rightUpperLeg.rotationPointY = this.legsRotationPointY;
            this.leftUpperLeg.rotationPointY = this.legsRotationPointY;

            // lower leg
            if ( this.rightUpperLeg.rotateAngleX < 0 )
            {
                this.rightLowerLeg.rotateAngleX = -this.rightUpperLeg.rotateAngleX;
            }
            else
            {
                this.rightLowerLeg.rotateAngleX = 0f;
            }
            if ( this.leftUpperLeg.rotateAngleX < 0 )
            {
                this.leftLowerLeg.rotateAngleX = -this.leftUpperLeg.rotateAngleX;
            }
            else
            {
                this.leftLowerLeg.rotateAngleX = 0f;
            }
        }

        
        
/* // 足をぶらぶらさせる感じ...余裕があったら設定で切り替え可能にしよう...
        if ( npc.isSitting() )
        {
            this.rightUpperArm.rotateAngleX += -((float)Math.PI / 5F);
            this.leftUpperArm.rotateAngleX += -((float)Math.PI / 5F);
            this.rightUpperLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.leftUpperLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.rightUpperLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.leftUpperLeg.rotateAngleY = -((float)Math.PI / 10F);
            
            this.body.rotationPointY = this.bodyRotationPointY + this.sittingOffset;
            this.rightUpperArm.rotationPointY = this.armsRotationPointY + this.sittingOffset;
            this.leftUpperArm.rotationPointY = this.armsRotationPointY + this.sittingOffset;
            this.rightUpperLeg.rotationPointY = this.legsRotationPointY + this.sittingOffset;
            this.leftUpperLeg.rotationPointY = this.legsRotationPointY + this.sittingOffset;
        }
        else
        {
            this.body.rotationPointY = this.bodyRotationPointY;
            this.rightUpperArm.rotationPointY = this.armsRotationPointY;
            this.leftUpperArm.rotationPointY = this.armsRotationPointY;
            this.rightUpperLeg.rotationPointY = this.legsRotationPointY;
            this.leftUpperLeg.rotationPointY = this.legsRotationPointY;
        }
*/
        
        // lower leg
/*
        if ( this.rightUpperLeg.rotateAngleX < 0 )
        {
            this.rightLowerLeg.rotateAngleX = -this.rightUpperLeg.rotateAngleX;
        }
        else
        {
            this.rightLowerLeg.rotateAngleX = 0f;
        }
        if ( this.leftUpperLeg.rotateAngleX < 0 )
        {
            this.leftLowerLeg.rotateAngleX = -this.leftUpperLeg.rotateAngleX;
        }
        else
        {
            this.leftLowerLeg.rotateAngleX = 0f;
        }
*/
    }

    @Override
    public void postRenderLeftArm(float a)
    {
        this.leftUpperArm.postRender(a);
        this.leftLowerArm.postRender(a);
    }

    @Override
    public void postRenderRightArm(float b)
    {
        this.rightUpperArm.postRender(b);
        this.rightLowerArm.postRender(b);
    }
}
