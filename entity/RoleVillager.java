package takap.mods.nnnpc.entity;

import takap.mods.nnnpc.gui.GuiNpcSetting;
import takap.mods.nnnpc.gui.GuiSubpageBase;
import takap.mods.nnnpc.gui.GuiSubpageRoleForRouteTracer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;


public class RoleVillager extends RoleBase
{
    public RoleVillager()
    {
        super("Villager");
    }

    @Override
    protected void createModeList()
    {
        this.modeList.add(EnumMode.WAIT);
        this.modeList.add(EnumMode.TRACEROUTE);
        this.modeList.add(EnumMode.SIT);
        this.modeList.add(EnumMode.FREEDOM);
    }

    @Override
    public GuiSubpageBase getGuiSubpage(GuiNpcSetting guiNpcSetting,
            EntityPlayer entityPlayer, EntityNpc npc, Minecraft mc, int guiTop,
            int guiBottom, int guiLeft, int guiRight, int top, int bottom,
            int right, int left)
    {
        return new GuiSubpageRoleForRouteTracer(guiNpcSetting, entityPlayer, npc, mc, guiTop, guiBottom, guiLeft, guiRight, top, bottom, right, left);
    }
}
