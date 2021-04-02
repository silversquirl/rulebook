package vktec.rulebook;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.client.options.GameOptions;
import net.minecraft.world.GameRules;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.util.math.ChunkPos;

import vktec.rulebook.mixin.IntegratedServerAccessor;

public class RulebookMod implements ModInitializer {
	public static GameRules.Key<GameRules.IntRule> FILL_VOLUME =
		GameRuleRegistry.register("fillVolume", GameRules.Category.MISC, GameRuleFactory.createIntRule(0x8000, 0));
	public static GameRules.Key<GameRules.BooleanRule> LIGHT_FIRES_IN_AIR =
		GameRuleRegistry.register("lightFiresInAir", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
	public static GameRules.Key<GameRules.IntRule> SPAWN_CHUNKS_RADIUS =
		GameRuleRegistry.register("spawnChunksRadius", GameRules.Category.MISC,
			GameRuleFactory.createIntRule(11, 0, (server, rule) -> setSpawnChunks(server, rule.get())));
	public static GameRules.Key<GameRules.IntRule> VIEW_DISTANCE =
		GameRuleRegistry.register("viewDistance", GameRules.Category.MISC,
			GameRuleFactory.createIntRule(10, 0, 32, (server, rule) -> setViewDistance(server, rule.get())));
	public static GameRules.Key<GameRules.BooleanRule> XZY_BLOCK_UPDATES =
		GameRuleRegistry.register("xzyBlockUpdates", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			oldSpawnChunksRadius = 11;
			int spawnChunksRadius = server.getGameRules().getInt(SPAWN_CHUNKS_RADIUS);
			setSpawnChunks(server, spawnChunksRadius);

			int viewDistance = server.getGameRules().getInt(VIEW_DISTANCE);
			setViewDistance(server, viewDistance);
		});
	}

	static int oldSpawnChunksRadius; // I really hope you don't have more than one MinecraftServer instance active at once
	public static void setSpawnChunks(MinecraftServer server, int radius) {
		ServerWorld world = server.getOverworld();
		ServerChunkManager manager = world.getChunkManager();
		ChunkPos spawnPos = new ChunkPos(world.getSpawnPos());
		if (oldSpawnChunksRadius != 0) {
			manager.removeTicket(ChunkTicketType.START, spawnPos, oldSpawnChunksRadius, Unit.INSTANCE);
		}
		if (radius != 0) {
			manager.addTicket(ChunkTicketType.START, spawnPos, radius, Unit.INSTANCE);
		}
		oldSpawnChunksRadius = radius;
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
