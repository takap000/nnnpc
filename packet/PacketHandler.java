package takap.mods.nnnpc.packet;

import takap.mods.nnnpc.utility.Utility;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.entity.player.*;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager,
            Packet250CustomPayload packet, Player player)
    {
        PacketBase packetBase = null;
        World world = ((EntityPlayer)player).worldObj;
        Utility.printDebugInformation("received packet channel:" + packet.channel + ", size:" + packet.length);
        
        // なんか  もっと  いい  判別方法が  あるんじゃ  なかろうか  ？  staticで多態性的ななにか...
        if ( packet.channel.equals(PacketNpcInitializeEvent.channel) )
        {
            packetBase = new PacketNpcInitializeEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcSettingEvent.channel) )
        {
            packetBase = new PacketNpcSettingEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcDeleteEvent.channel) )
        {
            packetBase = new PacketNpcDeleteEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcSpawnEvent.channel) )
        {
            packetBase = new PacketNpcSpawnEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcLifeSettingEvent.channel) )
        {
            packetBase = new PacketNpcLifeSettingEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcSwingEvent.channel) )
        {
            packetBase = new PacketNpcSwingEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcHealEvent.channel) )
        {
            packetBase = new PacketNpcHealEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcModeChangeEvent.channel) )
        {
            packetBase = new PacketNpcModeChangeEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcTeleportEvent.channel) )
        {
            packetBase = new PacketNpcTeleportEvent(packet);
        }
        else if ( packet.channel.equals(PacketNpcGuiCloseEvent.channel) )
        {
            packetBase = new PacketNpcGuiCloseEvent(packet);
        }
        
        if ( packetBase != null )
        {
            packetBase.handlePacket(world);
        }
    }
}
