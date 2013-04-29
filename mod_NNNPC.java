package takap.mods.nnnpc;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import takap.mods.nnnpc.entity.FormDefault;
import takap.mods.nnnpc.entity.FormManager;
import takap.mods.nnnpc.gui.GuiHandler;
import takap.mods.nnnpc.item.ItemSoulStone;
import takap.mods.nnnpc.item.ItemNiceStick;
import takap.mods.nnnpc.miscellaneous.NNNPCKeyHandler;
import takap.mods.nnnpc.miscellaneous.TickHandler;
import takap.mods.nnnpc.packet.PacketHandler;
import takap.mods.nnnpc.utility.Utility;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;


@Mod(
    modid = "NNNPC",
    name  = "NNNPC",
    version = "1.5.1.06"
)
@NetworkMod(
    clientSideRequired = true,
    serverSideRequired = false,
    channels = {
            Utility.initializeEventChannelName,
            Utility.spawnEventChannelName,
            Utility.deleteEventChannelName,
            Utility.settingEventChannelName,
            Utility.lifeSettingEventChannelName,
            Utility.swingEventChannelName,
            Utility.modeChangeEventChannelName,
            Utility.teleportEventChannelName,
            Utility.guiCloseEventChannelName
    },
    packetHandler = PacketHandler.class
)

public class mod_NNNPC
{
    @Instance("NNNPC")
    public static mod_NNNPC instance;
    private final int soulStoneDefaultItemID = 3939;
    private final int niceStickDefaultItemID = 3940;
    private int soulStoneItemID;
    private int niceStickItemID;
    public static Item soulStone;
    public static Item niceStick;
    private final String defaultNNNpcDirectoryPath = "mods/NNNPC";
    private final String textureDirectoryName = "NPCTexture";
    private final String tempDirectoryName = "Temp";
    private final String locationSSDirectoryName = "LocationSS";
    private final String configFileName = "config/NNNPC.cfg";
    public static int debugLv = 4;
    public static String nnnpcDirectoryPath;
    public static String textureDirectoryPath;
    public static String tempDirectoryPath;
    public static String locationSSDirectoryPath;
    public static GuiHandler guiHandler;

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        soulStoneItemID = soulStoneDefaultItemID;
        niceStickItemID = niceStickDefaultItemID;
        textureDirectoryPath = null;
        loadConfigFile(configFileName);
        registerForms();
    }
    
    @Init
    public void init(FMLInitializationEvent event)
    {
    	guiHandler = new GuiHandler();
    	
    	// Soul Stone
        soulStone = (new ItemSoulStone(soulStoneItemID)).setUnlocalizedName("nnnpc:soulstone").setCreativeTab(CreativeTabs.tabMisc);
        LanguageRegistry.addName(soulStone, "Soul Stone");
        GameRegistry.addRecipe(new ItemStack(soulStone, 1),
            new Object[] {
                " s ",
                "+o+",
                " m ",
                Character.valueOf('s'), Item.spiderEye,
                Character.valueOf('o'), Item.enderPearl,
                Character.valueOf('+'), Item.bone,
                Character.valueOf('m'), Item.rottenFlesh
            }
        );
        boolean debugFlag = false;
        if ( debugFlag )
        {
            GameRegistry.addRecipe(new ItemStack(soulStone, 8),
                new Object[] {
                    "+ +",
                    Character.valueOf('+'), Item.bone
                }
            );
        }
        // Nice Stick
        niceStick = (new ItemNiceStick(niceStickItemID)).setUnlocalizedName("nnnpc:nicestick").setCreativeTab(CreativeTabs.tabMisc);
        LanguageRegistry.addName(niceStick, "Nice Stick");
        GameRegistry.addRecipe(new ItemStack(niceStick, 1),
            new Object[] {
                "  o",
                " I ",
                "I  ",
                Character.valueOf('o'), mod_NNNPC.soulStone,
                Character.valueOf('I'), Item.blazeRod
            }
        );
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
        TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
        KeyBindingRegistry.registerKeyBinding(new NNNPCKeyHandler());
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        // 登録されたモデルの情報表示(debug)
        FormManager.getInstance().showRegisteredFormInformation();
    }
    
    private void loadConfigFile(String s)
    {
        // コンフィグファイルの存在チェック，なければ作成
        String configFileAbsolutePath = (new File(Minecraft.getMinecraftDir(), s)).getAbsolutePath();
        File configFile = new File(configFileAbsolutePath);
        if ( !configFile.exists() )
        {
            try
            {
                configFile.createNewFile();
            }
            catch ( IOException e )
            {
                Utility.printWarning("failed to create config file ... @ mod_NNNPC :\n    " + e);
                return;
            }
        }
        
        // コンフィグファイルの内容チェック
        Properties properties = new Properties();
        try
        {
            // "path"パラメタをチェック
            properties.load(new InputStreamReader(new FileInputStream(configFileAbsolutePath), System.getProperty("file.encoding")));
            String path = properties.getProperty("path");
            if ( (path == null) || path.equals("") )
            {
                nnnpcDirectoryPath = Minecraft.getMinecraftDir() + "/" + this.defaultNNNpcDirectoryPath;
                properties.setProperty("path", (new File(nnnpcDirectoryPath)).getAbsolutePath());
                properties.store(new OutputStreamWriter(new FileOutputStream(configFileAbsolutePath), System.getProperty("file.encoding")), null);
            }
            else
            {
                nnnpcDirectoryPath = path;
            }
            textureDirectoryPath = nnnpcDirectoryPath + "/" + this.textureDirectoryName;
            tempDirectoryPath = nnnpcDirectoryPath + "/" + this.tempDirectoryName;
            locationSSDirectoryPath = nnnpcDirectoryPath + "/" + this.locationSSDirectoryName;
            Utility.printInformation("texture path    :" + textureDirectoryPath);
            Utility.printInformation("temppath        :" + tempDirectoryPath);
            Utility.printInformation("location SS path:" + locationSSDirectoryPath);
            // "id"パラメタをチェック
            String id = properties.getProperty("id");
            if ( id != null && !id.equals("") )
            {
                try
                {
                    soulStoneItemID = Integer.parseInt(id);
                }
                catch ( NumberFormatException e )
                {
                    Utility.printAlert("invalid parameter ... \"id=" + id + "\"");
                }
            }
        }
        catch ( IOException e )
        {
            Utility.printWarning("failed to load/save config ... @ mod_NNNPC :\n    " + e);
            return;
        }
    }
    
    private void registerForms()
    {
        // 他のモデルはextensionとして追加
        FormManager.getInstance().registerForm(FormDefault.getInstance());
    }
}

