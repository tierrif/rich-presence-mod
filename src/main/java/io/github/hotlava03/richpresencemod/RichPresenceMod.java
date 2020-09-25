package io.github.hotlava03.richpresencemod;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;

public class RichPresenceMod implements ModInitializer {
	private static IPCClient client;

	@Override
	public void onInitialize() {
		client = new IPCClient(758455515658846269L);
		client.setListener(new RichPresenceListener());
		try {
			client.connect(DiscordBuild.ANY);
			LogManager.getLogger().info("Rich presence enabled.");
		} catch (NoDiscordClientException e) {
			LogManager.getLogger().info("No Discord client found. Skipping rich presence.");
		}
	}

	public static IPCClient getClient() {
		return client;
	}
}
