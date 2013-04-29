package takap.mods.nnnpc.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketNpcModeChangeEvent extends PacketBase
{
    public static final String channel = Utility.modeChangeEventChannelName;
    
    public PacketNpcModeChangeEvent()
    {
        super(PacketNpcModeChangeEvent.channel);
    }
    
    public PacketNpcModeChangeEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcModeChangeEvent.channel, packet);
    }

    @Override
    public void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcModeChangeEvent.writeRequestParameter");
            return;
        }
        
        writeString(npc.getMode().toString(), dos);
    }

    @Override
    public void writeResponseParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcModeChangeEvent.writeResponseParameter");
            return;
        }
        
        writeString(npc.getMode().toString(), dos);
    }
    
    @Override
    public void readRequestParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcModeChangeEvent.readRequestParameter");
            return;
        }
        
        npc.setModeByName(readString(dis));
    }
    
    @Override
    public void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcModeChangeEvent.readResponseParameter");
            return;
        }
        
        npc.setModeByName(readString(dis));
    }
}
