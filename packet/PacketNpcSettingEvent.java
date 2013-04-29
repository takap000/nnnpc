package takap.mods.nnnpc.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketNpcSettingEvent extends PacketBase
{
    public static final String channel = Utility.settingEventChannelName;
    
    public PacketNpcSettingEvent()
    {
        super(PacketNpcSettingEvent.channel);
    }
    
    public PacketNpcSettingEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcSettingEvent.channel, packet);
    }

    @Override
    public void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcSettingEvent.writeRequestParameter");
            return;
        }
        
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        npc.writeEntityToNBT(nbtTagCompound);
        writeNBTTagCompound(nbtTagCompound, dos);
    }

    @Override
    public void writeResponseParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcSettingEvent.writeResponseParameter");
            return;
        }
        
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        npc.writeEntityToNBT(nbtTagCompound);
        writeNBTTagCompound(nbtTagCompound, dos);
    }
    
    @Override
    public void readRequestParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcSettingEvent.readRequestParameter");
            return;
        }
        
        NBTTagCompound nbtTagCompound = readNBTTagCompound(dis);
        npc.readEntityFromNBT(nbtTagCompound);
    }
    
    @Override
    public void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcSettingEvent.readResponseParameter");
            return;
        }
        
        NBTTagCompound nbtTagCompound = readNBTTagCompound(dis);
        npc.readEntityFromNBT(nbtTagCompound);
    }
}
