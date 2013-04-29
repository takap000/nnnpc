package takap.mods.nnnpc.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketNpcLifeSettingEvent extends PacketBase
{
    public static final String channel = Utility.lifeSettingEventChannelName;
    private int maxLife;
    private boolean isLifeVisible;
    
    public PacketNpcLifeSettingEvent(int maxLife, boolean isLifeVisible)
    {
        super(PacketNpcLifeSettingEvent.channel);
        this.maxLife = maxLife;
        this.isLifeVisible = isLifeVisible;
    }
    
    public PacketNpcLifeSettingEvent(Packet250CustomPayload packet)
    {
        super(PacketNpcLifeSettingEvent.channel, packet);
    }

    @Override
    public void writeRequestParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        // lifeの増分と可視化設定変更の有無を送信
        dos.writeInt(this.maxLife);
        dos.writeBoolean(this.isLifeVisible);
    }

    @Override
    public void writeResponseParameter(DataOutputStream dos, World world, EntityNpc npc) throws IOException
    {
        // Server側NPCの設定値を送信, lifeの現在値も送る
    	dos.writeInt(npc.getHealth());
        dos.writeInt(npc.getMaxHealth());
        dos.writeBoolean(npc.isLifeVisible());
    }
    
    @Override
    public void readRequestParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcLifeSettingEvent.readRequestParameter");
            return;
        }
        
        // 最大値設定変更
        int health = npc.getHealth() + dis.readInt();
        if ( health < npc.getLimitMinHealth() )
        {
            npc.setMaxHealth(npc.getLimitMinHealth());
        }
        else if ( health > npc.getLimitMaxHealth() )
        {
            npc.setMaxHealth(npc.getLimitMaxHealth());
        }
        else
        {
            npc.setMaxHealth(health);
        }
        // 可視化設定の有無チェック
        if ( dis.readBoolean() )
        {
            npc.setLifeVisible(!npc.isLifeVisible());
        }
    }
    
    @Override
    public void readResponseParameter(DataInputStream dis, World world, EntityNpc npc) throws IOException
    {
        if ( npc == null )
        {
            Utility.printWarning("npc == null @ PacketNpcLifeSettingEvent.readResponseParameter");
            return;
        }
        
        // 読み込んだ値をClientのNPCに設定
    	int health = dis.readInt();
    	int maxHealth = dis.readInt();
    	boolean isLifeVisible = dis.readBoolean();
    	npc.setEntityHealth(health);
        npc.setMaxHealth(maxHealth);
        npc.setLifeVisible(isLifeVisible);
    }
}
