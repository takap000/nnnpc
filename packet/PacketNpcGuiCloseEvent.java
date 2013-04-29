package takap.mods.nnnpc.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.FormManager;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketNpcGuiCloseEvent extends PacketBase
{
    public static final String channel = Utility.guiCloseEventChannelName;
    
    public PacketNpcGuiCloseEvent()
    {
        super(PacketNpcGuiCloseEvent.channel);
        this.canSendReponse = false;
    }
    
    public PacketNpcGuiCloseEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcGuiCloseEvent.channel, packet);
    }

    @Override
    public void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
    }

    @Override
    public void writeResponseParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
    }
    
    @Override
    public void readRequestParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
    	if ( npc != null )
    	{
    		npc.clearGuiHolder();
    	}
    }
    
    @Override
    public void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
    }
}
