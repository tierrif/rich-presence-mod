package io.github.hotlava03.richpresencemod;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import io.github.hotlava03.richpresencemod.config.Config;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;

public class RichPresenceMod implements ModInitializer {
	private static RichPresenceMod instance;
	private IPCClient client;

	@Override
	public void onInitialize() {
		instance = this;
		this.connect();
		Config.getInstance().load();
	}

	public void connect() {
		client = new IPCClient(Long.parseLong(Config.getInstance().getStringValue("clientId")));
		client.setListener(new RichPresenceListener());
		try {
			client.connect(DiscordBuild.ANY);
			LogManager.getLogger().info("Rich presence enabled.");
		} catch (NoDiscordClientException e) {
			LogManager.getLogger().info("No Discord client found. Skipping rich presence.");
		} catch (IllegalStateException ignore) {
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static RichPresenceMod getInstance() {
		return instance;
	}

	public IPCClient getClient() {
		return client;
	}
}
