package takap.mods.nnnpc.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.inventory.ContainerNpcInventoryForSimple;
import takap.mods.nnnpc.packet.PacketNpcGuiCloseEvent;
import takap.mods.nnnpc.utility.Utility;

public class GuiNpcInventory extends GuiContainer
{
    protected float xSize_lo;
    protected float ySize_lo;
    protected EntityPlayer entityPlayer;
    protected EntityNpc npc;
    protected int fontColorWhite = 0xFFFFFF;
    protected int fontColorBlack = 0x202020;
    protected int fontColorRed = 0xF02020;
    protected int updateTick;

    public GuiNpcInventory(EntityPlayer player, EntityNpc npc)
    {
        super(new ContainerNpcInventoryForSimple(player.inventory, npc.getInventory()));
        this.npc = npc;
        xSize = 176;
        ySize = 222;
        updateTick = 0;
        entityPlayer = player;
    }

    @Override
    public void initGui()
    {
        buttonList.clear();
        super.initGui();
        
        // 現時点でinventoryからの操作は無効
/*
        // role
        controlList.add(new GuiSmallHeightButton(10, guiLeft + xSize - 26, guiTop + 43, 10, 12, "<"));
        controlList.add(new GuiSmallHeightButton(11, guiLeft + xSize - 15, guiTop + 43, 10, 12, ">"));
        // mode
        controlList.add(new GuiSmallHeightButton(12, guiLeft + xSize - 26, guiTop + 56, 10, 12, "<"));
        controlList.add(new GuiSmallHeightButton(13, guiLeft + xSize - 15, guiTop + 56, 10, 12, ">"));
        // delete
        controlList.add(new GuiSmallHeightButton(0, guiLeft + xSize + 8, guiTop + 200, 44, 12, "delete"));
        // detail
        controlList.add(new GuiSmallHeightButton(1, guiLeft + xSize + 8, guiTop + 4, 44, 12, "detail"));
*/
    }
    
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        if ( this.npc == null )
        {
            mc.displayGuiScreen(null);
            return;
        }
        String npcNameLabel1 = "Name";
        String npcNameLabel2 = this.npc.getNpcName();
        int currentHealth = this.npc.getHealth();
        if ( currentHealth <= 0 )
        {
            mc.displayGuiScreen(null);
            return;
        }
        String healthLabel1 = "Life";
        String healthLabel2 = (new StringBuilder()).append(currentHealth).append("/").append(this.npc.getMaxHealth()).toString();
//        int healthLabelColor = this.npc.getLifeColor();
        int healthLabelColor = Utility.colorBlack;
        String respawnLabel1 = "Respawn";
        String respawnLabel2 = this.npc.isRespawnable()?"Enable":"Disable";
        String roleLabel1 = "Role";
        String roleLabel2 = this.npc.getRole().getName();
        String modeLabel1 = "Mode";
        String modeLabel2 = (new StringBuilder()).append(this.npc.getMode().getModeName()).toString();
        
        GL11.glPushMatrix();
        GL11.glNormal3f(0.0F, 0.0F, 1F);
        fontRenderer.drawStringWithShadow(npcNameLabel1, 44, 8, Utility.colorWhite);
        fontRenderer.drawString          (npcNameLabel2, 78, 8, Utility.colorBlack);
        fontRenderer.drawStringWithShadow(healthLabel1, 44, 20, Utility.colorWhite);
        fontRenderer.drawString          (healthLabel2, 78, 20, healthLabelColor);
        fontRenderer.drawStringWithShadow(respawnLabel1, 44, 32, Utility.colorWhite);
        fontRenderer.drawString          (respawnLabel2, 98, 32, Utility.colorBlack);
/*
        fontRenderer.drawStringWithShadow(roleLabel1, 8, 45, fontColorWhite);
        fontRenderer.drawString          (roleLabel2, 60, 45, fontColorBlack);
        fontRenderer.drawStringWithShadow(modeLabel1, 8, 58, fontColorWhite);
        fontRenderer.drawString          (modeLabel2, 60, 58, fontColorBlack);
*/
        fontRenderer.drawStringWithShadow(roleLabel1, 44, 44, fontColorWhite);
        fontRenderer.drawString          (roleLabel2, 78, 44, fontColorBlack);
        fontRenderer.drawStringWithShadow(modeLabel1, 44, 56, fontColorWhite);
        fontRenderer.drawString          (modeLabel2, 78, 56, fontColorBlack);
        
        fontRenderer.drawStringWithShadow("L", 155, 70, fontColorWhite);
        fontRenderer.drawStringWithShadow("R", 155, 100, fontColorWhite);
        GL11.glPopMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.renderEngine.bindTexture(Utility.inventoryBackgroundImage);
        int l = guiLeft;
        int i1 = guiTop;
        // background image
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        
        // "Name" text field
        drawTexturedModalRect(l+72, i1+8, 0, 248, 98, 8);
        // "Life" text field
        drawTexturedModalRect(l+72, i1+20, 0, 248, 98, 8);
        // "Respawn" text field
        drawTexturedModalRect(l+92, i1+32, 0, 248, 78, 8);
        // "Role" text field
        drawTexturedModalRect(l+72, i1+44, 0, 248, 98, 8);
        // "Mode" text field
        drawTexturedModalRect(l+72, i1+56, 0, 248, 98, 8);
        
        drawIcon();
        GL11.glPopMatrix();
    }

    private void drawIcon()
    {
        float widthCoefficient = 1.0f / this.npc.getNpcTexture().getWidth();
        float heightCoefficient = 1.0f / this.npc.getNpcTexture().getHeight();
        int iconWidth = 32;
        int iconHeight = 32;
        int posX = guiLeft + 8;
        int posY = guiTop + 8;
        int texX = this.npc.getForm().getIconTextureOffsetX();
        int texY = this.npc.getForm().getIconTextureOffsetY();
        int texWidth = this.npc.getForm().getIconTextureWidth();
        int texHeight = this.npc.getForm().getIconTextureHeight();
        boolean disableIcon = false;
        
        int textureIndex = this.npc.getNpcTexture().getImageIndex();
        if ( textureIndex < 0 )
        {
            disableIcon = true;
        }
        
        if ( !disableIcon )
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0f, 0f, 1f);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIndex);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_I(0xffffff);
            tessellator.addVertexWithUV(posX + 0, posY + iconHeight, -1.0f, (texX + 0) * widthCoefficient, (texY + texHeight) * heightCoefficient);
            tessellator.addVertexWithUV(posX + iconWidth, posY + iconHeight, -1.0f, (texX + texWidth) * widthCoefficient, (texY + texHeight) * heightCoefficient);
            tessellator.addVertexWithUV(posX + iconWidth, posY + 0, -1.0f, (texX + texWidth) * widthCoefficient, (texY + 0) * heightCoefficient);
            tessellator.addVertexWithUV(posX + 0, posY + 0, -1.0f, (texX + 0) * widthCoefficient, (texY + 0) * heightCoefficient);
            tessellator.draw();
            GL11.glPopMatrix();
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        // Esc or inventory key
        if (par2 == Keyboard.KEY_ESCAPE || par2 == mc.gameSettings.keyBindInventory.keyCode)
        {
            closeScreen();
            return;
        }
    }
    
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
    }
    
    private void closeScreen()
    {
        this.mc.thePlayer.closeScreen();
    }
    
    @Override
    public void onGuiClosed()
    {
        this.npc.clearGuiHolder();
        PacketNpcGuiCloseEvent packet = new PacketNpcGuiCloseEvent();
        packet.sendPacketToServer(this.mc.theWorld, this.npc, false);
    	super.onGuiClosed();
    }
}
