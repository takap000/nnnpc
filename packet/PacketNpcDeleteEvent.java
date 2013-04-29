package takap.mods.nnnpc.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketNpcDeleteEvent extends PacketBase
{
    public static final String channel = Utility.deleteEventChannelName;
    private boolean canSpawnEffect;
    private boolean canDropItems;
    
    public PacketNpcDeleteEvent(boolean canSpawnEffect, boolean canDropItems)
    {
        super(PacketNpcDeleteEvent.channel);
        this.canSendReponse = false;
        this.canSendReponse = canSpawnEffect;
        this.canDropItems = canDropItems;
    }
    
    public PacketNpcDeleteEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcDeleteEvent.channel, packet);
        this.canSendReponse = false;
        this.canDropItems = false;
    }

    @Override
    public void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcDeleteEvent.writeRequestParameter");
            return;
        }
        
        // 先にclient側を削除, request書き込み時に実施
        npc.deleteByPlayer(this.canSpawnEffect, false);
        
        // アイテムドロップの有無だけ通知
        dos.writeBoolean(this.canDropItems);
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
            Utility.printWarning("npc == null @ PacketNpcDeleteEvent.readRequestParameter");
            return;
        }
        
        // server側処理，request読み込み時に処理
        this.canDropItems = dis.readBoolean();
        npc.deleteByPlayer(this.canSpawnEffect, this.canDropItems);
    }
    
    @Override
    public void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
    }
}
