package takap.mods.nnnpc.entity;

import takap.mods.nnnpc.gui.GuiNpcSetting;
import takap.mods.nnnpc.gui.GuiSubpageBase;
import takap.mods.nnnpc.gui.GuiSubpageRoleForFollower;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;


public class RoleFollower extends RoleBase
{
    public RoleFollower()
    {
        super("Follower");
    }

    @Override
    protected void createModeList()
    {
        this.modeList.add(EnumMode.WAIT);
        this.modeList.add(EnumMode.FOLLOW);
    }

    @Override
    public GuiSubpageBase getGuiSubpage(GuiNpcSetting guiNpcSetting,
            EntityPlayer entityPlayer, EntityNpc npc, Minecraft mc, int guiTop,
            int guiBottom, int guiLeft, int guiRight, int top, int bottom,
            int right, int left)
    {
        return new GuiSubpageRoleForFollower(guiNpcSetting, entityPlayer, npc, mc, guiTop, guiBottom, guiLeft, guiRight, top, bottom, right, left);
    }
    
    @Override
    public boolean canAttack()
    {
        return true;
    }
}
