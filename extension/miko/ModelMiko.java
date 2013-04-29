package takap.mods.nnnpc.extension.miko;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import takap.mods.nnnpc.entity.IModelWithHands;
import takap.mods.nnnpc.utility.Utility;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;


public class ModelMiko extends ModelBase implements IModelWithHands
{
    public ModelRenderer bipedHead;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer neck;
    public ModelRenderer cloth;
    public ModelRenderer skirt;
    public ModelRenderer frontSkirt;
    public ModelRenderer rightSkirt;
    public ModelRenderer leftSkirt;
    public ModelRenderer rearSkirt;
    public ModelRenderer rightArmOption;
    public ModelRenderer leftArmOption;
    public ModelRenderer hairRightLong;
    public ModelRenderer hairLeftLong;
    public ModelRenderer hairTop;
    public ModelRenderer hairFront1;
    public ModelRenderer hairFront2;
    public ModelRenderer hairFront3;
    public ModelRenderer hairFront4;
    public ModelRenderer hairFront5;
    public ModelRenderer hairFront6;
    public ModelRenderer hairFront7;
    public ModelRenderer hairRight1;
    public ModelRenderer hairRight2;
    public ModelRenderer hairRight3;
    public ModelRenderer hair4;
    public ModelRenderer hairLeft3;
    public ModelRenderer hairLeft2;
    public ModelRenderer hairLeft1;
    public ModelRenderer hairRear;
    public ModelRenderer ponytail;
    public ModelRenderer hairRearEx1;
    public ModelRenderer hairRearEx2;
    public ModelRenderer hairRearEx3;
    public ModelRenderer upperRightRibbon;
    public ModelRenderer upperLeftRibbon;
    public ModelRenderer lowerRightRibbon;
    public ModelRenderer lowerLeftRibbon;
    public ModelRenderer hairAcc;
    
    public boolean isSneak;
    public boolean aimedBow;

    public ModelMiko()
    {
    	this.isSneak = false;
    	this.aimedBow = false;

    	this.textureWidth = 64;
    	this.textureHeight = 32;

    	this.bipedBody = new ModelRenderer(this, 16, 12).setTextureSize(this.textureWidth, this.textureHeight);
    	this.bipedBody.addBox(-2F, 0F, -1F, 4, 7, 2);
    	this.bipedBody.setRotationPoint(0F, 9F, 0F);

    	this.bipedHead = new ModelRenderer(this, 0, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.bipedHead.addBox(-3F, -6.5F, -3F, 6, 6, 6);
    	this.bipedHead.setRotationPoint(0F, 0F, 0F);

    	this.bipedRightArm = new ModelRenderer(this, 0, 12).setTextureSize(this.textureWidth, this.textureHeight);
    	this.bipedRightArm.addBox(-1.6F, -0.5F, -1F, 2, 7, 2);
    	this.bipedRightArm.setRotationPoint(-2.5F, 10F, 0F);

    	this.bipedLeftArm = new ModelRenderer(this, 0, 12).setTextureSize(this.textureWidth, this.textureHeight);
    	this.bipedLeftArm.mirror = true;
    	this.bipedLeftArm.addBox(-0.4F, -0.5F, -1F, 2, 7, 2);
    	this.bipedLeftArm.setRotationPoint(2.5F, 10F, 0F);

    	this.bipedRightLeg = new ModelRenderer(this, 8, 12).setTextureSize(this.textureWidth, this.textureHeight);
    	this.bipedRightLeg.addBox(-1F, 0F, -1F, 2, 8, 2);
    	this.bipedRightLeg.setRotationPoint(-1.1F, 7F, 0F);

    	this.bipedLeftLeg = new ModelRenderer(this, 8, 12).setTextureSize(this.textureWidth, this.textureHeight);
    	this.bipedLeftLeg.mirror = true;
    	this.bipedLeftLeg.addBox(-1F, 0F, -1F, 2, 8, 2);
    	this.bipedLeftLeg.setRotationPoint(1.1F, 7F, 0F);

    	this.neck = new ModelRenderer(this, 18, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.neck.addBox(-1F, -3F, -1F, 2, 3, 2);
    	this.neck.setRotationPoint(0F, 0F, 0F);

    	this.cloth = new ModelRenderer(this, 12, 22).setTextureSize(this.textureWidth, this.textureHeight);
    	this.cloth.addBox(-2.5F, -0.2F, -1.5F, 5, 5, 3);
    	this.cloth.setRotationPoint(0F, 0F, 0F);

    	this.skirt = new ModelRenderer(this, 48, 9).setTextureSize(this.textureWidth, this.textureHeight);
    	this.skirt.addBox(-2.5F, 0F, -1.5F, 5, 1, 3);
    	this.skirt.setRotationPoint(0F, 5.5F, 0F);

    	this.frontSkirt = new ModelRenderer(this, 28, 9).setTextureSize(this.textureWidth, this.textureHeight);
    	this.frontSkirt.addBox(-2.5F, 0F, -0.5F, 5, 6, 1);
    	this.frontSkirt.setRotationPoint(0F, 6F, -1F);

    	this.rightSkirt = new ModelRenderer(this, 40, 9).setTextureSize(this.textureWidth, this.textureHeight);
    	this.rightSkirt.addBox(-0.5F, 0F, -1.5F, 1, 6, 3);
    	this.rightSkirt.setRotationPoint(-2F, 6F, 0F);

    	this.leftSkirt = new ModelRenderer(this, 40, 9).setTextureSize(this.textureWidth, this.textureHeight);
    	this.leftSkirt.mirror = true;
    	this.leftSkirt.addBox(-0.5F, 0F, -1.5F, 1, 6, 3);
    	this.leftSkirt.setRotationPoint(2F, 6F, 0F);

    	this.rearSkirt = new ModelRenderer(this, 28, 9).setTextureSize(this.textureWidth, this.textureHeight);
    	this.rearSkirt.addBox(-2.5F, 0F, -0.5F, 5, 6, 1);
    	this.rearSkirt.setRotationPoint(0F, 6F, 1F);

    	this.rightArmOption = new ModelRenderer(this, 0, 22).setTextureSize(this.textureWidth, this.textureHeight);
    	this.rightArmOption.addBox(-2.1F, 1F, -1.2F, 3, 5, 3);
    	this.rightArmOption.setRotationPoint(0F, 0F, 0F);

    	this.leftArmOption = new ModelRenderer(this, 0, 22).setTextureSize(this.textureWidth, this.textureHeight);
    	this.leftArmOption.mirror = true;
    	this.leftArmOption.addBox(-0.9F, 1F, -1.2F, 3, 5, 3);
    	this.leftArmOption.setRotationPoint(0F, 0F, 0F);

    	this.hairRightLong = new ModelRenderer(this, 26, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairRightLong.addBox(-3.5F, -6F, -2.5F, 1, 7, 1);
    	this.hairRightLong.setRotationPoint(0F, 0F, 0F);

    	this.hairLeftLong = new ModelRenderer(this, 30, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairLeftLong.mirror = true;
    	this.hairLeftLong.addBox(2.5F, -6F, -2.5F, 1, 7, 1);
    	this.hairLeftLong.setRotationPoint(0F, 0F, 0F);

    	this.hairTop = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairTop.addBox(-3.5F, -7.5F, -3.5F, 7, 3, 7);
    	this.hairTop.setRotationPoint(0F, 0F, 0F);

    	this.hairFront1 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairFront1.addBox(-3.5F, -6F, -3.5F, 1, 5, 1);
    	this.hairFront1.setRotationPoint(0F, 0F, 0F);

    	this.hairFront2 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairFront2.addBox(-3F, -5.5F, -3.5F, 1, 3, 1);
    	this.hairFront2.setRotationPoint(0F, 0F, 0F);

    	this.hairFront3 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairFront3.addBox(-2.5F, -5F, -3.5F, 1, 1, 1);
    	this.hairFront3.setRotationPoint(0F, 0F, 0F);

    	this.hairFront4 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairFront4.addBox(-0.5F, -5.5F, -3.5F, 1, 2, 1);
    	this.hairFront4.setRotationPoint(0F, 0F, 0F);

    	this.hairFront5 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairFront5.addBox(1.5F, -5F, -3.5F, 1, 1, 1);
    	this.hairFront5.setRotationPoint(0F, 0F, 0F);

    	this.hairFront6 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairFront6.addBox(2F, -5.5F, -3.5F, 1, 3, 1);
    	this.hairFront6.setRotationPoint(0F, 0F, 0F);

    	this.hairFront7 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairFront7.addBox(2.5F, -6F, -3.5F, 1, 5, 1);
    	this.hairFront7.setRotationPoint(0F, 0F, 0F);

    	this.hairRight1 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairRight1.addBox(-3.5F, -6F, 1.5F, 1, 7, 1);
    	this.hairRight1.setRotationPoint(0F, 0F, 0F);

    	this.hairRight2 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairRight2.addBox(-3.5F, -5.5F, 0.5F, 1, 6, 1);
    	this.hairRight2.setRotationPoint(0F, 0F, 0F);

    	this.hairRight3 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairRight3.addBox(-3.5F, -6F, -0.5F, 1, 6, 1);
    	this.hairRight3.setRotationPoint(0F, 0F, 0F);

    	this.hair4 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hair4.addBox(-3.5F, -5.5F, -1.5F, 7, 2, 1);
    	this.hair4.setRotationPoint(0F, 0F, 0F);

    	this.hairLeft3 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairLeft3.addBox(2.5F, -6F, -0.5F, 1, 6, 1);
    	this.hairLeft3.setRotationPoint(0F, 0F, 0F);

    	this.hairLeft2 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairLeft2.addBox(2.5F, -5.5F, 0.5F, 1, 6, 1);
    	this.hairLeft2.setRotationPoint(0F, 0F, 0F);

    	this.hairLeft1 = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairLeft1.addBox(2.5F, -6F, 1.5F, 1, 7, 1);
    	this.hairLeft1.setRotationPoint(0F, 0F, 0F);

    	this.hairRear = new ModelRenderer(this, 28, 18).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairRear.addBox(-3.5F, -6F, 2.5F, 7, 7, 1);
    	this.hairRear.setRotationPoint(0F, 0F, 0F);

    	this.ponytail = new ModelRenderer(this, 34, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.ponytail.addBox(-1F, -6F, 3F, 2, 6, 2);
    	this.ponytail.setRotationPoint(0F, 0F, 0F);

    	this.hairRearEx1 = new ModelRenderer(this, 48, 28).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairRearEx1.addBox(-3.5F, 1.0F, 2.0F, 7, 2, 1);
    	this.hairRearEx1.setRotationPoint(0F, 0F, 0F);

    	this.hairRearEx2 = new ModelRenderer(this, 48, 28).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairRearEx2.addBox(-3.0F, 1.0F, 1.5F, 6, 2, 2);
    	this.hairRearEx2.setRotationPoint(0F, 0F, 0F);

    	this.hairRearEx3 = new ModelRenderer(this, 48, 28).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairRearEx3.addBox(-3.0F, 3.0F, 2.0F, 6, 1, 1);
    	this.hairRearEx3.setRotationPoint(0F, 0F, 0F);

    	this.upperRightRibbon = new ModelRenderer(this, 46, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.upperRightRibbon.addBox(-5F, -1.5F, 0F, 4, 3, 1);
    	this.upperRightRibbon.setRotationPoint(0F, -5F, 3F);
    	this.upperRightRibbon.rotateAngleX = 0f;
    	this.upperRightRibbon.rotateAngleY = 0.3490659f;
    	this.upperRightRibbon.rotateAngleZ = 0.3490659f;
    	
    	this.upperLeftRibbon = new ModelRenderer(this, 46, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.upperLeftRibbon.mirror = true;
    	this.upperLeftRibbon.addBox(1F, -1.5F, 0F, 4, 3, 1);
    	this.upperLeftRibbon.setRotationPoint(0F, -5F, 3F);
    	this.upperLeftRibbon.rotateAngleX = 0f;
    	this.upperLeftRibbon.rotateAngleY = -0.3490659f;
    	this.upperLeftRibbon.rotateAngleZ = -0.3490659f;

    	this.lowerRightRibbon = new ModelRenderer(this, 42, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.lowerRightRibbon.addBox(-1.5F, 0F, 0F, 1, 6, 1);
    	this.lowerRightRibbon.setRotationPoint(0F, -5F, 3F);
    	this.lowerRightRibbon.rotateAngleX = 0.3490659f;
    	this.lowerRightRibbon.rotateAngleY = 0f;
    	this.lowerRightRibbon.rotateAngleZ = 0.3490659f;

    	this.lowerLeftRibbon = new ModelRenderer(this, 42, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.lowerLeftRibbon.addBox(0.5F, 0F, 0F, 1, 6, 1);
    	this.lowerLeftRibbon.setRotationPoint(0F, -5F, 3F);
    	this.lowerLeftRibbon.rotateAngleX = 0.3490659f;
    	this.lowerLeftRibbon.rotateAngleY = 0f;
    	this.lowerLeftRibbon.rotateAngleZ = -0.3490659f;

    	this.hairAcc = new ModelRenderer(this, 56, 0).setTextureSize(this.textureWidth, this.textureHeight);
    	this.hairAcc.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 1);
    	this.hairAcc.setRotationPoint(3F, -6F, -1F);
    	this.hairAcc.rotateAngleX = 0f;
    	this.hairAcc.rotateAngleY = -1.570796f;
    	this.hairAcc.rotateAngleZ = -0.1f;
        
    	this.bipedBody.addChild(this.bipedHead);
    	this.bipedBody.addChild(this.neck);
    	this.bipedBody.addChild(this.bipedLeftLeg);
    	this.bipedBody.addChild(this.bipedRightLeg);
    	this.bipedBody.addChild(this.cloth);
    	this.bipedBody.addChild(this.skirt);
    	this.bipedBody.addChild(this.frontSkirt);
    	this.bipedBody.addChild(this.rearSkirt);
    	this.bipedBody.addChild(this.leftSkirt);
    	this.bipedBody.addChild(this.rightSkirt);

    	this.bipedHead.addChild(this.hairLeftLong);
    	this.bipedHead.addChild(this.hairRightLong);
    	this.bipedHead.addChild(this.hairTop);
    	this.bipedHead.addChild(this.hairFront1);
    	this.bipedHead.addChild(this.hairFront2);
    	this.bipedHead.addChild(this.hairFront3);
    	this.bipedHead.addChild(this.hairFront4);
    	this.bipedHead.addChild(this.hairFront5);
    	this.bipedHead.addChild(this.hairFront6);
    	this.bipedHead.addChild(this.hairFront7);
    	this.bipedHead.addChild(this.hairLeft1);
    	this.bipedHead.addChild(this.hairLeft2);
    	this.bipedHead.addChild(this.hairLeft3);
    	this.bipedHead.addChild(this.hairRight1);
    	this.bipedHead.addChild(this.hairRight2);
    	this.bipedHead.addChild(this.hairRight3);
    	this.bipedHead.addChild(this.hairRear);
    	this.bipedHead.addChild(this.ponytail);
    	this.bipedHead.addChild(this.hairRearEx1);
    	this.bipedHead.addChild(this.hairRearEx2);
    	this.bipedHead.addChild(this.hairRearEx3);
    	this.bipedHead.addChild(this.upperLeftRibbon);
    	this.bipedHead.addChild(this.upperRightRibbon);
    	this.bipedHead.addChild(this.lowerLeftRibbon);
    	this.bipedHead.addChild(this.lowerRightRibbon);
    	this.bipedHead.addChild(this.hairAcc);
        
    	this.bipedLeftArm.addChild(this.leftArmOption);
    	this.bipedRightArm.addChild(this.rightArmOption);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.bipedBody.render(f5);
        this.bipedLeftArm.render(f5);
        this.bipedRightArm.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        EntityNpc npc = (EntityNpc)entity;
        this.bipedHead.rotateAngleY = f3 / 57.29578F;
        this.bipedHead.rotateAngleX = f4 / 57.29578F;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.6F * f1;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 0.6F * f1;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;
        if ( this.isRiding || (npc.getMode()!=null && npc.isMode(EnumMode.SIT)) )
        {
        	this.bipedRightArm.rotateAngleX += -0.8F;
        	this.bipedLeftArm.rotateAngleX += -0.8F;
        	this.bipedRightLeg.rotateAngleX = -1.5F;
        	this.bipedLeftLeg.rotateAngleX = -1.5F;
        	this.bipedRightLeg.rotateAngleY = 0.15F;
        	this.bipedLeftLeg.rotateAngleY = -0.15F;
        }

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        if ( this.onGround > -9990F )
        {
            if ( (npc.getForm() != null) && (npc.getDominantHand().equals(Utility.leftHanded)) )
            {
                float f6 = this.onGround;
                this.bipedBody.rotateAngleY = -MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
                this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 2.5F;
                this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 2.5F;
                this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 2.5F;
                this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 2.5F;
                this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedRightArm.rotateAngleX += this.bipedBody.rotateAngleY;
                f6 = 1.0F - this.onGround;
                f6 *= f6;
                f6 *= f6;
                f6 = 1.0F - f6;
                float f8 = MathHelper.sin(f6 * 3.141593F);
                float f10 = MathHelper.sin(this.onGround * 3.141593F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                this.bipedLeftArm.rotateAngleX -= (double)f8 * 1.2D + (double)f10;
                this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                this.bipedLeftArm.rotateAngleZ = MathHelper.sin(this.onGround * 3.141593F) * -0.4F;
            }
            else
            {
                float f6 = this.onGround;
                this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
                this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 2.5F;
                this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 2.5F;
                this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 2.5F;
                this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 2.5F;
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
                f6 = 1.0F - this.onGround;
                f6 *= f6;
                f6 *= f6;
                f6 = 1.0F - f6;
                float f8 = MathHelper.sin(f6 * 3.141593F);
                float f10 = MathHelper.sin(this.onGround * 3.141593F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                this.bipedRightArm.rotateAngleX -= (double)f8 * 1.2D + (double)f10;
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * 3.141593F) * -0.4F;
            }
        }
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.35F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.35F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
        if ( this.aimedBow )
        {
            float f7 = 0.0F;
            float f9 = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - f7 * 0.6F) + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = (0.1F - f7 * 0.6F) + this.bipedHead.rotateAngleY + 0.4F;
            this.bipedRightArm.rotateAngleX = -1.570796F + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -1.570796F + this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX -= f7 * 1.2F - f9 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= f7 * 1.2F - f9 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
        }

        if ( this.isRiding )
        {
        	this.bipedBody.rotationPointY = 9f;
        	this.bipedLeftArm.rotationPointY = 10f;
        	this.bipedRightArm.rotationPointY = 10f;
        	this.frontSkirt.rotateAngleX = -1.5F;
        	this.rearSkirt.rotateAngleX = 1.2F;
        	this.rightSkirt.rotateAngleZ = 1.2F;
        	this.leftSkirt.rotateAngleZ = -1.2F;
        	this.bipedRightArm.rotateAngleZ = -0.4F;
        	this.bipedLeftArm.rotateAngleZ = 0.4F;
        }
        else if ( npc.getMode() != null && npc.isMode(EnumMode.SIT) )
        {
        	this.bipedBody.rotationPointY = 16f;
        	this.bipedLeftArm.rotationPointY = 17f;
        	this.bipedRightArm.rotationPointY = 17f;
        	this.frontSkirt.rotateAngleX = -1.5F;
        	this.rearSkirt.rotateAngleX = 1.2F;
        	this.rightSkirt.rotateAngleZ = 1.2F;
        	this.leftSkirt.rotateAngleZ = -1.2F;
        	this.bipedRightArm.rotateAngleZ = -0.4F;
        	this.bipedLeftArm.rotateAngleZ = 0.4F;
        }
        else
        {
        	this.bipedBody.rotationPointY = 9f;
        	this.bipedLeftArm.rotationPointY = 10f;
        	this.bipedRightArm.rotationPointY = 10f;
        	this.frontSkirt.rotateAngleX = -0.3490659F;
        	this.rearSkirt.rotateAngleX = 0.3490659F;
        	this.rightSkirt.rotateAngleZ = 0.3490659F;
        	this.leftSkirt.rotateAngleZ = -0.3490659F;
        }
    }

    @Override
    public void postRenderLeftArm(float par1)
    {
    	this.bipedLeftArm.postRender(par1);
    }

    @Override
    public void postRenderRightArm(float par1)
    {
    	this.bipedRightArm.postRender(par1);
    }
}
