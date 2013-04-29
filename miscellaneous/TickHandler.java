package takap.mods.nnnpc.miscellaneous;

import java.util.EnumSet;

import takap.mods.nnnpc.party.PartyManager;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler
{
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if ( type.equals(EnumSet.of(TickType.RENDER)) )
        {
            if(Minecraft.getMinecraft().theWorld != null)
            {
                PartyManager.getInstance().displayFollowersIcon(Minecraft.getMinecraft());
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.RENDER);
    }

    @Override
    public String getLabel()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
