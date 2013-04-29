package takap.mods.nnnpc.miscellaneous;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import takap.mods.nnnpc.party.PartyManager;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class NNNPCKeyHandler extends KeyHandler
{
    static KeyBinding followerIcon = new KeyBinding("Toggle Visibility of Party Icon", Keyboard.KEY_I);
    static KeyBinding standby = new KeyBinding("Set Party Members to \"Wait\"", Keyboard.KEY_Z);
    static KeyBinding active = new KeyBinding("Set Party Members to \"Follow\"", Keyboard.KEY_X);
    static KeyBinding summon = new KeyBinding("Summon Party Members", Keyboard.KEY_R);
    private boolean keyPressed;
    
    public NNNPCKeyHandler()
    {
        super(new KeyBinding[]{followerIcon, standby, active, summon}, new boolean[]{false, false, false, false});
        keyPressed = false;
    }

    @Override
    public String getLabel()
    {
        return "NNNpcKeyBind";
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb,
            boolean tickEnd, boolean isRepeat)
    {
        keyPressed = true;
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
    {
        if ( keyPressed && Minecraft.getMinecraft().currentScreen == null )
        {
        	if ( kb.equals(followerIcon) )
        	{
                PartyManager.getInstance().switchIcon();
        	}
        	else if ( kb.equals(standby) )
        	{
                PartyManager.getInstance().setWait();
        	}
        	else if ( kb.equals(active) )
        	{
                PartyManager.getInstance().setFollow();
        	}
        	else if ( kb.equals(summon) )
        	{
                PartyManager.getInstance().summonFollowers();
        	}
            keyPressed = false;
        }
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

}
