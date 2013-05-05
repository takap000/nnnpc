package takap.mods.nnnpc.extension.majokko;

import takap.mods.nnnpc.entity.FormManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod(
    modid = "NNNPC_Majokko",
    name  = "NNNPC_Majokko",
    version = "1.5.2.01"
)
@NetworkMod(
    clientSideRequired = true,
    serverSideRequired = false
)

public class ExtensionFormMajokko
{
    @Instance("NNNPC_Majokko")
    public static ExtensionFormMajokko instance;

    @Init
    public void init(FMLInitializationEvent event)
    {
        FormManager.getInstance().registerForm(FormMajokko.getInstance());
    }
}

