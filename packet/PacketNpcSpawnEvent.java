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

public class PacketNpcSpawnEvent extends PacketBase
{
    public static final String channel = Utility.spawnEventChannelName;
    
    public PacketNpcSpawnEvent()
    {
        super(PacketNpcSpawnEvent.channel);
        this.canSendReponse = false;
    }
    
    public PacketNpcSpawnEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcSpawnEvent.channel, packet);
    }

    @Override
    public void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcSpawnEvent.writeRequestParameter");
            return;
        }
        
        writeString(npc.getForm().getName(), dos);
        dos.writeDouble(npc.posX);
        dos.writeDouble(npc.posY);
        dos.writeDouble(npc.posZ);
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        npc.writeEntityToNBT(nbtTagCompound);
        writeNBTTagCompound(nbtTagCompound, dos);
    }

    @Override
    public void writeResponseParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
    }
    
    @Override
    public void readRequestParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
        String formName = readString(dis);
        double posX = dis.readDouble();
        double posY = dis.readDouble();
        double posZ = dis.readDouble();
        npc = FormManager.getInstance().getForm(formName).getDefaultNpc(world, posX, posY, posZ, 0f, 0f);
        NBTTagCompound nbtTagCompound = readNBTTagCompound(dis);
        npc.readEntityFromNBT(nbtTagCompound);
        npc.setLocationAndAngles(npc.posX, npc.posY, npc.posZ, npc.rotationYaw, npc.rotationPitch);
        world.spawnEntityInWorld(npc);
    }
    
    @Override
    public void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
    }
}
