package fr.minemobs.aetheruhcutils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = MainAetherUHC.MODID, name = MainAetherUHC.NAME, version = MainAetherUHC.VERSION)
public class MainAetherUHC
{
    public static final String MODID = "aetheruhcutils";
    public static final String NAME = "Aether UHC Utils";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(new Events());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
}
