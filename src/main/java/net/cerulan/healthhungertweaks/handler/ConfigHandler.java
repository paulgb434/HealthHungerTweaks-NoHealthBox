package net.cerulan.healthhungertweaks.handler;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	private Configuration config;
	public ConfigHandler(Configuration config) {
		this.config = config;
	}
	
	private double exhaustionModifier;
	
	private int cdX, cdY;
	private boolean screenDarkenWhenInjure;
	
	private boolean disableRegularRegen;
	private int delayUntilStart;
	private int delayBetweenTicks;
	private int minimumHunger;
	private int minimumThirst; // Integration with ToughAsNails
	private boolean usePercent;
	private double percentAmount;
	private int staticAmount;
	
	public void load() {
		cdX = config.get("client", "cooldownX", 5, "The X Coordinate of the Cooldown Indicator").getInt();
		cdY = config.get("client", "cooldownY", 5, "The Y Coordinate of the Cooldown Indicator").getInt();
		screenDarkenWhenInjure = config.get("client", "screenDarken", true, "Whether or not the screen should darken when injured").getBoolean();
		
		exhaustionModifier = config.get("exhaustion", "exhaustionModifier", 1.0, "An exhaustion modifier that will be multiplied to the default maximum exhaustion. Higher values mean slower food drain.").getDouble();
		
		disableRegularRegen = config.get("mending", "disableRegularRegen", true, "Toggles whether regular regen (from food) should be disabled, and players must use health kits. Recommended if food is made easier. (This in itself also makes food easier as saturation will not be consumed to restore health).").getBoolean();
		delayUntilStart = config.get("mending", "delayUntilStart", 200, "The delay (in ticks) before a player will begin to regenerate health. Negative values disable this functionality, forcing players to rely solely on health kits or potions.").getInt();
		delayBetweenTicks = config.get("mending", "delayBetweenTicks", 10, "The delay (in ticks) between each regeneration.").getInt();
		
		minimumHunger = config.get("mending", "minimumHunger", 6, "The minimum hunger (in half-shanks) necessary to be able to heal.").getInt();
		
		minimumThirst = config.get("mending", "minimumThirst", 6, "ToughAsNails integration: The minimum thirst (in half-drops) necessary to be able to heal. No effect if ToughAsNails is not installed.").getInt();
		
		
		usePercent = config.get("mending", "usePercent", true, "Regeneration will heal for a percent of maximum health, rather that a flat value.").getBoolean();		
		percentAmount = config.get("mending", "percentAmount", 0.05, "The percent of maximum health to regenerate each regeneration if usePercent is enabled.").getDouble();
		staticAmount = config.get("mending", "staticAmount", 1, "The amount of half-hearts to heal each regeneration if usePercent is disabled.").getInt();
		
		
		config.save();
	}
	
	public double getExhaustionModifier() { return exhaustionModifier; }
	
	public boolean shouldDisableRegularRegen() { return disableRegularRegen; }
	
	public boolean shouldScreenDarkenWhenInjured() { return this.screenDarkenWhenInjure; }

	public int getDelayUntilStart() {
		return delayUntilStart;
	}

	public int getDelayBetweenTicks() {
		return delayBetweenTicks;
	}

	public int getMinimumHunger() {
		return minimumHunger;
	}
	
	public int getMinimumThirst() {
		return minimumThirst;
	}
	
	public boolean getUsePercent() {
		return this.usePercent;
	}
	
	public double getPercentAmount() { return this.percentAmount; }
	public int getStaticAmount() { return this.staticAmount; }
}
