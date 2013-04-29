package takap.mods.nnnpc.gui;

import java.util.List;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.inventory.ContainerNpcInventoryForSimple;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if ( ID == EnumGuiPage.INVENTORY.ordinal() )
        {
            EntityNpc npc = searchNpcAccessedBy(player, world);
            if ( npc != null )
            {
                return new ContainerNpcInventoryForSimple(player.inventory, npc.getInventory());
            }
            return null;
        }
        else if ( ID == EnumGuiPage.SETTING.ordinal() )
        {
            return null;
        }
        else if ( ID == EnumGuiPage.LOCATION.ordinal() )
        {
            return null;
        }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if ( ID == EnumGuiPage.INVENTORY.ordinal() )
        {
            EntityNpc npc = searchNpcAccessedBy(player, world);
            if ( npc != null )
            {
                return new GuiNpcInventory(player, npc);
            }
            return null;
        }
        else if ( ID == EnumGuiPage.SETTING.ordinal() )
        {
            EntityNpc npc = searchNpcAccessedBy(player, world);
            if ( npc != null )
            {
                return new GuiNpcSetting(player, npc);
            }
            return null;
        }
        else if ( ID == EnumGuiPage.LOCATION.ordinal() )
        {
            return new GuiLocationRecorder(x, y, z);
        }
        return null;
	}
    
    public static EntityNpc searchNpcAccessedBy(EntityPlayer player, World world)
    {
        EntityNpc npc = null;
        List loadedEntityList = world.getLoadedEntityList();
        for ( int i=0; i<loadedEntityList.size(); i++ )
        {
            if ( loadedEntityList.get(i) instanceof EntityNpc )
            {
                EntityNpc targetNpc = (EntityNpc)loadedEntityList.get(i);
                String guiHolder = targetNpc.getGuiHolder();
                if ( (guiHolder != null) && guiHolder.equals(player.username) )
                {
                    npc = targetNpc;
                    break;
                }
            }
        }
        return npc;
    }
}
