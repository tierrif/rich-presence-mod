package io.github.hotlava03.richpresencemod;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import io.github.hotlava03.richpresencemod.config.Config;
import io.github.hotlava03.richpresencemod.util.VarHandler;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;

import java.time.OffsetDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class RichPresenceListener implements IPCListener {
    private static OffsetDateTime timestamp;
    private Timer timer;

    @Override
    public void onReady(IPCClient client) {
        timestamp = OffsetDateTime.now();
        reloadRichPresence(client);
        this.timer = new Timer();
        startTimer(client);
    }

    public void stopTimer() {
        this.timer.cancel();
    }

    public void startTimer(IPCClient client) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    client.connect(DiscordBuild.ANY);
                } catch (NoDiscordClientException ignore) {
                }
            }
        }, 0, 10000);
    }

    public static OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public static void reloadRichPresence(IPCClient client) {
        VarHandler.update();
        Config config = Config.getInstance();
        RichPresence.Builder builder = new RichPresence.Builder()
                .setStartTimestamp(timestamp)
                .setLargeImage(config.getStringValue("largeImage"),
                        VarHandler.repl(config.getStringValue("largeImageText")))
                .setSmallImage(config.getStringValue("smallImage"),
                        VarHandler.repl(config.getStringValue("smallImageText")));
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getCurrentServerEntry() == null) {
            builder.setState(VarHandler.repl(config.getStringValue("stateIdle")))
                    .setDetails(VarHandler.repl(config.getStringValue("detailsIdle")));
        } else {
            builder.setState(VarHandler.repl(config.getStringValue("stateInServer")))
                    .setDetails(VarHandler.repl(config.getStringValue("detailsInServer")));
        }
        try {
            client.sendRichPresence(builder.build());
            LogManager.getLogger().info("Rich presence updated.");
        } catch (IllegalStateException e) {
            LogManager.getLogger().warn("Discord is not connected. Skipping rich presence update.");
        }
    }
}
