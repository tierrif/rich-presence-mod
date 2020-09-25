package io.github.hotlava03.richpresencemod.mixin;

import com.jagrosh.discordipc.entities.RichPresence;
import io.github.hotlava03.richpresencemod.RichPresenceListener;
import io.github.hotlava03.richpresencemod.RichPresenceMod;
import io.github.hotlava03.richpresencemod.config.Config;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ClientPlayNetworkHandler.class)
public class LeaveServerMixin {
    @Inject(at = @At("RETURN"), method = "onDisconnected")
    private void onServerDisconnect(Text reason) {
        Config config = Config.getInstance();
        RichPresence.Builder builder = new RichPresence.Builder()
                .setStartTimestamp(RichPresenceListener.getTimestamp())
                .setLargeImage(config.getStringValue("largeImage"),
                        config.getStringValue("largeImageText"))
                .setSmallImage(config.getStringValue("smallImage"),
                        config.getStringValue("smallImageText"))
                .setState(config.getStringValue("stateIdle"))
                .setDetails(config.getStringValue("detailsIdle"));
        try {
            RichPresenceMod.getClient().sendRichPresence(builder.build());
            LogManager.getLogger().info("Rich presence updated with new server data.");
        } catch (IllegalStateException e) {
            LogManager.getLogger().warn("Discord is not connected. Skipping rich presence update.");
        }
    }
}
