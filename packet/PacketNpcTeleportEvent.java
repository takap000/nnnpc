package takap.mods.nnnpc.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketNpcTeleportEvent extends PacketBase
{
    public static final String channel = Utility.teleportEventChannelName;
    
    public PacketNpcTeleportEvent()
    {
        super(PacketNpcTeleportEvent.channel);
    }
    
    public PacketNpcTeleportEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcTeleportEvent.channel, packet);
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
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcTeleportEvent.readRequestParameter");
            return;
        }
        
        npc.teleportToOwner();
    }
    
    @Override
    public void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
    }
}
