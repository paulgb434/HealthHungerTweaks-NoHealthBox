package net.cerulan.healthhungertweaks.handler;

import net.cerulan.healthhungertweaks.HealthHungerTweaks;
import net.cerulan.healthhungertweaks.capability.healthregen.HealthRegenCapabilityHandler;
import net.cerulan.healthhungertweaks.capability.healthregen.IHealthRegenCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.world.WorldEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.HealthRegenEvent;
import toughasnails.api.stat.capability.IThirst;

public class HealthHungerHandler {	
	
	public HealthHungerHandler() {
	}

	@SubscribeEvent
	public void allowNormalRegen(HealthRegenEvent.AllowRegen event) {
		//event.setResult(Result.DENY);
		allowRegen(event);
	}
	
	@SubscribeEvent
	public void allowSaturatedRegen(HealthRegenEvent.AllowSaturatedRegen event) {
		//event.setResult(Result.DENY);
		allowRegen(event);
	}
	
	private void allowRegen(HealthRegenEvent event) {
		if (HealthHungerTweaks.instance.configHandler.shouldDisableRegularRegen()) {
			event.setResult(Result.DENY);
		}
	}
	
	// TODO Peaceful config
	
	@SubscribeEvent
	public void getMaxExhaustion(ExhaustionEvent.GetMaxExhaustion event) {
		event.maxExhaustionLevel *= HealthHungerTweaks.instance.configHandler.getExhaustionModifier();
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			if (event.player.hasCapability(HealthRegenCapabilityHandler.HEALTH_REGEN, null)) {

				IHealthRegenCapability hRegenCap = event.player.getCapability(HealthRegenCapabilityHandler.HEALTH_REGEN,
						null);

				int untilStart = hRegenCap.getTicksUntilRegenStart();
				int untilNext = hRegenCap.getTicksUntilNextRegen();
				if (event.player.getFoodStats().getFoodLevel() >= HealthHungerTweaks.instance.configHandler.getMinimumHunger()
						&& (!Loader.isModLoaded("toughasnails") || getThirst(event.player) >= HealthHungerTweaks.instance.configHandler.getMinimumThirst()) // Tough as Nails Integration
						&& event.player.getHealth() < event.player.getMaxHealth()) {
					if (untilStart > 0) {
						untilStart--;
					} else if (untilStart == 0 && untilNext > 0) {
						untilNext--;
					} else if (untilStart == 0 && untilNext == 0) {
						untilNext = HealthHungerTweaks.instance.configHandler.getDelayBetweenTicks();
						if (!event.player.world.isRemote && event.player.getHealth() < event.player.getMaxHealth()) {
							if (HealthHungerTweaks.instance.configHandler.getUsePercent()) {
								event.player.heal((float)(HealthHungerTweaks.instance.configHandler.getPercentAmount() * event.player.getMaxHealth()));
								event.player.getFoodStats().addExhaustion(6F);
							}
							else {
								event.player.heal((float)(HealthHungerTweaks.instance.configHandler.getStaticAmount()));
								event.player.getFoodStats().addExhaustion(6F);
							}
						}
					}
				} else {
					untilStart = HealthHungerTweaks.instance.configHandler.getDelayUntilStart();
				}
				hRegenCap.setData(untilStart, untilNext);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity(); 
			if (player.hasCapability(HealthRegenCapabilityHandler.HEALTH_REGEN, null)) {
				IHealthRegenCapability cap = player.getCapability(HealthRegenCapabilityHandler.HEALTH_REGEN, null);
				cap.setData(HealthHungerTweaks.instance.configHandler.getDelayUntilStart(), HealthHungerTweaks.instance.configHandler.getDelayBetweenTicks());
			}

		}

	}
	
	@SubscribeEvent
	public void event(WorldEvent.Load event){
		event.getWorld().getGameRules().setOrCreateGameRule("naturalRegeneration", "false");
	}
	
	/* TOUGH AS NAILS INTEGRATION */
	
	@CapabilityInject(IThirst.class)
	private static final Capability<IThirst> THIRST = null; 
	
	private int getThirst(EntityPlayer player) {
		if (Loader.isModLoaded("toughasnails") && player.hasCapability(THIRST, null)) {
			return player.getCapability(THIRST, null).getThirst();
		}
		return 20;
	}
	
}
