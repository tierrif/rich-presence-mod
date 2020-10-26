package io.github.hotlava03.richpresencemod.mixin;

import com.jagrosh.discordipc.entities.RichPresence;
import io.github.hotlava03.richpresencemod.RichPresenceListener;
import io.github.hotlava03.richpresencemod.RichPresenceMod;
import io.github.hotlava03.richpresencemod.config.Config;
import io.github.hotlava03.richpresencemod.util.VarHandler;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class LeaveServerMixin {
    @Inject(at = @At("RETURN"), method = "disconnect")
    private void disconnect(CallbackInfo info) {
        VarHandler.update();
        Config config = Config.getInstance();
        RichPresence.Builder builder = new RichPresence.Builder()
                .setStartTimestamp(RichPresenceListener.getTimestamp())
                .setLargeImage(config.getStringValue("largeImage"),
                        VarHandler.repl(config.getStringValue("largeImageText")))
                .setSmallImage(config.getStringValue("smallImage"),
                        VarHandler.repl(config.getStringValue("smallImageText")))
                .setState(VarHandler.repl(config.getStringValue("stateIdle")))
                .setDetails(VarHandler.repl(config.getStringValue("detailsIdle")));
        try {
            RichPresenceMod.getInstance().getClient().sendRichPresence(builder.build());
            LogManager.getLogger().info("Rich presence updated with new server data.");
        } catch (IllegalStateException e) {
            LogManager.getLogger().warn("Discord is not connected. Skipping rich presence update.");
        }
    }
}
