package takap.mods.nnnpc.utility;

import java.io.File;
import java.io.IOException;
import java.util.List;

import takap.mods.nnnpc.mod_NNNPC;
import takap.mods.nnnpc.entity.EntityNpc;

import net.minecraft.world.World;

public final class Utility
{
    public static final String initializeEventChannelName      = "NNNPC_init";
    public static final String spawnEventChannelName           = "NNNPC_spawn";
    public static final String deleteEventChannelName          = "NNNPC_delete";
    public static final String settingEventChannelName         = "NNNPC_setting";
    public static final String lifeSettingEventChannelName     = "NNNPC_life";
    public static final String swingEventChannelName           = "NNNPC_swing";
    public static final String healEventChannelName            = "NNNPC_heal";
    public static final String modeChangeEventChannelName      = "NNNPC_mode";
    public static final String teleportEventChannelName        = "NNNPC_teleport";
    public static final String guiCloseEventChannelName        = "NNNPC_close";
    
    public static final int colorBlack   = 0x000000;
    public static final int colorWhite   = 0xffffff;
    public static final int colorRed     = 0xff0000;
    public static final int colorGreen   = 0x00ff00;
    public static final int colorBlue    = 0x0000ff;
    public static final int colorYellow  = 0xffff00;
    public static final int colorDimGray = 0x696969;
    
    public static final String packageName                   = "/takap/mods/nnnpc";
    public static final String settingPageBackgroundImage    = packageName + "/image/background.png";
    public static final String settingSubpageBackgroundImage = packageName + "/image/subpagebg.png";
    public static final String menuButtonBackgroundImage     = packageName + "/image/button.png";
    public static final String inventoryBackgroundImage      = packageName + "/image/simply_options.png";
    public static final String smallButtonBackgroundImage    = packageName + "/image/smallbutton.png";
    public static final String utilityButtonBackgroundImage  = packageName + "/image/utilitybutton.png";
    
    public static final String leftHanded = "left hand";
    public static final String rightHanded = "right hand";
    
    private static File tempDirectory = initializeTempDirectory();
    
    private Utility(){}
    
    private static File initializeTempDirectory()
    {
        File file = new File(mod_NNNPC.tempDirectoryPath);
        if ( !file.exists() )
        {
            try
            {
                file.createNewFile();
            }
            catch ( IOException e )
            {
                return null;
            }
        }
        return file;
    }

    public static EntityNpc searchNpcByNpcId(World world, String id)
    {
        EntityNpc npc = null;
        List loadedEntityList = world.getLoadedEntityList();
        for ( int i=0; i<loadedEntityList.size(); i++ )
        {
            if ( loadedEntityList.get(i) instanceof EntityNpc )
            {
                EntityNpc targetNpc = (EntityNpc)loadedEntityList.get(i);
                if ( targetNpc.getNpcId().equals(id) )
                {
                    npc = targetNpc;
                    break;
                }
            }
        }
        return npc;
    }
    
    public static File getTempDirectory()
    {
        return tempDirectory;
    }
    
    public static void printConsoleMessage(String lv, String message)
    {
        System.out.println("[NNNPC(Lv:" + lv + ")] " + message);
    }
    
    // 再起動必須のエラーなどに
    public static void printError(String s)
    {
        if ( mod_NNNPC.debugLv > 0 )
        {
            printConsoleMessage("E", s);
        }
    }
    
    // ファイル操作など，落とせない例外などに
    public static void printWarning(String s)
    {
        if ( mod_NNNPC.debugLv > 1 )
        {
            printConsoleMessage("W", s);
        }
    }
    
    // イベント発生状況やステータス表示などに
    public static void printInformation(String s)
    {
        if ( mod_NNNPC.debugLv > 2 )
        {
            printConsoleMessage("I", s);
        }
    }
    
    // 異常ルート検出用などに
    public static void printDebugInformation(String s)
    {
        if ( mod_NNNPC.debugLv > 3 )
        {
            printConsoleMessage("D", s);
        }
    }
    
    // プレイ中に表示させたい情報に
    public static void printAlert(String s)
    {
        System.out.println("[NNNPC] " + s);
    }
}
