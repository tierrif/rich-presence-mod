package io.github.hotlava03.richpresencemod.mixin;

import com.jagrosh.discordipc.entities.RichPresence;
import com.mojang.authlib.GameProfile;
import io.github.hotlava03.richpresencemod.RichPresenceMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.OffsetDateTime;

@Mixin(ClientPlayNetworkHandler.class)
public class JoinServerMixin {
	@Inject(at = @At("RETURN"), method = "<init>")
	private void onServerJoin(MinecraftClient mc, Screen screen, ClientConnection connection, GameProfile profile, CallbackInfo info) {
		RichPresence.Builder builder = new RichPresence.Builder();
		if (mc.getCurrentServerEntry() == null) {
			// Shouldn't happen.
			LogManager.getLogger().warn("Why would a server not exist when it's being connected to?");
			return;
		}
		builder.setState("Playing in " + mc.getCurrentServerEntry().address)
				.setLargeImage("large-logo")
				.setDetails(mc.getCurrentServerEntry().playerCountLabel.getString() + " players online.")
				.setStartTimestamp(OffsetDateTime.now());
		try {
			RichPresenceMod.getClient().sendRichPresence(builder.build());
			LogManager.getLogger().info("Rich presence updated with new server data.");
		} catch (IllegalStateException e) {
			LogManager.getLogger().warn("Discord is not connected. Skipping rich presence update.");
		}
	}
}
