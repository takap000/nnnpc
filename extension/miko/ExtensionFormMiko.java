package takap.mods.nnnpc.extension.miko;

import takap.mods.nnnpc.entity.FormManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod(
    modid = "NNNPC_Miko",
    name  = "NNNPC_Miko",
    version = "1.5.1.01"
)
@NetworkMod(
    clientSideRequired = true,
    serverSideRequired = false
)

public class ExtensionFormMiko
{
    @Instance("NNNPC_Miko")
    public static ExtensionFormMiko instance;

    @Init
    public void init(FMLInitializationEvent event)
    {
        FormManager.getInstance().registerForm(FormMiko.getInstance());
    }
}

