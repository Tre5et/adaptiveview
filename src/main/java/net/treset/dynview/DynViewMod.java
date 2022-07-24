package net.treset.dynview;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.treset.dynview.commands.CommandHandler;
import net.treset.dynview.config.Config;
import net.treset.dynview.distance.ServerTickHandler;
import net.treset.dynview.tools.MinecraftServerInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynViewMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("dynview");

	@Override
	public void onInitialize() {

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandHandler.registerCommands(dispatcher, environment);
		});

		ServerLifecycleEvents.SERVER_STARTED.register(MinecraftServerInstance::setInstance);
		ServerTickEvents.END_SERVER_TICK.register(ServerTickHandler::onTick);

		Config.load();
	}
}
