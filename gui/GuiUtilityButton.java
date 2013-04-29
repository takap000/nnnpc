package takap.mods.nnnpc.gui;

import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiUtilityButton extends GuiButton
{
    private boolean isHover;
    private boolean isActive;
    
    public GuiUtilityButton(int id, int posX, int posY, int width, int height, String par2Str)
    {
        super(id, posX, posY, width, height, par2Str);
        this.isHover = false;
        setActive(true);
    }
    
    public void setActive(boolean flag)
    {
        this.isActive = flag;
    }
    
    public boolean isActive()
    {
        return this.isActive;
    }
    
    public void setButtonProperty(int posX, int posY, int width, int height)
    {
        this.xPosition = posX;
        this.yPosition = posY;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if ( this.drawButton )
        {
            FontRenderer var4 = par1Minecraft.fontRenderer;
            par1Minecraft.renderEngine.bindTexture(Utility.utilityButtonBackgroundImage);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHover = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            int yOffset = 0;
            int enabledY = 0;
            int disabledY = this.height;
            int hoverY = this.height + this.height;
            if ( this.isHover )
            {
                if ( this.isActive )
                {
                    yOffset = hoverY;
                }
                else
                {
                    yOffset = disabledY;
                }
            }
            else
            {
                if ( this.isActive )
                {
                    yOffset = enabledY;
                }
                else
                {
                    yOffset = disabledY;
                }
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, yOffset, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, yOffset, this.width / 2, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);
            int fontColor = Utility.colorWhite;

            if (this.isHover)
            {
                if ( this.isActive )
                {
                    fontColor = Utility.colorWhite;
                }
                else
                {
                    fontColor = Utility.colorDimGray;
                }
            }
            else
            {
                if (!this.isActive)
                {
                    fontColor = Utility.colorDimGray;
                }
                else
                {
                    fontColor = Utility.colorWhite;
                }
            }
            this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, fontColor);
        }
    }
}
