package takap.mods.nnnpc.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;

public abstract class GuiSubpageBase
{
    protected EntityPlayer player;
    protected EntityNpc npc;
    protected String backgroundImage;
    
    protected Minecraft mc;
    protected GuiNpcSetting mainPage;
    protected int guiTop;
    protected int guiBottom;
    protected int guiLeft;
    protected int guiRight;
    protected int guiWidth;
    protected int guiHeight;
    protected int top;
    protected int bottom;
    protected int left;
    protected int right;
    protected int width;
    protected int height;
    protected List<GuiButton> buttonList;
    protected List<GuiButton> overwrappingButtonList;
    
    public GuiSubpageBase(GuiNpcSetting mainPage, EntityPlayer player, EntityNpc npc, Minecraft mc, int guiTop, int guiBottom, int guiLeft, int guiRight, int top, int bottom, int left, int right)
    {
        this.mainPage = mainPage;
        this.player = player;
        this.npc = npc;
        this.mc = mc;
        this.guiTop = guiTop;
        this.guiBottom = guiBottom;
        this.guiLeft = guiLeft;
        this.guiRight = guiRight;
        this.guiWidth = this.guiRight - this.guiLeft;
        this.guiHeight = this.guiBottom - this.guiTop;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.width = this.right - this.left;
        this.height = this.bottom - this.top;
        this.backgroundImage = Utility.settingSubpageBackgroundImage;
        this.buttonList = new ArrayList();
        this.overwrappingButtonList = new ArrayList();
        createButtonList();
    }
    
    public abstract String getSubpageTitle();
    public abstract void createButtonList();
    
    public List<GuiButton> getButtonList()
    {
        return buttonList;
    }
    
    protected void addButtonControl(GuiButton button)
    {
        button.xPosition += this.left;
        button.yPosition += this.top;
        this.buttonList.add(button);
    }
    
    public abstract void drawForegroundLayer();
    
    public void drawBackgroundLayer()
    {
        Tessellator var2 = Tessellator.instance;
        this.mc.renderEngine.bindTexture(this.backgroundImage);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var3 = 8.0F;
        var2.startDrawingQuads();
        var2.addVertexWithUV((double)0, (double)this.height, 0.0D, 0.0D, (double)(height / var3 + 0.0f));
        var2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)(width / var3), (double)(height / var3 + 0.0f));
        var2.addVertexWithUV((double)this.width, (double)0, 0.0D, (double)(width / var3), 0.0f);
        var2.addVertexWithUV((double)0, (double)0, 0.0d, 0.0d, 0.0d);
        var2.draw();
    }
    
    public void drawOverwrappingLayer()
    {
    }
    
    public abstract boolean actionPerformed(int id);
    
    public abstract boolean keyTyped(char par1, int par2);
    
    public void translateToSubpageDomain()
    {
        GL11.glTranslatef(this.left, this.top, 0f);
    }
    
    public void resetTranslation()
    {
        GL11.glTranslatef(-this.left, -this.top, 0f);
    }
    
    public void drawString(String string, int posX, int posY, int color, boolean isShadowed)
    {
        if ( isShadowed )
        {
            this.mc.fontRenderer.drawStringWithShadow(string, posX, posY, color);
        }
        else
        {
            this.mc.fontRenderer.drawString(string, posX, posY, color);
        }
    }
    
    public void drawCenteredString(String string, int posX, int posY, int width, int color, boolean isShadowed)
    {
        FontRenderer fontRenderer = this.mc.fontRenderer;
        if ( isShadowed )
        {
            fontRenderer.drawStringWithShadow(string, posX+(width-fontRenderer.getStringWidth(string))/2, posY, color);
        }
        else
        {
            fontRenderer.drawString(string, posX+(width-fontRenderer.getStringWidth(string))/2, posY, color);
        }
    }
    
    public void drawRightAlignedString(String string, int posX, int posY, int width, int color, boolean isShadowed)
    {
        FontRenderer fontRenderer = this.mc.fontRenderer;
        if ( isShadowed )
        {
            fontRenderer.drawStringWithShadow(string, posX+width-fontRenderer.getStringWidth(string), posY, color);
        }
        else
        {
            fontRenderer.drawString(string, posX+width-fontRenderer.getStringWidth(string), posY, color);
        }
    }
    
    public void setNpc(EntityNpc npc)
    {
        this.npc = npc;
    }
    
    public abstract boolean updateScreen();
    public abstract boolean mouseClicked(int x, int y, int z);
    
    public void drawOverwrappingShade()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(0f, 0f, 0f, 0.8f);
        tessellator.addVertex((double)this.mc.displayWidth, 0d, 0d);
        tessellator.addVertex(0d, 0d, 0d);
        tessellator.addVertex(0d, (double)this.mc.displayHeight, 0d);
        tessellator.addVertex((double)this.mc.displayWidth, (double)this.mc.displayHeight, 0d);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public List<GuiButton> getOverwrappingButtonList()
    {
        return this.overwrappingButtonList;
    }
    
    protected void addOverwrappingButtonControl(GuiButton button)
    {
        this.overwrappingButtonList.add(button);
    }
}
