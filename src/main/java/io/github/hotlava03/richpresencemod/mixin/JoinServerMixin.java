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
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class JoinServerMixin {
    @Inject(at = @At("RETURN"), method = "<init>")
    private void onServerJoin(MinecraftClient client, Screen screen, ClientConnection connection, GameProfile profile, CallbackInfo ci) {
        LogManager.getLogger().info(1);
        // Update the rich presence variables.
        VarHandler.update();
        LogManager.getLogger().info(2);
        // Get values from config.
        Config config = Config.getInstance();
        LogManager.getLogger().info(3);
        // Build RichPresence to set the new values.
        RichPresence.Builder builder = new RichPresence.Builder()
                .setStartTimestamp(RichPresenceListener.getTimestamp())
                .setLargeImage(config.getStringValue("largeImage"),
                        VarHandler.repl(config.getStringValue("largeImageText")))
                .setSmallImage(config.getStringValue("smallImage"),
                        VarHandler.repl(config.getStringValue("smallImageText")))
                .setState(VarHandler.repl(config.getStringValue("stateInServer")));
        LogManager.getLogger().info(4);
        // Check if we're in singleplayer or a multiplayer server.
        String playerCount = VarHandler.getVariableValue("playerCount");
        if (playerCount == null || playerCount.equals("-1")) { // -1 means singleplayer.
            builder.setDetails(VarHandler.repl(config.getStringValue("detailsInSingleplayer")));
        } else {
            builder.setDetails(VarHandler.repl(config.getStringValue("detailsInServer")));
        }
        LogManager.getLogger().info(5);
        // Attempt to update the rich presence in Discord.
        try {
            RichPresenceMod.getInstance().getClient().sendRichPresence(builder.build());
            LogManager.getLogger().info("Rich presence updated with new server data.");
        } catch (IllegalStateException e) {
            LogManager.getLogger().warn("Discord is not connected. Skipping rich presence update.");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        LogManager.getLogger().info(6);
    }
}
