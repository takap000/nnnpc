package takap.mods.nnnpc.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketNpcInitializeEvent extends PacketBase
{
    public static final String channel = Utility.initializeEventChannelName;
    
    public PacketNpcInitializeEvent()
    {
        super(PacketNpcInitializeEvent.channel);
    }
    
    public PacketNpcInitializeEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcInitializeEvent.channel, packet);
    }

    @Override
    public void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
    }

    @Override
    public void writeResponseParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcInitializeEvent.writeResponseParameter");
            return;
        }
        
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        npc.writeEntityToNBT(nbtTagCompound);
        writeNBTTagCompound(nbtTagCompound, dos);
    }
    
    @Override
    public void readRequestParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
    }
    
    @Override
    public void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcInitializeEvent.readResponseParameter");
            return;
        }
        
        NBTTagCompound nbtTagCompound = readNBTTagCompound(dis);
        npc.readEntityFromNBT(nbtTagCompound);
    }
}
