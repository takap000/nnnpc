package takap.mods.nnnpc.gui;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class GuiSubpageRoleForFollower extends GuiSubpageBase
{
    private final int labelOffsetX = 5;
    private final int labelOffsetY = 12;

    public GuiSubpageRoleForFollower(GuiNpcSetting mainPage, EntityPlayer player, EntityNpc npc, Minecraft mc, int guiTop, int guiBottom, int guiLeft, int guiRight, int top, int bottom, int left, int right)
    {
        super(mainPage, player, npc, mc, guiTop, guiBottom, guiLeft, guiRight, top, bottom, left, right);
    }

    @Override
    public void createButtonList()
    {
    }
    
    @Override
    public void drawForegroundLayer()
    {
        drawString("Not used", this.labelOffsetX , this.labelOffsetY, Utility.colorWhite, true);
    }
    
    @Override
    public boolean actionPerformed(int id)
    {
        return false;
    }

    @Override
    public String getSubpageTitle()
    {
        return "Role";
    }

    @Override
    public boolean keyTyped(char par1, int par2)
    {
        return false;
    }

    @Override
    public boolean updateScreen()
    {
        return false;
    }

    @Override
    public boolean mouseClicked(int x, int y, int z)
    {
        return false;
    }
}
