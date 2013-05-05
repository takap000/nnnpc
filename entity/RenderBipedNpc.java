package takap.mods.nnnpc.entity;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.texture.Texture;
import takap.mods.nnnpc.utility.Utility;

public class RenderBipedNpc extends RenderLiving
{
    protected float baseShadowSize;
    protected ModelBase modelBase;

    public RenderBipedNpc(ModelBase npcModel, float f)
    {
        super(npcModel, f);
        this.baseShadowSize = f;
        this.modelBase = npcModel;
    }
    
    @Override
    protected void func_98190_a(EntityLiving entity)
    {
    	EntityNpc npc = (EntityNpc)entity;
        Texture texture = npc.getNpcTexture();
        if ( texture == null )
        {
            return;
        }
        if ( texture.getImageIndex() >= 0 )
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getImageIndex());
            // glBindTexture()の後はresetBoundTexture()を忘れずに！
            this.renderManager.renderEngine.resetBoundTexture();
        	return;
        }
        
        int i = this.renderManager.renderEngine.allocateAndSetupTexture(texture.getTextureImage());
        if ( i >= 0 )
        {
            texture.setImageIndex(i);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getImageIndex());
            // glBindTexture()の後は忘れずに！
            this.renderManager.renderEngine.resetBoundTexture();
            // 初回のみ出力
            Utility.printInformation("register texture: " + texture.getAbsolutePath() + ", index: " + i);
            return;
        }
    }
    
    @Override
    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2)
    {
        super.preRenderCallback(par1EntityLiving, par2);
        scaleNpc((EntityNpc)par1EntityLiving, par2);
    }
    
    protected void scaleNpc(EntityNpc npc, float f)
    {
        FormBase form = npc.getForm();
        if ( form != null )
        {
            float scale = form.getScaleWeight() * npc.getScaleAmount();
//            shadowSize = baseShadowSize * f1;
            GL11.glScalef(scale, scale, scale);
        }
    }
    
    protected void renderOptions(EntityLiving entity, double par2, double par4, double par6)
    {
        EntityNpc npc = (EntityNpc)entity;
        EntityPlayer player = (EntityPlayer)this.renderManager.livingPlayer;
        if ( npc.isNameVisible() && (!player.username.equals(npc.getGuiHolder())) )
        {
            float distance = npc.getDistanceToEntity(this.renderManager.livingPlayer);
            float distanceLimit = 64F;

            if (distance < distanceLimit)
            {
                String s = npc.getNpcName();
                renderLivingLabel(entity, s, par2, par4, par6, 64);
            }
        }
        if ( npc.isLifeVisible() && (!player.username.equals(npc.getGuiHolder())) )
        {
            float distance = npc.getDistanceToEntity(this.renderManager.livingPlayer);
            float distanceLimit = 64F;

            if (distance < distanceLimit)
            {
                String s = npc.getNpcName();
                renderLifebar(entity, s, par2, par4, par6, 64);
            }
        }
    }
    
    @Override
    protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        renderOptions(par1EntityLiving, par2, par4, par6);
    }

    protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9)
    {
        EntityNpc npc = (EntityNpc)par1EntityLiving;
        float f = par1EntityLiving.getDistanceToEntity(this.renderManager.livingPlayer);

        if (f > par9)
        {
            return;
        }

        FontRenderer fontrenderer = getFontRendererFromRenderManager();
        float f1 = 1.6F;
        float f2 = 0.01666667F * f1;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + (npc.height + 0.8F + 0.25F * npc.getScaleAmount()), (float)par7);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f2, -f2, f2);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        byte byte0 = 0;

/*
        // render plate
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        int i = fontrenderer.getStringWidth(par2Str) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex(-i - 1, -1 + byte0, 0.0D);
        tessellator.addVertex(-i - 1, 8 + byte0, 0.0D);
        tessellator.addVertex(i + 1, 8 + byte0, 0.0D);
        tessellator.addVertex(i + 1, -1 + byte0, 0.0D);
        tessellator.draw();
*/

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, byte0, 0x20ffffff);
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, byte0, -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    protected void renderLifebar(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9)
    {
        EntityNpc npc = (EntityNpc)par1EntityLiving;
        float f = par1EntityLiving.getDistanceToEntity(renderManager.livingPlayer);

        if (f > par9)
        {
            return;
        }

        float f1 = 1.6F;
        float f2 = 0.01666667F * f1;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + (npc.height + 0.5F + 0.25F * npc.getScaleAmount()), (float)par7);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f2, -f2, f2);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        int i = 24;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.75F);
        tessellator.addVertex(-i - 1, 0, 0.0D);
        tessellator.addVertex(-i - 1, 6, 0.0D);
        tessellator.addVertex(i + 1, 6, 0.0D);
        tessellator.addVertex(i + 1, 0, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        Tessellator tessellator2 = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator2.startDrawingQuads();
        int j = 24;
        int currentLife = (int)(2.0d * j * npc.getHealth() / npc.getMaxHealth());
        tessellator2.setColorRGBA_I(npc.getLifeColor(), 196);
        tessellator2.addVertex(-j, 1, 0.0D);
        tessellator2.addVertex(-j, 5, 0.0D);
        tessellator2.addVertex(-j+currentLife, 5, 0.0D);
        tessellator2.addVertex(-j+currentLife, 1, 0.0D);
        tessellator2.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);


//        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @Override
    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        EntityNpc npc = (EntityNpc)par1EntityLiving;
        if ( !(modelBase instanceof IModelWithHands) )
        {
            return;
        }

        // left hand
        FormBase form = npc.getForm();
        ItemStack itemstack = npc.getHeldItemOnLeftHand();
        if (itemstack != null)
        {
            GL11.glPushMatrix();
            ((IModelWithHands)this.modelBase).postRenderLeftArm(0.0625F);
            GL11.glTranslatef(form.getItemOffsetX(), form.getItemOffsetY(), form.getItemOffsetZ());

            if (itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
            {
                float f = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f *= 0.75F;
                GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f, -f, f);
            }
            else if (itemstack.itemID == Item.bow.itemID)
            {
                float f1 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20F, 0.0F, -1.0F, 0.0F);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else if (Item.itemsList[itemstack.itemID].isFull3D())
            {
                float f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                float f3 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f3, f3, f3);
                GL11.glRotatef(60F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
            }

            this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 0);

            if (itemstack.getItem().requiresMultipleRenderPasses())
            {
            	this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 1);
            }

            GL11.glPopMatrix();
        }

        // right hand
        itemstack = npc.getHeldItemOnRightHand();
        if (itemstack != null)
        {
            GL11.glPushMatrix();
            ((IModelWithHands)this.modelBase).postRenderRightArm(0.0625F);
            GL11.glTranslatef(-form.getItemOffsetX(), form.getItemOffsetY(), form.getItemOffsetZ());

            if (itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
            {
                float f = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f *= 0.75F;
                GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f, -f, f);
            }
            else if (itemstack.itemID == Item.bow.itemID)
            {
                float f1 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else if (Item.itemsList[itemstack.itemID].isFull3D())
            {
                float f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                float f3 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f3, f3, f3);
                GL11.glRotatef(60F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
            }

            this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 0);

            if (itemstack.getItem().requiresMultipleRenderPasses())
            {
            	this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 1);
            }

            GL11.glPopMatrix();
        }
    }
}
