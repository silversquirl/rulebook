package vktec.rulebook;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.client.options.GameOptions;
import net.minecraft.world.GameRules;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

import vktec.rulebook.mixin.IntegratedServerAccessor;

public class RulebookMod implements ModInitializer {
	public static GameRules.Key<GameRules.IntRule> FILL_VOLUME =
		GameRuleRegistry.register("fillVolume", GameRules.Category.MISC, GameRuleFactory.createIntRule(0x8000, 0));
	public static GameRules.Key<GameRules.IntRule> VIEW_DISTANCE =
		GameRuleRegistry.register("viewDistance", GameRules.Category.MISC,
			GameRuleFactory.createIntRule(10, 0, 32, (server, rule) -> setViewDistance(server, rule.get())));
	public static GameRules.Key<GameRules.BooleanRule> LIGHT_FIRES_IN_AIR =
		GameRuleRegistry.register("lightFiresInAir", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			int viewDistance = server.getGameRules().getInt(VIEW_DISTANCE);
			setViewDistance(server, viewDistance);
		});
	}

	public static void setViewDistance(MinecraftServer server, int viewDistance) {
		if (!server.isDedicated()) {
			GameOptions options = ((IntegratedServerAccessor)server).getClient().options;
			options.viewDistance = viewDistance;
			options.write();
		}
		server.getPlayerManager().setViewDistance(viewDistance);
	}
}
