package takap.mods.nnnpc.gui;

import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiMenuButton extends GuiButton
{
    private boolean isHover;
    private boolean isSelected;
    
    public GuiMenuButton(int par1, String par2Str)
    {
        super(par1, 0, 0, 0, 0, par2Str);
        this.isHover = false;
        setIsSelected(false);
    }
    
    public void setIsSelected(boolean flag)
    {
        this.isSelected = flag;
    }
    
    public void setButtonProperty(int posX, int posY, int width, int height)
    {
        this.xPosition = posX;
        this.yPosition = posY;
        this.width = width;
        this.height = height;
    }
    
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if ( this.drawButton )
        {
            FontRenderer fontRenderer = par1Minecraft.fontRenderer;
            par1Minecraft.renderEngine.bindTexture(Utility.menuButtonBackgroundImage);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHover = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            int textureOffsetY = 0;
            int enabledY = 0;
            int disabledY = 20;
            int hoverY = 40;
            if ( this.enabled )
            {
                if ( this.isHover )
                {
                    textureOffsetY = hoverY;
                }
                else
                {
                    if ( this.isSelected )
                    {
                        textureOffsetY = enabledY;
                    }
                    else
                    {
                        textureOffsetY = disabledY;
                    }
                }
            }
            else
            {
                textureOffsetY = disabledY;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, textureOffsetY, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, textureOffsetY, this.width / 2, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);

            if (this.isHover)
            {
                fontRenderer.drawStringWithShadow(this.displayString, this.xPosition+(this.width-fontRenderer.getStringWidth(this.displayString))/2, this.yPosition + (this.height - 8) / 2, Utility.colorWhite);
            }
            else
            {
                if (!this.isSelected)
                {
                    fontRenderer.drawString(this.displayString, this.xPosition+(this.width-fontRenderer.getStringWidth(this.displayString))/2, this.yPosition + (this.height - 8) / 2, Utility.colorBlack);
                }
                else
                {
                    fontRenderer.drawStringWithShadow(this.displayString, this.xPosition+(this.width-fontRenderer.getStringWidth(this.displayString))/2, this.yPosition + (this.height - 8) / 2, Utility.colorWhite);
                }
            }

        }
    }
}
