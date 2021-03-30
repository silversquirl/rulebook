package vktec.rulebook;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;

import net.minecraft.world.GameRules;

public class RulebookMod implements ModInitializer {
	public static GameRules.Key<GameRules.IntRule> FILL_VOLUME =
		GameRuleRegistry.register("fillVolume", GameRules.Category.MISC, GameRuleFactory.createIntRule(0x8000));
	// TODO: sync with server.properties
	public static GameRules.Key<GameRules.IntRule> VIEW_DISTANCE =
		GameRuleRegistry.register("viewDistance", GameRules.Category.MISC, GameRuleFactory.createIntRule(10));

	@Override
	public void onInitialize() {}
}
