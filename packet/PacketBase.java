package takap.mods.nnnpc.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.inventory.InventoryNpc;

import cpw.mods.fml.common.network.PacketDispatcher;

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public abstract class PacketBase extends Packet250CustomPayload
{
    private String channelName;
    protected boolean canSendReponse;
    
    public PacketBase(String channel)
    {
        this.channelName = channel;
        this.canSendReponse = true;
    }
    
    public PacketBase(String channel, Packet250CustomPayload packet)
    {
        this(channel);
        this.data = packet.data;
        this.length = packet.length;
    }
    
    public abstract void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException;
    public abstract void writeResponseParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException;
    public abstract void readRequestParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException;
    public abstract void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException;
    
    protected ByteArrayOutputStream setPacketData(World world, EntityNpc npc, boolean isGuiEvent)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try
        {
            int entityId = (npc==null)?(-1):npc.getEntityId();
            dos.writeInt(entityId);
            dos.writeBoolean(isGuiEvent);
            if ( world.isRemote )
            {
                writeRequestParameter(dos, world, npc);
            }
            else
            {
                writeResponseParameter(dos, world, npc);
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return null;
        }
        return bos;
    }
    
    public Packet250CustomPayload createPacket(World world, EntityNpc npc, boolean isGuiEvent)
    {
        ByteArrayOutputStream bos = setPacketData(world, npc, isGuiEvent);
        if ( bos == null )
        {
            return null;
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = this.channelName;
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        return packet;
    }
    
    public void sendPacketToClient(World world, EntityNpc npc, boolean isGuiEvent)
    {
        Packet250CustomPayload packet = createPacket(world, npc, isGuiEvent);
        if ( packet != null )
        {
            PacketDispatcher.sendPacketToAllPlayers(packet);
        }
    }
    
    public void sendPacketToServer(World world, EntityNpc npc, boolean isGuiEvent)
    {
        Packet250CustomPayload packet = createPacket(world, npc, isGuiEvent);
        if ( packet != null )
        {
            PacketDispatcher.sendPacketToServer(packet);
        }
    }
    
    public void handlePacket(World world)
    {
        // 共通部(entityId, isGuiEvent)読み込み
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(this.data));
        int entityId;
        EntityNpc npc = null;
        boolean isGuiEvent;
        try
        {
            entityId = inputStream.readInt();
            isGuiEvent = inputStream.readBoolean();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return;
        }
        
        // Packet独自の処理を実施
        if ( entityId != -1)
        {
            npc = (EntityNpc)world.getEntityByID(entityId);
        }
        try
        {
            if ( world.isRemote )
            {
                readResponseParameter(inputStream, world, npc);
            }
            else
            {
                readRequestParameter(inputStream, world, npc);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        // TODO: Guiイベントの場合，Guiに反映？
        if ( isGuiEvent )
        {
            
        }
        
        // Server側でResponseを送信する必要がある場合はClientに返信
        if ( !world.isRemote && this.canSendReponse )
        {
            sendPacketToClient(world, npc, isGuiEvent);
        }
    }
    
    public static String readString(DataInputStream stream) throws IOException
    {
        StringBuilder str = new StringBuilder();
        short size = stream.readShort();
        if ( size <= 0 )
        {
            return null;
        }
        for ( int i=0; i<size; i++ )
        {
            str.append(stream.readChar());
        }
        return str.toString();
    }
    
    public static void writeInventoryData(InventoryNpc inventory, DataOutputStream dos) throws IOException
    {
        int inventorySize = inventory.getSizeInventory();
        dos.writeInt(inventorySize);
        ItemStack itemStack;
        for ( int i = 0; i < inventorySize; i++ )
        {
            itemStack = inventory.getStackInSlot(i);
            writeItemStack(itemStack, dos);
        }    
    }
    
    public static ItemStack[] readInventoryData(DataInputStream dis) throws IOException
    {
        int inventorySize = dis.readInt();
        if ( inventorySize < 0 )
        {
            return null;
        }
        ItemStack itemStack[] = new ItemStack[inventorySize];
        for ( int i=0; i<inventorySize; i++ )
        {
            itemStack[i] = readItemStack(dis);
        }
        return itemStack;
    }
}
