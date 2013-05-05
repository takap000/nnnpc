package takap.mods.nnnpc.extension.slim;

import takap.mods.nnnpc.entity.FormManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod(
    modid = "NNNPC_Slim",
    name  = "NNNPC_Slim",
    version = "1.5.2.01"
)
@NetworkMod(
    clientSideRequired = true,
    serverSideRequired = false
)

public class ExtensionFormSlim
{
    @Instance("NNNPC_Slim")
    public static ExtensionFormSlim instance;

    @Init
    public void init(FMLInitializationEvent event)
    {
        FormManager.getInstance().registerForm(FormSlim.getInstance());
    }
}

