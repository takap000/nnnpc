package takap.mods.nnnpc.party;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import takap.mods.nnnpc.entity.RoleBase;
import takap.mods.nnnpc.packet.PacketNpcModeChangeEvent;
import takap.mods.nnnpc.packet.PacketNpcTeleportEvent;
import takap.mods.nnnpc.texture.Texture;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;

public class PartyManager
{
    private static PartyManager instance = new PartyManager();
    private List<EntityNpc> memberList;
    private boolean isIconHiding;
    private final int initialIconOffsetX = 8;
    private final int initialIconOffsetY = 8;
    
    private PartyManager()
    {
        this.memberList = new ArrayList<EntityNpc>();
        this.isIconHiding = false;
    }
    
    public static PartyManager getInstance()
    {
        return instance;
    }

    public void switchIcon()
    {
        this.isIconHiding = !this.isIconHiding;
        if ( this.isIconHiding )
        {
        	Minecraft.getMinecraft().thePlayer.addChatMessage("hide follower icons");
        }
        else
        {
        	Minecraft.getMinecraft().thePlayer.addChatMessage("show follower icons");
        }
    }

    public void setWait()
    {
        for ( int i=0; i<this.memberList.size(); i++ )
        {
            EntityNpc npc = this.memberList.get(i);
            if ( npc.getOwnerName().equals(Minecraft.getMinecraft().thePlayer.username) )
            {
            	npc.setMode(EnumMode.WAIT);
                PacketNpcModeChangeEvent packet = new PacketNpcModeChangeEvent();
                packet.sendPacketToServer(npc.worldObj, npc, false);
            }
        }
    	Minecraft.getMinecraft().thePlayer.addChatMessage("set followers to \"Wait\"");
    }

    public void setFollow()
    {
        int size = getNumberOfPartyMembers();
        for ( int i=0; i<size; i++ )
        {
            EntityNpc npc = this.memberList.get(i);
            if ( npc.getOwnerName().equals(Minecraft.getMinecraft().thePlayer.username) )
            {
            	npc.setMode(EnumMode.FOLLOW);
                PacketNpcModeChangeEvent packet = new PacketNpcModeChangeEvent();
                packet.sendPacketToServer(npc.worldObj, npc, false);
            }
        }
        if ( size > 0 )
        {
            Minecraft.getMinecraft().thePlayer.addChatMessage("set followers to \"Follow\"");
        }
    }

    public void summonFollowers()
    {
    	EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        int size = getNumberOfPartyMembers();
        for ( int i=0; i<size; i++ )
        {
            EntityNpc npc = this.memberList.get(i);
            if ( npc.getOwnerName().equals(player.username) )
            {
                PacketNpcTeleportEvent packet = new PacketNpcTeleportEvent();
                packet.sendPacketToServer(npc.worldObj, npc, false);
                npc.showSmoke(5);
            }
        }
        if ( size > 0 )
        {
            Minecraft.getMinecraft().thePlayer.addChatMessage("summon followers");
        }
    }

    public void registerNpc(EntityNpc npc)
    {
        int size = getNumberOfPartyMembers();
        for ( int i=0; i<size; i++ )
        {
            if ( npc.getNpcId().equals(memberList.get(i).getNpcId()) )
            {
                this.memberList.remove(i);
                break;
            }
        }
        this.memberList.add(npc);
    }

    public void deleteNpc(EntityNpc npc)
    {
        this.memberList.remove(npc);
    }

    public void displayFollowersIcon(Minecraft minecraft)
    {
        if ( !minecraft.theWorld.isRemote )
        {
            return;
        }
        ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        int windowWidth = scaledresolution.getScaledWidth();
        int windowHeight = scaledresolution.getScaledHeight();
        checkNpcExistence(minecraft);
        if ( this.isIconHiding || minecraft.currentScreen != null )
        {
            return;
        }
        int size = getNumberOfPartyMembers();
        for ( int i=0, j=0; i<size; i++ )
        {
            EntityNpc npc = this.memberList.get(i);
            if ( npc.getOwnerName().equals(minecraft.thePlayer.username) )
            {
                renderNpcIcon(minecraft, npc, j++, windowWidth, windowHeight);
            }
        }
    }

    private void checkNpcExistence(Minecraft minecraft)
    {
        int size = getNumberOfPartyMembers();
        for ( int i=0; i<size; i++ )
        {
            EntityNpc npc = this.memberList.get(i);
            // 生存チェック, party参加可能なroleかチェック, 同じworldかチェック
            if ( npc == null || npc.isDead || !npc.getRole().canFollow() || !npc.worldObj.equals(minecraft.theWorld) )
            {
                this.memberList.remove(i);
                return;
            }
        }
    }

    public void renderNpcIcon(Minecraft mc, EntityNpc npc, int index, int windowWidth, int windowHeight)
    {
        Texture texture = npc.getNpcTexture();
        if ( texture == null )
        {
            return;
        }
        
        float widthCoefficient = 1.0F / (float)texture.getWidth();
        float heightCoefficient = 1.0F / (float)texture.getHeight();
        int iconWidth = 16;
        int iconHeight = 16;
        int posX = initialIconOffsetX;
        int posY = initialIconOffsetY + index*(iconHeight + 16);
        int texX = npc.getForm().getIconTextureOffsetX();
        int texY = npc.getForm().getIconTextureOffsetY();
        int texWidth = npc.getForm().getIconTextureWidth();
        int texHeight = npc.getForm().getIconTextureHeight();
        int lifeBarWidth = 32;
        int lifeBarHeight = 4;
        int supportWidth = iconWidth + lifeBarWidth + 6;
        int supportHeight = iconHeight + 4;
        boolean disableIcon = false;
        
        if ( texture.getImageIndex() < 0 )
        {
            disableIcon = true;
        }
        GL11.glPushMatrix();
        // support
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // アイコン領域にダメージエフェクト
        if ( npc.hurtTime > 0 || npc.deathTime > 0 )
        {
            GL11.glColor4f(0.8f, 0.0f, 0.0f, 0.5f);
        }
        else
        {
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
        }
        tessellator.startDrawingQuads();
        tessellator.addVertex(posX - 2, posY - 2, -2.0d);
        tessellator.addVertex(posX - 2, posY + supportHeight - 2, -2.0d);
        tessellator.addVertex(posX + supportWidth - 2, posY + supportHeight - 2, -2.0d);
        tessellator.addVertex(posX + supportWidth - 2, posY - 2, -2.0d);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        // icon
        if ( !disableIcon )
        {
        	GL11.glPushMatrix();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getImageIndex());
            // glBindTexture()を呼びだしたらboundTextureのリセットを忘れずに！
            mc.renderEngine.resetBoundTexture();
            tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_I(0xffffff);
            tessellator.addVertexWithUV(posX + 0, posY + iconHeight, -1.0f, (float)(texX + 0) * widthCoefficient, (float)(texY + texHeight) * heightCoefficient);
            tessellator.addVertexWithUV(posX + iconWidth, posY + iconHeight, -1.0f, (float)(texX + texWidth) * widthCoefficient, (float)(texY + texHeight) * heightCoefficient);
            tessellator.addVertexWithUV(posX + iconWidth, posY + 0, -1.0f, (float)(texX + texWidth) * widthCoefficient, (float)(texY + 0) * heightCoefficient);
            tessellator.addVertexWithUV(posX + 0, posY + 0, -1.0f, (float)(texX + 0) * widthCoefficient, (float)(texY + 0) * heightCoefficient);
            tessellator.draw();
            GL11.glPopMatrix();
        }
        // lifeBar
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(npc.getLifeColorR(), npc.getLifeColorG(), npc.getLifeColorB(), 1.0f);
        int lifeBarLength = lifeBarWidth * npc.getHealth() / npc.getMaxHealth();
        tessellator.startDrawingQuads();
        tessellator.addVertex(posX + iconWidth + 2, posY + iconHeight - lifeBarHeight, -1.0d);
        tessellator.addVertex(posX + iconWidth + 2, posY + iconHeight, -1.0d);
        tessellator.addVertex(posX + iconWidth + lifeBarLength + 2, posY + iconHeight, -1.0d);
        tessellator.addVertex(posX + iconWidth + lifeBarLength + 2, posY + iconHeight - lifeBarHeight , -1.0d);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // life
        mc.fontRenderer.drawStringWithShadow(Integer.toString(npc.getHealth()) + "/" + Integer.toString(npc.getMaxHealth()), posX + iconWidth + 2, posY + 1, Utility.colorWhite);
        // name
        mc.fontRenderer.drawStringWithShadow(npc.getNpcName(), posX, posY + iconHeight + 2, Utility.colorWhite);
        GL11.glPopMatrix();
    }
    
    public int getNumberOfPartyMembers()
    {
        return this.memberList.size();
    }
    
    public List<EntityNpc> getPartyMemberList()
    {
        return memberList;
    }
}
