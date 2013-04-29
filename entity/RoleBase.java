package takap.mods.nnnpc.entity;

import java.util.ArrayList;

import takap.mods.nnnpc.gui.GuiNpcSetting;
import takap.mods.nnnpc.gui.GuiSubpageBase;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public abstract class RoleBase
{
    private String name;
    protected ArrayList<EnumMode> modeList;
    private boolean canFollow;
    
    public RoleBase(String name)
    {
        this.name = name;
        this.modeList = new ArrayList<EnumMode>();
        createModeList();
        this.canFollow = this.modeList.contains(EnumMode.FOLLOW);
    }
    
    public String getName()
    {
        return this.name;
    }
    
    protected abstract void createModeList();

    public ArrayList<EnumMode> getModeList()
    {
        return modeList;
    }
    
    public int getNumberOfModes()
    {
        return this.modeList.size();
    }
    
    public EnumMode getMode(int i)
    {
        if ( getNumberOfModes() > 0 )
        {
            return this.modeList.get(i);
        }
        return null;
    }
    
    public EnumMode getPreviousMode(EnumMode mode)
    {
        int modeIndex = getModeIndex(mode);
        if ( modeIndex == -1 )
        {
            return getDefaultMode();
        }
        else
        {
            int size = getNumberOfModes();
            return getMode((modeIndex-1+size) % size);
        }
    }
    
    public EnumMode getNextMode(EnumMode mode)
    {
        int modeIndex = getModeIndex(mode);
        if ( modeIndex == -1 )
        {
            return getDefaultMode();
        }
        else
        {
            return getMode((modeIndex+1) % getNumberOfModes());
        }
    }

    public EnumMode getDefaultMode()
    {
        if ( getNumberOfModes() > 0 )
        {
            return getMode(0);
        }
        return null;
    }
    
    public int getModeIndex(EnumMode mode)
    {
        for ( int i=0; i<getNumberOfModes(); i++ )
        {
            if ( this.modeList.get(i).equals(mode) )
            {
                return i;
            }
        }
        return -1;
    }

    public boolean canFollow()
    {
    	return this.canFollow;
    }
    
    public boolean canAttack()
    {
        return false;
    }
    
    public boolean canHeal()
    {
        return false;
    }
    
    public abstract GuiSubpageBase getGuiSubpage(GuiNpcSetting guiNpcSetting, EntityPlayer entityPlayer, EntityNpc npc, Minecraft mc, int guiTop, int guiBottom, int guiLeft, int guiRight, int top, int bottom, int right, int left);
}
