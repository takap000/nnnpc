package takap.mods.nnnpc.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketNpcHealEvent extends PacketBase
{
    public static final String channel = Utility.healEventChannelName;
    private double x;
    private double y;
    private double z;
    
    public PacketNpcHealEvent(double x, double y, double z)
    {
        super(PacketNpcHealEvent.channel);
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public PacketNpcHealEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcHealEvent.channel, packet);
    }

    @Override
    public void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
    }

    @Override
    public void writeResponseParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        dos.writeDouble(this.x);
        dos.writeDouble(this.y);
        dos.writeDouble(this.z);
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
            Utility.printWarning("npc == null @ PacketNpcHealEvent.readResponseParameter");
            return;
        }
        
        this.x = dis.readDouble();
        this.y = dis.readDouble();
        this.z = dis.readDouble();
        npc.showHealEffect(x, y, z);
    }
}
