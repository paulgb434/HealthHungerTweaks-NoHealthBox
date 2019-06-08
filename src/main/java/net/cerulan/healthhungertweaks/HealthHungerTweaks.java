package net.cerulan.healthhungertweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.cerulan.healthhungertweaks.handler.ConfigHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION, dependencies="required-after:applecore@[2.2.2,)", updateJSON="https://raw.githubusercontent.com/CerulanLumina/HealthHungerTweaks/master/versions.json")
public class HealthHungerTweaks
{
	
	public static Logger Log = LogManager.getLogger("healthhungertweaks");		
	
	@Instance
	public static HealthHungerTweaks instance;
	
	@SidedProxy(serverSide="net.cerulan.healthhungertweaks.CommonProxy", clientSide="net.cerulan.healthhungertweaks.ClientProxy")
	public static CommonProxy sidedProxy;
	
	public ConfigHandler configHandler;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		HealthHungerTweaks.Log.info("PreInitialization");
		configHandler = new ConfigHandler(new Configuration(event.getSuggestedConfigurationFile()));
		configHandler.load();
    }
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	HealthHungerTweaks.Log.info("Initialization");
        sidedProxy.init();
    }
    
    
}
