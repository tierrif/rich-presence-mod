package io.github.hotlava03.richpresencemod.mixin;

import com.jagrosh.discordipc.entities.RichPresence;
import com.mojang.authlib.GameProfile;
import io.github.hotlava03.richpresencemod.RichPresenceListener;
import io.github.hotlava03.richpresencemod.RichPresenceMod;
import io.github.hotlava03.richpresencemod.config.Config;
import io.github.hotlava03.richpresencemod.util.VarHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class JoinServerMixin {
	@Inject(at = @At("RETURN"), method = "<init>")
	private void onServerJoin(MinecraftClient mc, Screen screen, ClientConnection connection, GameProfile profile, CallbackInfo info) {
		VarHandler.update();
		Config config = Config.getInstance();
		RichPresence.Builder builder = new RichPresence.Builder()
				.setStartTimestamp(RichPresenceListener.getTimestamp())
				.setLargeImage(config.getStringValue("largeImage"),
						VarHandler.repl(config.getStringValue("largeImageText")))
				.setSmallImage(config.getStringValue("smallImage"),
						VarHandler.repl(config.getStringValue("smallImageText")));
		if (mc.getCurrentServerEntry() == null) {
			LogManager.getLogger().warn("Why would a server not exist when it's being connected to?");
			return;
		} else {
			builder.setState(VarHandler.repl(config.getStringValue("stateInServer")))
					.setDetails(VarHandler.repl(config.getStringValue("detailsInServer")));
		}
		try {
			RichPresenceMod.getInstance().getClient().sendRichPresence(builder.build());
			LogManager.getLogger().info("Rich presence updated with new server data.");
		} catch (IllegalStateException e) {
			LogManager.getLogger().warn("Discord is not connected. Skipping rich presence update.");
		}
	}
}
