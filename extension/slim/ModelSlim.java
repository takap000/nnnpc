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
        this.head.addBox(-4f, -8f, -4f, 8, 8, 8);
        this.head.setRotationPoint(0f, -0.2f, 0f);
        this.headWear = new ModelRenderer(this, 0, 16).setTextureSize(this.textureWidth, this.textureHeight);
        this.headWear.addBox(-4f, -8f, -4f, 8, 8, 8, 0.5f);
        this.headWear.setRotationPoint(0f, 0f, 0f);
        this.body = new ModelRenderer(this, 0, 32).setTextureSize(this.textureWidth, this.textureHeight);
        this.body.addBox(-4f, 0f, -2f, 8, 16, 4);
        this.body.setRotationPoint(0f, -8f, 0f);
        this.neck = new ModelRenderer(this, 0, 52).setTextureSize(this.textureWidth, this.textureHeight);
        this.neck.addBox(-1.5f, -3f, -1.5f, 3, 3, 3);
        this.neck.setRotationPoint(0f, 0f, 0f);
        this.rightUpperArm = new ModelRenderer(this, 32, 0).setTextureSize(this.textureWidth, this.textureHeight);
        this.rightUpperArm.addBox(-2f, -1.8f, -2f, 4, 8, 4);
        this.rightUpperArm.setRotationPoint(-6f, -6f, 0f);
        this.rightLowerArm = new ModelRenderer(this, 32, 12).setTextureSize(this.textureWidth, this.textureHeight);
        this.rightLowerArm.addBox(-2f, 0f, -2f, 4, 8, 4);
        this.rightLowerArm.setRotationPoint(0f, 6.2f, 0f);
        this.leftUpperArm = new ModelRenderer(this, 48, 0).setTextureSize(this.textureWidth, this.textureHeight);
        this.leftUpperArm.addBox(-2f, -1.8f, -2f, 4, 8, 4);
        this.leftUpperArm.setRotationPoint(6f, -6f, 0f);
        this.leftLowerArm = new ModelRenderer(this, 48, 12).setTextureSize(this.textureWidth, this.textureHeight);
        this.leftLowerArm.addBox(-2f, 0f, -2f, 4, 8, 4);
        this.leftLowerArm.setRotationPoint(0f, 6.2f, 0f);
        this.rightUpperLeg = new ModelRenderer(this, 32, 24).setTextureSize(this.textureWidth, this.textureHeight);
        this.rightUpperLeg.addBox(-2f, 0f, -2f, 4, 8, 4);
        this.rightUpperLeg.setRotationPoint(-2f, 8f, 0f);
        this.rightLowerLeg = new ModelRenderer(this, 32, 36).setTextureSize(this.textureWidth, this.textureHeight);
        this.rightLowerLeg.addBox(-2f, 0f, -2f, 4, 8, 4);
        this.rightLowerLeg.setRotationPoint(0f, 8f, 0f);
        this.leftUpperLeg = new ModelRenderer(this, 48, 24).setTextureSize(this.textureWidth, this.textureHeight);
        this.leftUpperLeg.addBox(-2f, 0f, -2f, 4, 8, 4);
        this.leftUpperLeg.setRotationPoint(2f, 8f, 0f);
        this.leftLowerLeg = new ModelRenderer(this, 48, 36).setTextureSize(this.textureWidth, this.textureHeight);
        this.leftLowerLeg.addBox(-2f, 0f, -2f, 4, 8, 4);
        this.leftLowerLeg.setRotationPoint(0f, 8f, 0f);

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
        this.head.rotateAngleY = f3 / 57.29578f;
        this.head.rotateAngleX = f4 / 57.29578f;
        float naturalArmAngle = 0.01f;
        float rightArmAngle = MathHelper.cos(f * 0.6662f + 3.141593f) * 2.0f * f1 * 0.5f;
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
        float leftArmAngle = MathHelper.cos(f * 0.6662f) * 2.0f * f1 * 0.5f;
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
        this.rightUpperLeg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.0f * f1;
        this.leftUpperLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + 3.141593f) * 1.0f * f1;
        this.rightUpperLeg.rotateAngleY = 0f;
        this.leftUpperLeg.rotateAngleY = 0f;

        if ( onGround > -9990f )
        {
            if ( (npc.getForm() != null) && (npc.getDominantHand().equals(Utility.leftHanded)) )
            {
                float f6 = onGround;
                this.body.rotateAngleY = -MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593f * 2.0f) * 0.2f;
                this.rightUpperArm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 2.5f;
                this.leftUpperArm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 2.5f;
                this.rightUpperArm.rotateAngleY += this.body.rotateAngleY;
                this.leftUpperArm.rotateAngleY += this.body.rotateAngleY;
                this.rightUpperArm.rotateAngleX -= this.body.rotateAngleY;
                f6 = 1.0f - onGround;
                f6 *= f6;
                f6 *= f6;
                f6 = 1.0f - f6;
                float f8 = MathHelper.sin(f6 * 3.141593f);
                float f10 = MathHelper.sin(onGround * 3.141593f) * -(this.head.rotateAngleX - 0.7f) * 0.75f;
                this.leftUpperArm.rotateAngleX -= f8 * 1.2d + f10;
                this.leftUpperArm.rotateAngleY += this.body.rotateAngleY * 2.0f;
                this.leftUpperArm.rotateAngleZ = -MathHelper.sin(onGround * 3.141593f) * -0.4f;
            }
            else
            {
                float f6 = onGround;
                this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593f * 2.0f) * 0.2f;
                this.rightUpperArm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 2.5f;
                this.leftUpperArm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 2.5f;
                this.rightUpperArm.rotateAngleY += this.body.rotateAngleY;
                this.leftUpperArm.rotateAngleY += this.body.rotateAngleY;
                this.leftUpperArm.rotateAngleX += this.body.rotateAngleY;
                f6 = 1.0f - onGround;
                f6 *= f6;
                f6 *= f6;
                f6 = 1.0f - f6;
                float f8 = MathHelper.sin(f6 * 3.141593f);
                float f10 = MathHelper.sin(onGround * 3.141593f) * -(this.head.rotateAngleX - 0.7f) * 0.75f;
                this.rightUpperArm.rotateAngleX -= f8 * 1.2d + f10;
                this.rightUpperArm.rotateAngleY += this.body.rotateAngleY * 2.0f;
                this.rightUpperArm.rotateAngleZ = MathHelper.sin(onGround * 3.141593f) * -0.4f;
            }
        }
        this.rightUpperArm.rotateAngleZ += MathHelper.cos(f2 * 0.09f) * 0.02f + 0.02f;
        this.leftUpperArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09f) * 0.02f + 0.02f;
        
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
            this.rightUpperArm.rotateAngleX += -((float)Math.PI / 5f);
            this.leftUpperArm.rotateAngleX += -((float)Math.PI / 5f);
            this.rightUpperLeg.rotateAngleX = -((float)Math.PI * 2f / 5f);
            this.leftUpperLeg.rotateAngleX = -((float)Math.PI * 2f / 5f);
            this.rightUpperLeg.rotateAngleY = ((float)Math.PI / 10f);
            this.leftUpperLeg.rotateAngleY = -((float)Math.PI / 10f);
            
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
